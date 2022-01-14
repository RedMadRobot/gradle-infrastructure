package com.redmadrobot.build.android

import com.android.build.api.dsl.CommonExtension
import com.redmadrobot.build.InfrastructurePlugin
import com.redmadrobot.build.StaticAnalyzerSpec
import com.redmadrobot.build.android.internal.android
import com.redmadrobot.build.android.internal.test
import com.redmadrobot.build.kotlin.internal.configureKotlin
import com.redmadrobot.build.kotlin.internal.setTestOptions
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.provider.Property
import org.gradle.api.tasks.TaskProvider
import org.gradle.kotlin.dsl.apply
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
        configureAndroid(configPlugin.androidOptions, configPlugin.jvmTarget, configPlugin.staticAnalyzerSpec)
        configureRepositories()
    }
}

@Suppress("UnstableApiUsage")
private fun Project.configureAndroid(
    options: AndroidOptions,
    jvmTarget: Property<JavaVersion>,
    staticAnalyzerSpec: StaticAnalyzerSpec,
) = android<CommonExtension<*, *, *, *>> {
    compileSdkVersion(options.compileSdk.get())
    options.buildToolsVersion.orNull?.let { buildToolsVersion = it }

    defaultConfig {
        minSdk = options.minSdk.get()
    }

    // Set NDK version from env variable if exists
    val requestedNdkVersion = System.getenv("ANDROID_NDK_VERSION")
    if (requestedNdkVersion != null) ndkVersion = requestedNdkVersion

    compileOptions {
        sourceCompatibility = jvmTarget.get()
        targetCompatibility = jvmTarget.get()
    }

    buildFeatures {
        aidl = false
        renderScript = false
        shaders = false
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
        isCheckDependencies = true
        isAbortOnError = true
        isWarningsAsErrors = true
        xmlOutput = staticAnalyzerSpec.reportsDir.file("lint-results.xml").get().asFile
        htmlOutput = staticAnalyzerSpec.reportsDir.file("lint-results.html").get().asFile
        lintConfig = staticAnalyzerSpec.configsDir.file("lint/lint.xml").get().asFile
        baselineFile = staticAnalyzerSpec.configsDir.file("lint/lint-baseline.xml").get().asFile
    }
}

private fun Project.configureRepositories() {
    repositories {
        mavenCentral()
        google()
    }
}
