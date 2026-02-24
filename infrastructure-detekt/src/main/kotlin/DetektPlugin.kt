package com.redmadrobot.build.detekt

import com.android.build.api.variant.AndroidComponentsExtension
import com.redmadrobot.build.InfrastructurePlugin
import com.redmadrobot.build.StaticAnalyzerSpec
import com.redmadrobot.build.detekt.CollectGitDiffFilesTask.ChangeType
import com.redmadrobot.build.detekt.CollectGitDiffFilesTask.FilterParams
import com.redmadrobot.build.detekt.DetektPlugin.Companion.BASELINE_KEYWORD
import com.redmadrobot.build.detekt.internal.*
import com.redmadrobot.build.internal.InternalGradleInfrastructureApi
import com.redmadrobot.build.internal.addRepositoriesIfNeed
import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.DetektCreateBaselineTask
import org.gradle.api.Project
import org.gradle.api.tasks.SourceTask
import org.gradle.api.tasks.TaskProvider
import org.gradle.kotlin.dsl.*

/**
 * Plugin with common configurations for detekt.
 * Should be applied to root project only.
 *
 * Tied to `com.redmadrobot.detekt` plugin ID.
 */
public class DetektPlugin : InfrastructurePlugin() {

    @InternalGradleInfrastructureApi
    override fun Project.configure() {
        apply(plugin = "io.gitlab.arturbosch.detekt")

        val detektOptions = createExtension<DetektOptionsImpl>("detekt")

        configureDependencies()

        configureDetektFormatTask(redmadrobotExtension)
        configureDetektAllTasks(redmadrobotExtension)
        configureDetektDiffTask(redmadrobotExtension, detektOptions)
    }

    internal companion object {
        const val BASELINE_KEYWORD = "Baseline"
    }
}

@OptIn(InternalGradleInfrastructureApi::class)
private fun Project.configureDependencies() {
    addRepositoriesIfNeed {
        mavenCentral()
    }

    // DON't USE VersionCatalogsExtension here
    dependencies {
        detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:1.23.8")
    }
}

private fun Project.configureDetektFormatTask(staticAnalyzerSpec: StaticAnalyzerSpec) {
    detektTask(staticAnalyzerSpec, "detektFormat") {
        description = "Reformats whole code base."
        disableDefaultRuleSets = true
        autoCorrect = true
    }
}

private fun Project.configureDetektAllTasks(
    staticAnalyzerSpec: StaticAnalyzerSpec,
) {
    detektTask(staticAnalyzerSpec, "detektAll") {
        description = "Runs over whole code base without the starting overhead for each module."
    }

    detektCreateBaselineTask(
        staticAnalyzerSpec,
        "detekt${BASELINE_KEYWORD}All",
    ) {
        description = "Runs over whole code base without the starting overhead for each module."
    }

    val variantRegex = Regex("detekt($BASELINE_KEYWORD)?([A-Za-z]+)All$")
    val relativePath = project.path.replace(Regex("^:"), "")
    val taskRegex = Regex("^(:?$relativePath:)?$variantRegex")
    val startTask = gradle.startParameter.taskNames.find { it.contains(taskRegex) }
    if (startTask != null && startTask != "detekt${BASELINE_KEYWORD}All") {
        val taskData = variantRegex.find(startTask)?.groupValues
        val detektTaskName = taskData?.get(0) ?: return
        val isBaseline = taskData[1] == BASELINE_KEYWORD
        val requiredVariant = taskData[2]

        // Регистрируем onVariants-коллбэки в фазе конфигурации — до регистрации задач.
        // withPlugin срабатывает при конфигурации каждого субпроекта, onVariants — когда
        // AGP обрабатывает варианты. К моменту выполнения lazy-блоков задач оба коллбэка
        // уже отработали и Set<String> содержит актуальные имена вариантов.
        val variantNamesByModule = registerAndroidVariantCollectors()

        if (isBaseline) {
            detektCreateBaselineTask(staticAnalyzerSpec, detektTaskName) {
                val modules = subprojects.filter(Project::hasKotlinPlugin)
                modules.checkModulesContainDetekt { modulesNames ->
                    "Modules $modulesNames don't contain \"detekt\" or \"redmadrobot.detekt\" plugin"
                }

                val detektTaskProviders = modules.map { subproject ->
                    subproject.extractDetektTaskProviderByType<DetektCreateBaselineTask>(
                        requiredVariant,
                        variantNamesByModule[subproject].orEmpty(),
                    )
                }

                classpath.setFrom(detektTaskProviders.map { it.map(DetektCreateBaselineTask::classpath) })
                setSource(detektTaskProviders.map { it.map(DetektCreateBaselineTask::getSource) })
            }
        } else {
            detektTask(staticAnalyzerSpec, detektTaskName) {
                val modules = subprojects.filter(Project::hasKotlinPlugin)
                modules.checkModulesContainDetekt { modulesNames ->
                    "Modules $modulesNames don't contain \"detekt\" or \"redmadrobot.detekt\" plugin"
                }

                val detektTaskProviders = modules.map { subproject ->
                    subproject.extractDetektTaskProviderByType<Detekt>(
                        requiredVariant,
                        variantNamesByModule[subproject].orEmpty(),
                    )
                }

                classpath.setFrom(detektTaskProviders.map { it.map(Detekt::classpath) })
                setSource(detektTaskProviders.map { it.map(Detekt::getSource) })
            }
        }
    }
}

