package com.redmadrobot.build

import com.android.build.gradle.AppExtension
import com.android.build.gradle.BaseExtension
import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.TestExtension
import com.android.build.gradle.internal.core.InternalBaseVariant
import com.redmadrobot.build.DetektPlugin.Companion.BASELINE_KEYWORD
import com.redmadrobot.build.extension.RedmadrobotExtension
import com.redmadrobot.build.internal.checkAllSubprojectsContainPlugin
import com.redmadrobot.build.internal.detekt.CollectGitDiffFilesTask
import com.redmadrobot.build.internal.detekt.CollectGitDiffFilesTask.ChangeType
import com.redmadrobot.build.internal.detekt.CollectGitDiffFilesTask.FilterParams
import com.redmadrobot.build.internal.detektPlugins
import com.redmadrobot.build.internal.getFileIfExists
import com.redmadrobot.build.internal.isInfrastructureRootProject
import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.DetektCreateBaselineTask
import io.gitlab.arturbosch.detekt.DetektPlugin
import org.gradle.api.Project
import org.gradle.api.tasks.SourceTask
import org.gradle.api.tasks.TaskProvider
import org.gradle.kotlin.dsl.*

/**
 * Plugin with common configurations for detekt.
 * Should be applied to root project only.
 *
 * Tied to `redmadrobot.detekt` plugin ID.
 */
public class DetektPlugin : InfrastructurePlugin() {

    override fun Project.configure() {
        apply(plugin = "io.gitlab.arturbosch.detekt")

        configureDependencies()
        configureDetektTasks(redmadrobotExtension, infrastructureRootProject)
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
        detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:1.18.1")
    }
}

private fun Project.configureDetektTasks(extension: RedmadrobotExtension, infrastructureRootProject: Project) {
    configureDetektFormatTask(extension, infrastructureRootProject)
    configureDetektAllTasks(extension, infrastructureRootProject)
    configureDetektDiffTask(extension, infrastructureRootProject)
}

private fun Project.configureDetektFormatTask(extension: RedmadrobotExtension, infrastructureRootProject: Project) {
    detektTask(extension, infrastructureRootProject, "detektFormat") {
        description = "Reformats whole code base."
        disableDefaultRuleSets = true
        autoCorrect = true
    }
}

private fun Project.configureDetektAllTasks(extension: RedmadrobotExtension, infrastructureRootProject: Project) {
    detektTask(extension, infrastructureRootProject, "detektAll") {
        description = "Runs over whole code base without the starting overhead for each module."
    }

    detektCreateBaselineTask(extension, infrastructureRootProject, "detekt${BASELINE_KEYWORD}All") {
        description = "Runs over whole code base without the starting overhead for each module."
    }

    if (project.isInfrastructureRootProject) {
        val variantRegex = Regex("^detekt($BASELINE_KEYWORD)?([A-Za-z]+)All$")
        val startTask = gradle.startParameter.taskNames.find { it.contains(variantRegex) }
        if (startTask != null && startTask != "detekt${BASELINE_KEYWORD}All") {
            val taskData = variantRegex.find(startTask)?.groups
            val requiredVariant = taskData?.get(2)?.value.orEmpty()
            val isBaseline = taskData?.get(1)?.value == BASELINE_KEYWORD

            if (isBaseline) {
                detektCreateBaselineTask(extension, infrastructureRootProject, startTask) {
                    checkAllSubprojectsContainPlugin<DetektPlugin> { modulesNames ->
                        "Modules $modulesNames don't contain \"detekt\" or \"redmadrobot.detekt\" plugin"
                    }

                    val detektTaskProviders = subprojects.map { subproject ->
                        subproject.extractDetektTaskProviderByType<DetektCreateBaselineTask>(requiredVariant)
                    }

                    val allClasspath = detektTaskProviders.map { it.map(DetektCreateBaselineTask::classpath) }
                    val allSources = detektTaskProviders.map { it.map(DetektCreateBaselineTask::getSource) }

                    classpath.setFrom(allClasspath)
                    setSource(allSources)
                }
            } else {
                detektTask(extension, infrastructureRootProject, startTask) {
                    checkAllSubprojectsContainPlugin<DetektPlugin> { modulesNames ->
                        "Modules $modulesNames don't contain \"detekt\" or \"redmadrobot.detekt\" plugin"
                    }

                    val detektTaskProviders = subprojects.map { subproject ->
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
}

private fun Project.configureDetektDiffTask(extension: RedmadrobotExtension, infrastructureRootProject: Project) {
    val detektDiffOptions = extension.detekt.detektDiffOptions.orNull
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

        detektTask(extension, infrastructureRootProject, "detektDiff") {
            description = "Check out only changed files versus the ${detektDiffOptions.baseBranch} branch"
            setSource(infrastructureRootProject.files(findChangedFiles.map { it.outputFiles.from }))
        }
    }
}

private inline fun Project.detektTask(
    extension: RedmadrobotExtension,
    infrastructureRootProject: Project,
    name: String,
    crossinline configure: Detekt.() -> Unit,
): TaskProvider<Detekt> {
    return tasks.register<Detekt>(name) {
        parallel = true
        config.setFrom(provider { extension.configsDir.get().file("detekt/detekt.yml") })
        baseline.set(provider { extension.configsDir.getFileIfExists("detekt/baseline.xml") })
        setSource(infrastructureRootProject.projectDir)
        reportsDir.set(extension.reportsDir.asFile)
        include("**/*.kt")
        include("**/*.kts")
        exclude("**/res/**")
        exclude("**/build/**")
        exclude("**/.*/**")
        reports {
            xml.enabled = true
            txt.enabled = false
            html.enabled = false
        }
        configure()
    }
}

private inline fun Project.detektCreateBaselineTask(
    extension: RedmadrobotExtension,
    infrastructureRootProject: Project,
    name: String,
    crossinline configure: DetektCreateBaselineTask.() -> Unit,
): TaskProvider<DetektCreateBaselineTask> {
    return tasks.register<DetektCreateBaselineTask>(name) {
        parallel.set(true)
        config.setFrom(provider { extension.configsDir.get().file("detekt/detekt.yml") })
        baseline.set(provider { extension.configsDir.get().file("detekt/baseline.xml") })
        setSource(infrastructureRootProject.projectDir)
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
    val isSubprojectAndroid = plugins.hasPlugin("com.android.application") ||
        plugins.hasPlugin("com.android.library")

    val taskSuffix = BASELINE_KEYWORD.takeIf { T::class == DetektCreateBaselineTask::class }.orEmpty()

    val taskProvider = if (isSubprojectAndroid) {
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

@Suppress("DefaultLocale")
private fun BaseExtension.checkVariantExists(variantName: String, lazyMessage: (List<String>) -> String) {
    val variants = when (this) {
        is AppExtension -> applicationVariants
        is LibraryExtension -> libraryVariants
        is TestExtension -> applicationVariants
        else -> null
    }
    val requiredVariant = variants?.find { it.name.capitalize() == variantName }
    checkNotNull(requiredVariant) { lazyMessage.invoke(variants?.map(InternalBaseVariant::getName).orEmpty()) }
}

private fun createDetektVariantTaskName(suffix: String, variantName: String, postfix: String = ""): String {
    return "detekt$suffix$variantName$postfix"
}
