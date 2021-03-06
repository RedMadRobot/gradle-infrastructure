package com.redmadrobot.build

import com.redmadrobot.build.extension.RedmadrobotExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.create

public class RootProjectPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        check(target.rootProject == target) { "This plugin can be applied only to the root project." }

        target.configureExtension()
    }

    private fun Project.configureExtension() {
        val rmr = extensions.create<RedmadrobotExtension>(RedmadrobotExtension.NAME, objects)
        rmr.configsDir.convention(layout.projectDirectory.dir(RedmadrobotExtension.DEFAULT_CONFIGS_DIR))
        rmr.reportsDir.convention(layout.buildDirectory.dir(RedmadrobotExtension.DEFAULT_REPORTS_DIR))
    }
}

internal fun Project.requireRootPlugin() {
    check(rootProject.plugins.hasPlugin("redmadrobot.root-project")) {
        """
        Plugin 'redmadrobot.root-project' not found. You should apply it to your root project:

        plugins {
            id("redmadrobot.root-project") version "[LATEST_VERSION_HERE]"
        }
        """.trimIndent()
    }
}
