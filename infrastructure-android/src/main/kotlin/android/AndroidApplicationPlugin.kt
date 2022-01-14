@file:Suppress("UnstableApiUsage") // We want to use new APIs

package com.redmadrobot.build.android

import com.android.build.api.dsl.ApplicationExtension
import com.redmadrobot.build.android.internal.android
import com.redmadrobot.build.android.internal.androidFinalizeDsl
import com.redmadrobot.build.dsl.BUILD_TYPE_DEBUG
import com.redmadrobot.build.dsl.BUILD_TYPE_QA
import com.redmadrobot.build.dsl.BUILD_TYPE_RELEASE
import com.redmadrobot.build.dsl.finalizeQaBuildType
import org.gradle.api.Project

/**
 * Plugin that applies default configurations for Android application project.
 * Should be applied in place of `com.android.application`.
 *
 * Tied to `com.redmadrobot.application` plugin ID.
 */
public class AndroidApplicationPlugin : BaseAndroidPlugin() {

    override fun Project.configure() {
        applyBaseAndroidPlugin("com.android.application")

        configureApp()
        finalizeApp(configPlugin.androidOptions)
    }
}

private fun Project.configureApp() = android<ApplicationExtension> {
    defaultConfig {
        // Keep only 'ru' resources by default
        resourceConfigurations.add("ru")

        // Collect proguard rules from 'proguard' dir
        setProguardFiles(
            fileTree("proguard").files.filter { file -> file.extension == "pro" } +
                getDefaultProguardFile("proguard-android-optimize.txt"),
        )

        buildConfigField("boolean", VAR_LOCK_ORIENTATION, "true")
        buildConfigField("boolean", VAR_CRASH_REPORTS_ENABLED, "true")
    }

    finalizeQaBuildType()
    buildTypes {
        debug {
            applicationIdSuffix = ".$BUILD_TYPE_DEBUG"
            isDebuggable = true
            isMinifyEnabled = false
            isShrinkResources = false

            buildConfigField("boolean", VAR_LOCK_ORIENTATION, "false")
            buildConfigField("boolean", VAR_CRASH_REPORTS_ENABLED, "false")
        }

        release {
            applicationIdSuffix = ""
            isDebuggable = false
            isMinifyEnabled = true
            isShrinkResources = true
        }

        register(BUILD_TYPE_QA) {
            initWith(getByName(BUILD_TYPE_RELEASE))
            applicationIdSuffix = ".$BUILD_TYPE_QA"
            isDebuggable = true
            matchingFallbacks += listOf(BUILD_TYPE_DEBUG, BUILD_TYPE_RELEASE)
            signingConfig = signingConfigs.findByName(BUILD_TYPE_DEBUG)
        }
    }
}

private fun Project.finalizeApp(
    androidOptions: AndroidOptions,
) = androidFinalizeDsl<ApplicationExtension> {
    defaultConfig {
        targetSdk = androidOptions.targetSdk.get()
    }
}

// Constants

private const val VAR_LOCK_ORIENTATION = "LOCK_ORIENTATION"
private const val VAR_CRASH_REPORTS_ENABLED = "CRASH_REPORTS_ENABLED"
