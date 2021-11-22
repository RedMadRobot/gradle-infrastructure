package com.redmadrobot.build

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import com.redmadrobot.build.kotlin.KotlinLibraryPlugin as NewKotlinLibraryPlugin

/** @see NewKotlinLibraryPlugin */
@Deprecated("Use plugin 'com.redmadrobot.kotlin-library' and 'com.redmadrobot.kotlin-config' instead.")
public class KotlinLibraryPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        println(
            """
            WARNING: Plugin 'redmadrobot.kotlin-library' is deprecated and will be removed soon.
            You should apply plugin 'com.redmadrobot.kotlin-library' instead.
            """.trimIndent(),
        )
        target.apply<NewKotlinLibraryPlugin>()
    }
}
