package com.redmadrobot.build.detekt

import com.android.build.gradle.AppExtension
import com.android.build.gradle.BaseExtension
import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.TestExtension
import com.android.build.gradle.internal.core.InternalBaseVariant
import com.redmadrobot.build.DetektPlugin
import com.redmadrobot.build.extension.RedmadrobotExtension
import com.redmadrobot.build.internal.detekt.CollectGitDiffFilesTask
import com.redmadrobot.build.internal.getFileIfExists
import com.redmadrobot.build.internal.hasKotlinPlugin
import com.redmadrobot.build.internal.hasPlugin
import com.redmadrobot.build.internal.isInfrastructureRootProject
import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.DetektCreateBaselineTask
import org.gradle.api.Project
import org.gradle.api.tasks.SourceTask
import org.gradle.api.tasks.TaskProvider
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.named
import org.gradle.kotlin.dsl.register

internal class DetektConfiguration(
    private val project: Project,
) {

    fun configure(extension: RedmadrobotExtension, infrastructureRootProject: Project) = with(project) {
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

        detektCreateBaselineTask(
            extension,
            infrastructureRootProject,
            "detekt${DetektPlugin.BASELINE_KEYWORD}All",
        ) {
            description = "Runs over whole code base without the starting overhead for each module."
        }

        if (project.isInfrastructureRootProject) {
            val variantRegex = Regex("detekt(${DetektPlugin.BASELINE_KEYWORD})?([A-Za-z]+)All$")
            val taskRegex = Regex("^(:?${project.name.orEmpty()}:)?$variantRegex")
            val startTask = gradle.startParameter.taskNames.find { it.contains(taskRegex) }
            if (startTask != null && startTask != "detekt${DetektPlugin.BASELINE_KEYWORD}All") {
                val taskData = variantRegex.find(startTask)?.groupValues
                val detektTaskName = taskData?.get(0) ?: return
                val isBaseline = taskData[1] == DetektPlugin.BASELINE_KEYWORD
                val requiredVariant = taskData[2]

                if (isBaseline) {
                    detektCreateBaselineTask(extension, infrastructureRootProject, detektTaskName) {
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
                    detektTask(extension, infrastructureRootProject, detektTaskName) {
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
    }

    private fun Project.configureDetektDiffTask(extension: RedmadrobotExtension, infrastructureRootProject: Project) {
        val detektDiffOptions = extension.detekt.detektDiffOptions.orNull
        if (detektDiffOptions != null && detektDiffOptions.baseBranch.isNotEmpty()) {
            val changedFilesFilter = CollectGitDiffFilesTask.FilterParams(
                changeTypes = listOf(CollectGitDiffFilesTask.ChangeType.ADD, CollectGitDiffFilesTask.ChangeType.MODIFY),
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

        val taskSuffix = DetektPlugin.BASELINE_KEYWORD
            .takeIf { T::class == DetektCreateBaselineTask::class }
            .orEmpty()

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

    private fun List<Project>.checkModulesContainDetekt(lazyMessage: (String) -> String) {
        val missingPluginModules = filterNot { subproject ->
            subproject.plugins.hasPlugin<io.gitlab.arturbosch.detekt.DetektPlugin>()
        }

        check(missingPluginModules.isEmpty()) {
            val modulesName = missingPluginModules.joinToString(", ") { project -> "\"${project.name}\"" }
            lazyMessage.invoke(modulesName)
        }
    }
}