private fun Project.configureDetektDiffTask(
    staticAnalyzerSpec: StaticAnalyzerSpec,
    detektOptions: DetektOptions,
) {
    val detektDiffOptions = detektOptions.detektDiffOptions.orNull
    if (detektDiffOptions != null && detektDiffOptions.baseBranch.isNotEmpty()) {
        val changedFilesFilter = FilterParams(
            changeTypes = listOf(ChangeType.ADD, ChangeType.MODIFY),
            fileExtensions = detektDiffOptions.fileExtensions,
        )

        val findChangedFiles = tasks.register<CollectGitDiffFilesTask>("findChangedFiles") {
            projectDir = layout.projectDirectory
            filterParams = changedFilesFilter
            branch = detektDiffOptions.baseBranch
        }

        detektTask(staticAnalyzerSpec, "detektDiff") {
            description = "Check out only changed files versus the ${detektDiffOptions.baseBranch} branch"
            setSource(files(findChangedFiles.map { it.outputFiles.from }))
        }
    }
}

private inline fun Project.detektTask(
    staticAnalyzerSpec: StaticAnalyzerSpec,
    name: String,
    crossinline configure: Detekt.() -> Unit,
): TaskProvider<Detekt> {
    return tasks.register<Detekt>(name) {
        parallel = true
        config.setFrom(provider { staticAnalyzerSpec.configsDir.get().file("detekt/detekt.yml") })
        baseline = provider { staticAnalyzerSpec.configsDir.getFileIfExists("detekt/baseline.xml") }
        setSource(projectDir)
        reportsDir = staticAnalyzerSpec.reportsDir.asFile
        include("**/*.kt")
        include("**/*.kts")
        exclude("**/res/**")
        exclude("**/build/**")
        exclude("**/.*/**")
        reports {
            xml.required = true
            txt.required = false
            html.required = false
        }
        configure()
    }
}

private inline fun Project.detektCreateBaselineTask(
    staticAnalyzerSpec: StaticAnalyzerSpec,
    name: String,
    crossinline configure: DetektCreateBaselineTask.() -> Unit,
): TaskProvider<DetektCreateBaselineTask> {
    return tasks.register<DetektCreateBaselineTask>(name) {
        parallel = true
        config.setFrom(provider { staticAnalyzerSpec.configsDir.get().file("detekt/detekt.yml") })
        baseline = provider { staticAnalyzerSpec.configsDir.get().file("detekt/baseline.xml") }
        setSource(projectDir)
        include("**/*.kt")
        include("**/*.kts")
        exclude("**/res/**")
        exclude("**/build/**")
        exclude("**/.*/**")
        configure()
    }
}

@Suppress("DefaultLocale")
private inline fun <reified T : SourceTask> Project.extractDetektTaskProviderByType(
    variantName: String,
    androidVariantNames: Set<String>,
): TaskProvider<T> {
    val taskSuffix = BASELINE_KEYWORD
        .takeIf { T::class == DetektCreateBaselineTask::class }
        .orEmpty()

    val taskProvider = if (isKotlinAndroidProject) {
        val requiredVariant = androidVariantNames.find { it.equals(variantName, ignoreCase = true) }
        checkNotNull(requiredVariant) {
            val candidates = androidVariantNames.joinToString(", ") { variant ->
                "'${createDetektVariantTaskName(taskSuffix, variant, "All")}'"
            }
            "Task ${createDetektVariantTaskName(taskSuffix, variantName, "All")} not found in project. " +
                "Some candidates are: $candidates"
        }

        tasks.named<T>(createDetektVariantTaskName(taskSuffix, requiredVariant))
    } else {
        tasks.named<T>(createDetektVariantTaskName(taskSuffix, "Main"))
    }

    return taskProvider.also { taskProvider.configure { isEnabled = false } }
}

private fun Project.registerAndroidVariantCollectors(): Map<Project, Set<String>> {
    val result = mutableMapOf<Project, MutableSet<String>>()

    subprojects.forEach { subproject ->
        subproject.pluginManager.withPlugin("kotlin-android") {
            val names = mutableSetOf<String>()
            result[subproject] = names
            (subproject.extensions.findByName("androidComponents") as? AndroidComponentsExtension<*, *, *>)
                ?.onVariants { variant -> names.add(variant.name) }
        }
    }

    return result
}

private fun createDetektVariantTaskName(suffix: String, variantName: String, postfix: String = ""): String {
    return "detekt$suffix${variantName.capitalized()}$postfix"
}

private fun List<Project>.checkModulesContainDetekt(lazyMessage: (String) -> String) {
    val detektMissingModules = filterNot(Project::hasDetektPlugin)

    check(detektMissingModules.isEmpty()) {
        val modulesName = detektMissingModules.joinToString(", ") { project -> "\"${project.name}\"" }
        lazyMessage.invoke(modulesName)
    }
}
