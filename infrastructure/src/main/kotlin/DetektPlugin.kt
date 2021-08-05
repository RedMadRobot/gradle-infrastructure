package com.redmadrobot.build

import com.android.build.gradle.AppExtension
import com.android.build.gradle.BaseExtension
import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.TestExtension
import com.android.build.gradle.internal.core.InternalBaseVariant
import com.redmadrobot.build.extension.RedmadrobotExtension
import com.redmadrobot.build.internal.detekt.CollectGitDiffFilesTask
import com.redmadrobot.build.internal.detekt.CollectGitDiffFilesTask.ChangeType
import com.redmadrobot.build.internal.detekt.CollectGitDiffFilesTask.FilterParams
import com.redmadrobot.build.internal.detektPlugins
import com.redmadrobot.build.internal.hasPlugin
import com.redmadrobot.build.internal.isRoot
import io.gitlab.arturbosch.detekt.Detekt
import org.gradle.api.Project
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.tasks.TaskProvider
import org.gradle.kotlin.dsl.*
import java.io.File

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
        configureDetektTasks(redmadrobotExtension)
    }
}

private fun Project.configureDependencies() {
    repositories {
        mavenCentral()
    }

    dependencies {
        detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:1.17.1")
    }
}

private fun Project.configureDetektTasks(extension: RedmadrobotExtension) {
    detektTask(extension, "detektFormat") {
        description = "Reformats whole code base."
        disableDefaultRuleSets = true
        autoCorrect = true
    }

    detektTask(extension, "detektAll") {
        description = "Runs over whole code base without the starting overhead for each module."
    }

    if (project.isRoot) {
        val variantRegex = Regex("^detekt([A-Z][a-z]+)All$")
        val startTask = gradle.startParameter.taskNames.find { it.contains(variantRegex) }
        if (startTask != null) {
            val requiredVariant = variantRegex.find(startTask)?.groups?.get(1)?.value.orEmpty()
            detektTask(extension, startTask) {
                checkAllModulesContainDetekt()

                val detektTaskProviders = subprojects.map { it.extractDetektTaskProvider(requiredVariant) }

                val allClasspath = detektTaskProviders.map { it.map(Detekt::classpath) }
                val allSources = detektTaskProviders.map { it.map(Detekt::getSource) }

                classpath.setFrom(allClasspath)
                setSource(allSources)
            }
        }
    }

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

        detektTask(extension, "detektDiff") {
            description = "Check out only changed files versus the ${detektDiffOptions.baseBranch} branch"
            setSource(rootProject.files(findChangedFiles.map { it.outputFiles.from }))
        }
    }
}

private inline fun Project.detektTask(
    extension: RedmadrobotExtension,
    name: String,
    crossinline configure: Detekt.() -> Unit,
): TaskProvider<Detekt> {
    return tasks.register<Detekt>(name) {
        parallel = true
        config.setFrom(extension.configsDir.file("detekt/detekt.yml"))
        baseline.set(extension.configsDir.getFileIfExists("detekt/baseline.xml"))
        setSource(rootProject.files(rootProject.projectDir))
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

private fun DirectoryProperty.getFileIfExists(path: String): File? {
    return file(path).get().asFile.takeIf { it.exists() }
}

private fun Project.checkAllModulesContainDetekt() {
    val missingPluginModules = subprojects.filterNot { subproject ->
        subproject.plugins.hasPlugin<io.gitlab.arturbosch.detekt.DetektPlugin>()
    }

    check(missingPluginModules.isEmpty()) {
        val modulesName = missingPluginModules.joinToString(
            prefix = "\"",
            separator = "\", \"",
            postfix = "\"",
            transform = Project::getName,
        )
        "Modules $modulesName don't contain \"detekt\" or \"redmadrobot.detekt\" plugin"
    }
}

@Suppress("DefaultLocale")
private fun Project.extractDetektTaskProvider(variantName: String): TaskProvider<Detekt> {
    val isSubprojectAndroid = plugins.hasPlugin("com.android.application") ||
        plugins.hasPlugin("com.android.library")

    val taskProvider = if (isSubprojectAndroid) {
        val baseExtensions = extensions.getByType<BaseExtension>()
        baseExtensions.checkVariantExist(variantName) { existVariants ->
            val candidates = existVariants.joinToString(
                separator = "', '",
                prefix = "'",
                postfix = "'",
                transform = { variant -> "detekt${variant.capitalize()}All" },
            )
            "Task detekt${variantName.capitalize()}All not found in project. Some candidates are: $candidates"
        }

        tasks.named<Detekt>("detekt$variantName")
    } else {
        tasks.named<Detekt>("detektMain")
    }

    return taskProvider.also { taskProvider.configure { isEnabled = false } }
}

private fun BaseExtension.checkVariantExist(variantName: String, lazyMessage: (List<String>) -> String) {
    val variants = when (this) {
        is AppExtension -> applicationVariants
        is LibraryExtension -> libraryVariants
        is TestExtension -> applicationVariants
        else -> null
    }
    val requiredVariant = variants?.find { it.name.equals(variantName, ignoreCase = true) }
    checkNotNull(requiredVariant) { lazyMessage.invoke(variants?.map(InternalBaseVariant::getName).orEmpty()) }
}
