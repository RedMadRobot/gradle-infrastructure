package com.redmadrobot.build

import com.redmadrobot.build.android.AndroidConfigPlugin
import com.redmadrobot.build.kotlin.KotlinConfigPlugin
import com.redmadrobot.build.publish.PublishConfigPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.plugin

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
        target.apply {
            plugin<PublishConfigPlugin>()
            plugin<KotlinConfigPlugin>()
            plugin<AndroidConfigPlugin>()
        }
        println(
            """
            WARNING: Plugin 'redmadrobot.root-project' is deprecated and will be removed soon.
            You should apply plugins 'detekt', 'kotlin-config', 'publish-config' and 'android-config' instead.
            """.trimIndent()
        )
    }
}
