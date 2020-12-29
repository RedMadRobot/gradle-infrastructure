package com.redmadrobot.build

import com.android.build.gradle.AppExtension
import com.redmadrobot.build.extension.redmadrobotExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply

public class AndroidApplicationPlugin : BaseAndroidPlugin() {

    override fun apply(target: Project) {
        with(target) {
            apply(plugin = "com.android.application")
            super.apply(target)

            configureKotlinDependencies("implementation")
            configureApp()
        }
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

    val extension = redmadrobotExtension
    lintOptions {
        isCheckDependencies = true
        isAbortOnError = true
        isWarningsAsErrors = true
        xmlOutput = extension.reportsDir.file("lint-results.xml").get().asFile
        htmlOutput = extension.reportsDir.file("lint-results.html").get().asFile
        lintConfig = extension.configsDir.file("lint/lint.xml").get().asFile
        baselineFile = extension.configsDir.file("lint/lint-baseline.xml").get().asFile
    }
}

// Constants

public const val BUILD_TYPE_DEBUG: String = "debug"
public const val BUILD_TYPE_STAGING: String = "staging"
public const val BUILD_TYPE_RELEASE: String = "release"

private const val VAR_LOCK_ORIENTATION = "LOCK_ORIENTATION"
private const val VAR_CRASH_REPORTS_ENABLED = "CRASH_REPORTS_ENABLED"
