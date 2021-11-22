package com.redmadrobot.build

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import com.redmadrobot.build.publish.PublishPlugin as NewPublishPlugin

/** @see NewPublishPlugin */
@Deprecated("Use plugin 'com.redmadrobot.publish' and 'com.redmadrobot.publish-config' instead.")
public class PublishPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        println(
            """
            WARNING: Plugin 'redmadrobot.publish' is deprecated and will be removed soon.
            You should apply plugin 'com.redmadrobot.publish' instead.
            """.trimIndent(),
        )
        target.apply<NewPublishPlugin>()
    }
}
