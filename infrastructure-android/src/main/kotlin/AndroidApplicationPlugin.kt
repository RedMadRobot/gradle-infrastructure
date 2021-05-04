package com.redmadrobot.build

import com.android.build.gradle.AppExtension
import com.redmadrobot.build.extension.RedmadrobotExtension
import com.redmadrobot.build.internal.android
import com.redmadrobot.build.internal.configureKotlinDependencies
import org.gradle.api.Project

public class AndroidApplicationPlugin : BaseAndroidPlugin() {

    override fun Project.configure() {
        applyBaseAndroidPlugin("com.android.application")

        configureKotlinDependencies(redmadrobotExtension.kotlinVersion, "implementation")
        configureApp(redmadrobotExtension)
    }
}

private fun Project.configureApp(extension: RedmadrobotExtension) = android<AppExtension> {
    defaultConfig {
        // Keep only 'ru' resources
        resConfig("ru")

        // Collect proguard rules from 'proguard' dir
        setProguardFiles(
            rootProject.fileTree("proguard").files +
                getDefaultProguardFile("proguard-android-optimize.txt"),
        )

        buildConfigField("boolean", VAR_LOCK_ORIENTATION, "true")
        buildConfigField("boolean", VAR_CRASH_REPORTS_ENABLED, "true")
    }

    finalizeQaBuildType()
    buildTypes {
        getByName(BUILD_TYPE_DEBUG) {
            applicationIdSuffix = ".$BUILD_TYPE_DEBUG"
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

        register(BUILD_TYPE_QA) {
            initWith(release)
            applicationIdSuffix = ".$BUILD_TYPE_QA"
            isDebuggable = true
            matchingFallbacks += BUILD_TYPE_RELEASE
            signingConfig = signingConfigs.findByName(BUILD_TYPE_DEBUG)
        }
    }

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

private const val VAR_LOCK_ORIENTATION = "LOCK_ORIENTATION"
private const val VAR_CRASH_REPORTS_ENABLED = "CRASH_REPORTS_ENABLED"
