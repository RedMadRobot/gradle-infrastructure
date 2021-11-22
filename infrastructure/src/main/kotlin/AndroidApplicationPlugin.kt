package com.redmadrobot.build

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import com.redmadrobot.build.android.AndroidApplicationPlugin as NewAndroidApplicationPlugin

/** @see NewAndroidApplicationPlugin */
@Deprecated("Use plugin 'com.redmadrobot.application' and 'com.redmadrobot.android-config' instead.")
public class AndroidApplicationPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        target.logger.warn(
            """
            Plugin 'redmadrobot.application' is deprecated and will be removed soon.
            You should apply plugin 'com.redmadrobot.application' instead.
            """.trimIndent(),
        )
        target.apply<NewAndroidApplicationPlugin>()
    }
}
