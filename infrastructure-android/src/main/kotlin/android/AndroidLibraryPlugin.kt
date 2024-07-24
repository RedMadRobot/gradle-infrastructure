@file:Suppress("UnstableApiUsage") // We want to use new APIs

package com.redmadrobot.build.android

import com.android.build.api.dsl.LibraryExtension
import com.redmadrobot.build.android.internal.android
import com.redmadrobot.build.dsl.collectProguardFiles
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
                resValues = false
                androidResources = false
            }
        }

        // Enable Explicit API mode for libraries by default
        kotlinExtension.explicitApi()
    }
}
