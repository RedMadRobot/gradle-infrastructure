package com.redmadrobot.build

import com.redmadrobot.build.android.AndroidConfigPlugin
import com.redmadrobot.build.kotlin.KotlinConfigPlugin
import com.redmadrobot.build.publish.PublishConfigPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.plugin

@Deprecated("Use config plugins instead.")
public class RootProjectPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        println(
            """
            WARNING: Plugin 'redmadrobot.root-project' is deprecated and will be removed soon.
            You should apply plugins 'detekt', 'kotlin-config', 'publish-config' and 'android-config' instead.
            Please, refer to release notes to learn new plugins' structure:
            https://github.com/RedMadRobot/gradle-infrastructure/releases/tag/v0.13
            """.trimIndent()
        )
        target.apply {
            plugin<PublishConfigPlugin>()
            plugin<KotlinConfigPlugin>()
            plugin<AndroidConfigPlugin>()
        }
    }
}
