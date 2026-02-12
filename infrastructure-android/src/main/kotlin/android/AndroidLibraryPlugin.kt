@file:Suppress("UnstableApiUsage") // We want to use new APIs

package com.redmadrobot.build.android

import com.android.build.api.dsl.LibraryExtension
import com.android.build.api.variant.LibraryAndroidComponentsExtension
import com.redmadrobot.build.StaticAnalyzerSpec
import com.redmadrobot.build.android.internal.android
import com.redmadrobot.build.android.internal.androidComponents
import com.redmadrobot.build.android.internal.test
import com.redmadrobot.build.dsl.collectProguardFiles
import com.redmadrobot.build.internal.InternalGradleInfrastructureApi
import com.redmadrobot.build.kotlin.internal.setTestOptions
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.kotlinExtension

/**
 * Plugin that applies default configurations for Android library project.
 * Should be applied in place of `com.android.library`.
 *
 * Tied to `com.redmadrobot.android-library` plugin ID.
 */
public class AndroidLibraryPlugin : BaseAndroidPlugin("com.android.library") {

    override fun Project.configure(configPlugin: AndroidConfigPlugin) {
        android<LibraryExtension> {
            defaultConfig {
                // Add all files from 'proguard' dir
                consumerProguardFiles.addAll(collectProguardFiles())
            }

            buildFeatures {
                shaders = false
                resValues = false
            }
            androidResources.enable = false
        }

        androidComponents<LibraryAndroidComponentsExtension> {
            finalizeDsl {
                it.applyCommonAndroidOptions(configPlugin.androidOptions)
                it.finalizeLibrary(configPlugin.androidOptions, configPlugin.staticAnalyzerSpec)
            }
        }

        filterTestTaskDependencies(configPlugin.androidOptions)

        // Enable Explicit API mode for libraries by default
        kotlinExtension.explicitApi()
    }
}

@OptIn(InternalGradleInfrastructureApi::class)
private fun LibraryExtension.finalizeLibrary(
    androidOptions: AndroidOptions,
    staticAnalyzerSpec: StaticAnalyzerSpec,
) {
    testOptions {
        unitTests.all { it.setTestOptions(androidOptions.test) }
    }

    lint {
        checkDependencies = true
        abortOnError = true
        warningsAsErrors = true
        lintConfig = staticAnalyzerSpec.configsDir.file("lint/lint.xml").get().asFile
        baseline = staticAnalyzerSpec.configsDir.file("lint/lint-baseline.xml").get().asFile
    }
}
