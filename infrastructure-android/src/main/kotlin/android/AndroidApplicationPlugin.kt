@file:Suppress("UnstableApiUsage") // We want to use new APIs

package com.redmadrobot.build.android

import com.android.build.api.artifact.SingleArtifact
import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.variant.ApplicationAndroidComponentsExtension
import com.android.build.api.variant.ApplicationVariant
import com.redmadrobot.build.StaticAnalyzerSpec
import com.redmadrobot.build.android.internal.android
import com.redmadrobot.build.android.internal.androidComponents
import com.redmadrobot.build.android.internal.ifPresent
import com.redmadrobot.build.android.internal.test
import com.redmadrobot.build.android.task.MakeDebuggableTask
import com.redmadrobot.build.dsl.*
import com.redmadrobot.build.internal.InternalGradleInfrastructureApi
import com.redmadrobot.build.kotlin.internal.setTestOptions
import org.gradle.api.Project
import org.gradle.api.tasks.TaskContainer
import org.gradle.kotlin.dsl.register

/**
 * Plugin that applies default configurations for Android application project.
 * Should be applied in place of `com.android.application`.
 *
 * Tied to `com.redmadrobot.application` plugin ID.
 */
public class AndroidApplicationPlugin : BaseAndroidPlugin("com.android.application") {

    override fun Project.configure(configPlugin: AndroidConfigPlugin) {
        configureApp()
        androidComponents<ApplicationAndroidComponentsExtension> {
            onVariants(selector().withBuildType(BUILD_TYPE_QA)) { it.makeDebuggable(tasks) }
            finalizeDsl {
                it.applyCommonAndroidOptions(configPlugin.androidOptions)
                it.finalizeApp(configPlugin.androidOptions, configPlugin.staticAnalyzerSpec)
            }
        }
        filterTestTaskDependencies(configPlugin.androidOptions)
    }
}

private fun Project.configureApp() = android<ApplicationExtension> {
    defaultConfig {
        // Collect proguard rules from 'proguard' dir
        proguardFiles.addAll(collectProguardFiles())
    }

    buildFeatures {
        shaders = false
    }

    finalizeQaBuildType()
    buildTypes {
        debug {
            applicationIdSuffix = ".$BUILD_TYPE_DEBUG"
            isDebuggable = true
            isMinifyEnabled = false
            isShrinkResources = false
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
            matchingFallbacks += listOf(BUILD_TYPE_DEBUG, BUILD_TYPE_RELEASE)
            signingConfig = signingConfigs.findByName(BUILD_TYPE_DEBUG)
        }
    }
}

// NOTE: Since AGP 7.2.0 debuggable builds can not be obfuscated by default, so we should
// set isDebuggable to `false` and manually set "debuggable" flag in manifest
// See: https://issuetracker.google.com/issues/238655204
private fun ApplicationVariant.makeDebuggable(tasks: TaskContainer) {
    val capitalizedName = name.replaceFirstChar { it.uppercaseChar() }
    val makeDebuggableTask = tasks.register<MakeDebuggableTask>("make${capitalizedName}Debuggable")

    artifacts.use(makeDebuggableTask)
        .wiredWithFiles(
            taskInput = { it.mergedManifest },
            taskOutput = { it.debuggableManifest },
        )
        .toTransform(SingleArtifact.MERGED_MANIFEST)
}

@OptIn(InternalGradleInfrastructureApi::class)
private fun ApplicationExtension.finalizeApp(
    androidOptions: AndroidOptions,
    staticAnalyzerSpec: StaticAnalyzerSpec,
) {
    androidOptions.targetSdk.ifPresent { defaultConfig.targetSdk = it }

    testOptions {
        unitTests.all { it.setTestOptions(androidOptions.test) }
    }

    lint {
        checkDependencies = true
        abortOnError = true
        warningsAsErrors = true
        lintConfig = staticAnalyzerSpec.configsDir.file("lint/lint.xml").get().asFile
        baseline = staticAnalyzerSpec.configsDir.file("lint/lint-baseline.xml").get().asFile
        xmlOutput = staticAnalyzerSpec.reportsDir.file("lint-results.xml").get().asFile
        htmlOutput = staticAnalyzerSpec.reportsDir.file("lint-results.html").get().asFile
    }

    buildTypes {
        if (buildFeatures.buildConfig == true) {
            qa {
                // We can not use isDebuggable = true here, so set DEBUG field ourselves.
                // See `makeDebuggable` for more information
                buildConfigField(type = "boolean", name = "DEBUG", value = "true")
            }
        }
    }
}
