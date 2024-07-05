@file:Suppress("UnstableApiUsage") // We want to use new APIs

package com.redmadrobot.build.android

import com.android.build.api.dsl.LibraryExtension
import com.redmadrobot.build.android.internal.android
import com.redmadrobot.build.android.internal.projectProguardFiles
import com.redmadrobot.build.internal.InternalGradleInfrastructureApi
import com.redmadrobot.build.kotlin.internal.kotlin
import org.gradle.api.Project

/**
 * Plugin that applies default configurations for Android library project.
 * Should be applied in place of `com.android.library`.
 *
 * Tied to `com.redmadrobot.android-library` plugin ID.
 */
public class AndroidLibraryPlugin : BaseAndroidPlugin() {

    @InternalGradleInfrastructureApi
    override fun Project.configure() {
        applyBaseAndroidPlugin("com.android.library")

        android<LibraryExtension> {
            defaultConfig {
                // Add all files from 'proguard' dir
                consumerProguardFiles.addAll(projectProguardFiles())
            }

            buildFeatures {
                buildConfig = false
                resValues = false
                androidResources = false
            }
        }

        // Enable Explicit API mode for libraries by default
        kotlin.explicitApi()
    }
}
