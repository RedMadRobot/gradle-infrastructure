package com.redmadrobot.build

import com.redmadrobot.build.extension.RedmadrobotExtension
import com.redmadrobot.build.extension.RedmadrobotExtensionImpl
import com.redmadrobot.build.extension.StaticAnalyzerSpec
import com.redmadrobot.build.internal.findInfrastructureRootProject
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.create

/**
 * This plugin adds extension [RedmadrobotExtension] to the project.
 * Can be applied only to the root project.
 *
 * Tied to `redmadrobot.root-project` plugin ID.
 *
 * @see RedmadrobotExtension
 */
public class RootProjectPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        val rootProject = target.findInfrastructureRootProject()
        if (rootProject != null) error("Root-project is already defined: $rootProject")

        target.configureExtension()
    }

    private fun Project.configureExtension() {
        extensions.create<RedmadrobotExtensionImpl>(RedmadrobotExtension.NAME).apply {
            configsDir.convention(layout.projectDirectory.dir(StaticAnalyzerSpec.DEFAULT_CONFIGS_DIR))
            reportsDir.convention(layout.buildDirectory.dir(StaticAnalyzerSpec.DEFAULT_REPORTS_DIR))
        }
    }
}
