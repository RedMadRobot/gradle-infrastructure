@file:Suppress("UnstableApiUsage") // We want to use new APIs

package com.redmadrobot.build.android

import com.redmadrobot.build.StaticAnalyzerSpec
import com.redmadrobot.build.android.internal.*
import com.redmadrobot.build.internal.InternalGradleInfrastructureApi
import com.redmadrobot.build.internal.addRepositoriesIfNeed
import com.redmadrobot.build.kotlin.internal.configureKotlin
import com.redmadrobot.build.kotlin.internal.setTestOptions
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.TaskProvider
import org.gradle.kotlin.dsl.apply

/**
 * Base plugin with common configurations for both application and library modules.
 * @see AndroidLibraryPlugin
 * @see AndroidApplicationPlugin
 */
public abstract class BaseAndroidPlugin internal constructor(
    private val androidPluginId: String,
) : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            val configPlugin = plugins.apply(AndroidConfigPlugin::class)
            applyBaseAndroidPlugin(androidPluginId, configPlugin)
            configure(configPlugin)
        }
    }

    internal abstract fun Project.configure(configPlugin: AndroidConfigPlugin)

    @OptIn(InternalGradleInfrastructureApi::class)
    private fun Project.applyBaseAndroidPlugin(pluginId: String, configPlugin: AndroidConfigPlugin) {
        apply {
            plugin(pluginId)
            plugin("kotlin-android")

            // Apply fix for Android caching problems
            // See https://github.com/gradle/android-cache-fix-gradle-plugin
            plugin("org.gradle.android.cache-fix")
        }

        configureKotlin()
        configureAndroid()
        androidComponents {
            finalizeDsl { extension ->
                extension.applyAndroidOptions(
                    options = configPlugin.androidOptions,
                    staticAnalyzerSpec = configPlugin.staticAnalyzerSpec,
                )
                filterTestTaskDependencies(configPlugin.androidOptions)
            }
        }

        configureRepositories()
    }
}

private fun Project.configureAndroid() = android {
    buildFeatures {
        shaders = false
    }

    lint {
        checkDependencies = true
        abortOnError = true
        warningsAsErrors = true
    }
}

@OptIn(InternalGradleInfrastructureApi::class)
private fun CommonExtension.applyAndroidOptions(
    options: AndroidOptions,
    staticAnalyzerSpec: StaticAnalyzerSpec,
) {
    options.compileSdk.ifPresent { setCompileSdkVersion(it) }
    options.buildToolsVersion.ifPresent { buildToolsVersion = it }
    options.ndkVersion.ifPresent { ndkVersion = it }
    options.minSdk.ifPresent { defaultConfig.minSdk = it }

    testOptions {
        unitTests.all { it.setTestOptions(options.test) }
    }

    lint {
        lintConfig = staticAnalyzerSpec.configsDir.file("lint/lint.xml").get().asFile
        baseline = staticAnalyzerSpec.configsDir.file("lint/lint-baseline.xml").get().asFile
    }
}

/** Universal function to set compile SDK even if it is a preview version. */
private fun CommonExtension.setCompileSdkVersion(version: String) {
    val intVersion = version.toIntOrNull()
    if (intVersion != null) {
        compileSdk = intVersion
    } else {
        compileSdkPreview = version
    }
}

/** Filter unit tests to be run with the 'test' task. */
private fun Project.filterTestTaskDependencies(options: AndroidOptions) {
    afterEvaluate {
        tasks.named("test") {
            val testTasksFilter = options.testTasksFilter.get()
            setDependsOn(dependsOn.filter { it !is TaskProvider<*> || testTasksFilter(it) })
        }
    }
}

@OptIn(InternalGradleInfrastructureApi::class)
private fun Project.configureRepositories() {
    addRepositoriesIfNeed {
        mavenCentral()
        google()
    }
}
