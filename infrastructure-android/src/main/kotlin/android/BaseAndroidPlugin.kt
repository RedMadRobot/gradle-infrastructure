@file:Suppress("UnstableApiUsage") // We want to use new APIs

package com.redmadrobot.build.android

import com.android.build.api.dsl.CommonExtension
import com.redmadrobot.build.InfrastructurePlugin
import com.redmadrobot.build.StaticAnalyzerSpec
import com.redmadrobot.build.android.internal.android
import com.redmadrobot.build.android.internal.androidFinalizeDsl
import com.redmadrobot.build.android.internal.test
import com.redmadrobot.build.kotlin.internal.configureKotlin
import com.redmadrobot.build.kotlin.internal.setTestOptions
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.TaskProvider
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getPlugin
import org.gradle.kotlin.dsl.repositories

/**
 * Base plugin with common configurations for both application and library modules.
 * @see AndroidLibraryPlugin
 * @see AndroidApplicationPlugin
 */
public abstract class BaseAndroidPlugin : InfrastructurePlugin() {

    protected val configPlugin: AndroidConfigPlugin
        get() = project.plugins.getPlugin(AndroidConfigPlugin::class)

    /** Should be called from [configure] in implementation. */
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
        applyAndroidOptions(configPlugin.androidOptions, configPlugin.jvmTarget, configPlugin.staticAnalyzerSpec)
        configureRepositories()
    }
}

private fun Project.configureAndroid() = android<CommonExtension<*, *, *, *>> {
    // Compile SDK is configured in finalizeDsl block, so here we can specify any value
    // Without this line finalizeDsl will not be called at all.
    // TODO: Remove this hack, when the issue will be fixed
    //  https://issuetracker.google.com/issues/215407138
    compileSdk = -1

    // Set NDK version from env variable if exists
    val requestedNdkVersion = System.getenv("ANDROID_NDK_VERSION")
    if (requestedNdkVersion != null) ndkVersion = requestedNdkVersion

    buildFeatures {
        aidl = false
        renderScript = false
        shaders = false
    }

    lint {
        checkDependencies = true
        abortOnError = true
        warningsAsErrors = true
    }
}

private fun Project.applyAndroidOptions(
    options: AndroidOptions,
    jvmTarget: Provider<JavaVersion>,
    staticAnalyzerSpec: StaticAnalyzerSpec,
) = androidFinalizeDsl {
    setCompileSdkVersion(options.compileSdk.get())
    options.buildToolsVersion.orNull?.let { buildToolsVersion = it }

    defaultConfig {
        minSdk = options.minSdk.get()
    }

    compileOptions {
        sourceCompatibility = jvmTarget.get()
        targetCompatibility = jvmTarget.get()
    }

    afterEvaluate {
        // Filter unit tests to be run with 'test' task
        tasks.named("test") {
            val testTasksFilter = options.testTasksFilter.get()
            setDependsOn(dependsOn.filter { it !is TaskProvider<*> || testTasksFilter(it) })
        }
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
private fun CommonExtension<*, *, *, *>.setCompileSdkVersion(version: String) {
    val intVersion = version.toIntOrNull()
    if (intVersion != null) {
        compileSdk = intVersion
    } else {
        compileSdkPreview = version
    }
}

private fun Project.configureRepositories() {
    repositories {
        mavenCentral()
        google()
    }
}
