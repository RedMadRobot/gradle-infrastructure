package com.redmadrobot.build

import com.android.build.gradle.LibraryExtension
import com.redmadrobot.build.internal.android
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.ExplicitApiMode
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

private const val ARG_EXPLICIT_API = "-Xexplicit-api"

public class AndroidLibraryPlugin : BaseAndroidPlugin() {

    override fun Project.configure() {
        applyBaseAndroidPlugin("com.android.library")

        android<LibraryExtension> {
            buildFeatures {
                buildConfig = false
                resValues = false
                androidResources = false
            }
        }

        configureKotlinDependencies(redmadrobotExtension.kotlinVersion)

        // Enable Explicit API by default
        if (kotlin.explicitApi == null) kotlin.explicitApi()
        afterEvaluate {
            configureExplicitApi(kotlin.explicitApi)
        }
    }
}

private fun Project.configureExplicitApi(mode: ExplicitApiMode?) {
    if (mode == null) return

    tasks.matching { it is KotlinCompile && !it.name.contains("test", ignoreCase = true) }
        .configureEach {
            val options = (this as KotlinCompile).kotlinOptions
            if (options.freeCompilerArgs.none { it.startsWith(ARG_EXPLICIT_API) }) {
                options.freeCompilerArgs += mode.toCompilerArg()
            }
        }
}
