@file:Suppress("UnstableApiUsage") // We want to use new APIs

package com.redmadrobot.build.android

import com.redmadrobot.build.InfrastructurePlugin
import com.redmadrobot.build.StaticAnalyzerSpec
import com.redmadrobot.build.android.internal.*
import com.redmadrobot.build.internal.InternalGradleInfrastructureApi
import com.redmadrobot.build.internal.addRepositoriesIfNeed
import com.redmadrobot.build.kotlin.internal.configureKotlin
import com.redmadrobot.build.kotlin.internal.setTestOptions
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.TaskProvider
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getPlugin

/**
 * Base plugin with common configurations for both application and library modules.
 * @see AndroidLibraryPlugin
 * @see AndroidApplicationPlugin
 */
public abstract class BaseAndroidPlugin internal constructor() : InfrastructurePlugin() {

    @InternalGradleInfrastructureApi
    protected val configPlugin: AndroidConfigPlugin
        get() = project.plugins.getPlugin(AndroidConfigPlugin::class)

    /** Should be called from [configure] in implementation. */
    @InternalGradleInfrastructureApi
    protected fun Project.applyBaseAndroidPlugin(pluginId: String) {
        val configPlugin = plugins.apply(AndroidConfigPlugin::class)
        apply {
            plugin(pluginId)
            plugin("kotlin-android")

            // Apply fix for Android caching problems
            // See https://github.com/gradle/android-cache-fix-gradle-plugin
            plugin("org.gradle.android.cache-fix")
        }

        configureKotlin(configPlugin.jvmTarget)
        configureAndroid()
        androidComponents {
            finalizeDsl { extension ->
                extension.applyAndroidOptions(
                    options = configPlugin.androidOptions,
                    jvmTarget = configPlugin.jvmTarget,
                    staticAnalyzerSpec = configPlugin.staticAnalyzerSpec,
                )
                filterTestTaskDependencies(configPlugin.androidOptions)
            }
        }

        configureRepositories()
    }
}

private fun Project.configureAndroid() = android {
    // Set NDK version from env variable if exists
    val requestedNdkVersion = System.getenv("ANDROID_NDK_VERSION")
    if (requestedNdkVersion != null) ndkVersion = requestedNdkVersion

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
    jvmTarget: Provider<JavaVersion>,
    staticAnalyzerSpec: StaticAnalyzerSpec,
) {
    setCompileSdkVersion(options.compileSdk.get())
    options.buildToolsVersion.ifPresent { buildToolsVersion = it }

    defaultConfig {
        minSdk = options.minSdk.get()
    }

    compileOptions {
        sourceCompatibility = jvmTarget.get()
        targetCompatibility = jvmTarget.get()
    }

    testOptions {
        unitTests.all { it.setTestOptions(options.test) }
    }

    lint {
        lintConfig = staticAnalyzerSpec.configsDir.file("lint/lint.xml").get().asFile
        baseline = staticAnalyzerSpec.configsDir.file("lint/lint-baseline.xml").get().asFile
    }
}

/** Universal function to set compile SDK even if it is preview version. */
private fun CommonExtension.setCompileSdkVersion(version: String) {
    val intVersion = version.toIntOrNull()
    if (intVersion != null) {
        compileSdk = intVersion
    } else {
        compileSdkPreview = version
    }
}

/** Filter unit tests to be run with 'test' task. */
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
