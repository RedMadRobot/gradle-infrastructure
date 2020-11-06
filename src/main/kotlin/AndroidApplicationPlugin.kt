package com.redmadrobot.build

import com.android.build.gradle.AppExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply

class AndroidApplicationPlugin : BaseAndroidPlugin() {

    override fun apply(target: Project) = with(target) {
        apply(plugin = "com.android.application")
        super.apply(target)

        configureKotlinDependencies("implementation")
        configureApp()
    }
}

private fun Project.configureApp() = android<AppExtension> {
    defaultConfig {
        // Keep only 'ru' resources
        resConfig("ru")

        // Collect proguard rules from 'proguard' dir
        setProguardFiles(
            rootProject.fileTree("proguard").files +
                getDefaultProguardFile("proguard-android-optimize.txt")
        )

        buildConfigField("boolean", VAR_LOCK_ORIENTATION, "true")
        buildConfigField("boolean", VAR_CRASH_REPORTS_ENABLED, "true")
    }

    buildTypes {
        getByName(BUILD_TYPE_DEBUG) {
            applicationIdSuffix = ".debug"
            isDebuggable = true
            isMinifyEnabled = false
            isShrinkResources = false

            buildConfigField("boolean", VAR_LOCK_ORIENTATION, "false")
            buildConfigField("boolean", VAR_CRASH_REPORTS_ENABLED, "false")
        }

        val release = getByName(BUILD_TYPE_RELEASE) {
            applicationIdSuffix = ""
            isDebuggable = false
            isMinifyEnabled = true
            isShrinkResources = true
        }

        register(BUILD_TYPE_STAGING) {
            initWith(release)
            applicationIdSuffix = ".staging"
            isDebuggable = true
            matchingFallbacks += BUILD_TYPE_RELEASE
            signingConfig = signingConfigs.findByName(BUILD_TYPE_DEBUG)
        }
    }
}

// Constants

private const val BUILD_TYPE_DEBUG: String = "debug"
private const val BUILD_TYPE_STAGING: String = "staging"
private const val BUILD_TYPE_RELEASE: String = "release"

private const val VAR_LOCK_ORIENTATION = "LOCK_ORIENTATION"
private const val VAR_CRASH_REPORTS_ENABLED = "CRASH_REPORTS_ENABLED"
