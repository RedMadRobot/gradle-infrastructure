package com.redmadrobot.build

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import com.redmadrobot.build.android.AndroidLibraryPlugin as NewAndroidLibraryPlugin

/** @see NewAndroidLibraryPlugin */
@Deprecated("Use plugin 'com.redmadrobot.android-library' and 'com.redmadrobot.android-config' instead.")
public class AndroidLibraryPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        println(
            """
            WARNING: Plugin 'redmadrobot.android-library' is deprecated and will be removed soon.
            You should apply plugin 'com.redmadrobot.android-library' instead.
            """.trimIndent(),
        )
        target.apply<NewAndroidLibraryPlugin>()
    }
}
