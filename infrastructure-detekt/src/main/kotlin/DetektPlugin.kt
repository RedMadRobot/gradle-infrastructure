package com.redmadrobot.build.detekt

import com.android.build.gradle.BaseExtension
import com.redmadrobot.build.InfrastructurePlugin
import com.redmadrobot.build.StaticAnalyzerSpec
import com.redmadrobot.build.detekt.CollectGitDiffFilesTask.ChangeType
import com.redmadrobot.build.detekt.CollectGitDiffFilesTask.FilterParams
import com.redmadrobot.build.detekt.DetektPlugin.Companion.BASELINE_KEYWORD
import com.redmadrobot.build.detekt.internal.*
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

private fun Project.configureDependencies() {
    repositories {
        mavenCentral()
    }

    dependencies {
        detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:1.19.0")
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

        if (isBaseline) {
            detektCreateBaselineTask(staticAnalyzerSpec, detektTaskName) {
                val modules = subprojects.filter(Project::hasKotlinPlugin)
                modules.checkModulesContainDetekt { modulesNames ->
                    "Modules $modulesNames don't contain \"detekt\" or \"redmadrobot.detekt\" plugin"
                }

                val detektTaskProviders = modules.map { subproject ->
                    subproject.extractDetektTaskProviderByType<DetektCreateBaselineTask>(requiredVariant)
                }

                val allClasspath = detektTaskProviders.map { it.map(DetektCreateBaselineTask::classpath) }
                val allSources = detektTaskProviders.map { it.map(DetektCreateBaselineTask::getSource) }

                classpath.setFrom(allClasspath)
                setSource(allSources)
            }
        } else {
            detektTask(staticAnalyzerSpec, detektTaskName) {
                val modules = subprojects.filter(Project::hasKotlinPlugin)
                modules.checkModulesContainDetekt { modulesNames ->
                    "Modules $modulesNames don't contain \"detekt\" or \"redmadrobot.detekt\" plugin"
                }

                val detektTaskProviders = modules.map { subproject ->
                    subproject.extractDetektTaskProviderByType<Detekt>(requiredVariant)
                }

                val allClasspath = detektTaskProviders.map { it.map(Detekt::classpath) }
                val allSources = detektTaskProviders.map { it.map(Detekt::getSource) }

                classpath.setFrom(allClasspath)
                setSource(allSources)
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
            projectDir.set(layout.projectDirectory)
            filterParams.set(changedFilesFilter)
            branch.set(detektDiffOptions.baseBranch)
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
        baseline.set(provider { staticAnalyzerSpec.configsDir.getFileIfExists("detekt/baseline.xml") })
        setSource(projectDir)
        reportsDir.set(staticAnalyzerSpec.reportsDir.asFile)
        include("**/*.kt")
        include("**/*.kts")
        exclude("**/res/**")
        exclude("**/build/**")
        exclude("**/.*/**")
        reports {
            xml.required.set(true)
            txt.required.set(false)
            html.required.set(false)
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
        parallel.set(true)
        config.setFrom(provider { staticAnalyzerSpec.configsDir.get().file("detekt/detekt.yml") })
        baseline.set(provider { staticAnalyzerSpec.configsDir.get().file("detekt/baseline.xml") })
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
): TaskProvider<T> {
    val taskSuffix = BASELINE_KEYWORD
        .takeIf { T::class == DetektCreateBaselineTask::class }
        .orEmpty()

    val taskProvider = if (isKotlinAndroidProject) {
        val baseExtensions = extensions.getByType<BaseExtension>()
        baseExtensions.checkVariantExists(variantName) { existingVariants ->
            val candidates = existingVariants.joinToString(", ") { variant ->
                "'${createDetektVariantTaskName(taskSuffix, variant.capitalize(), "All")}'"
            }
            "Task ${createDetektVariantTaskName(taskSuffix, variantName, "All")} not found in project. " +
                "Some candidates are: $candidates"
        }

        tasks.named<T>(createDetektVariantTaskName(taskSuffix, variantName))
    } else {
        tasks.named<T>(createDetektVariantTaskName(taskSuffix, "Main"))
    }

    return taskProvider.also { taskProvider.configure { isEnabled = false } }
}

private fun BaseExtension.checkVariantExists(variantName: String, lazyMessage: (List<String>) -> String) {
    val requiredVariant = variants?.find { it.name.capitalize() == variantName }
    checkNotNull(requiredVariant) { lazyMessage.invoke(variants?.map { it.name }.orEmpty()) }
}

private fun createDetektVariantTaskName(suffix: String, variantName: String, postfix: String = ""): String {
    return "detekt$suffix$variantName$postfix"
}

private fun List<Project>.checkModulesContainDetekt(lazyMessage: (String) -> String) {
    val detektMissingModules = filterNot(Project::hasDetektPlugin)

    check(detektMissingModules.isEmpty()) {
        val modulesName = detektMissingModules.joinToString(", ") { project -> "\"${project.name}\"" }
        lazyMessage.invoke(modulesName)
    }
}
