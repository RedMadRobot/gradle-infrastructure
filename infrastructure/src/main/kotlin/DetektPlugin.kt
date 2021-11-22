package com.redmadrobot.build

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import com.redmadrobot.build.detekt.DetektPlugin as NewDetektPlugin

/** @see NewDetektPlugin */
@Deprecated("Use plugin 'com.redmadrobot.detekt' instead.")
public class DetektPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        println(
            """
            WARNING: Plugin 'redmadrobot.detekt' is deprecated and will be removed soon.
            You should apply plugin 'com.redmadrobot.detekt' instead.
            """.trimIndent(),
        )
        target.apply<NewDetektPlugin>()
    }
}
