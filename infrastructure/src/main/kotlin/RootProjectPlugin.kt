package com.redmadrobot.build

import com.redmadrobot.build.extension.RedmadrobotExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.create

public class RootProjectPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        check(target.rootProject == target) { "This plugin can be applied only to the root project." }

        target.configureExtension()
        // TODO: Remove after Kotlin 1.5.10 release
        //  https://youtrack.jetbrains.com/issue/KT-46368
        target.apply(plugin = "dev.zacsweers.kgp-150-leak-patcher")
    }

    private fun Project.configureExtension() {
        val rmr = extensions.create<RedmadrobotExtension>(RedmadrobotExtension.NAME, objects)
        rmr.configsDir.convention(layout.projectDirectory.dir(RedmadrobotExtension.DEFAULT_CONFIGS_DIR))
        rmr.reportsDir.convention(layout.buildDirectory.dir(RedmadrobotExtension.DEFAULT_REPORTS_DIR))
    }
}
