package com.redmadrobot.build

import com.redmadrobot.build.extension.RedmadrobotExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.create

class RootProjectPlugin : Plugin<Project> {

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
