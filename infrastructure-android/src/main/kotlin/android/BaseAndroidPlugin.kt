package com.redmadrobot.build.android

import com.android.build.gradle.BaseExtension
import com.redmadrobot.build.InfrastructurePlugin
import com.redmadrobot.build.android.internal.android
import com.redmadrobot.build.android.internal.test
import com.redmadrobot.build.kotlin.internal.configureKotlin
import com.redmadrobot.build.kotlin.internal.setTestOptions
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.provider.Property
import org.gradle.api.tasks.TaskProvider
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.repositories
import java.io.File

/**
 * Base plugin with common configurations for both application and library modules.
 * @see AndroidLibraryPlugin
 * @see AndroidApplicationPlugin
 */
public abstract class BaseAndroidPlugin : InfrastructurePlugin() {

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
        configureAndroid(configPlugin.androidOptions, configPlugin.jvmTarget)
        configureRepositories()
    }
}

private fun Project.configureAndroid(
    options: AndroidOptions,
    jvmTarget: Property<JavaVersion>,
) = android<BaseExtension> {
    compileSdkVersion(options.compileSdk.get())
    options.buildToolsVersion.orNull?.let(::buildToolsVersion)

    defaultConfig {
        minSdkVersion(options.minSdk.get())
        targetSdkVersion(options.targetSdk.get())
    }

    // Set NDK version from env variable if exists
    val requestedNdkVersion = System.getenv("ANDROID_NDK_VERSION")
    if (requestedNdkVersion != null) ndkVersion = requestedNdkVersion

    compileOptions {
        sourceCompatibility = jvmTarget.get()
        targetCompatibility = jvmTarget.get()
    }

    @Suppress("UnstableApiUsage")
    with(buildFeatures) {
        aidl = false
        renderScript = false
        shaders = false
    }

    afterEvaluate {
        // Add kotlin folder to all source sets
        // Do it after evaluate because there can be added build types
        for (sourceSet in sourceSets) {
            val javaSrcDirs = sourceSet.java.srcDirs.map(File::toString)
            val kotlinSrcDirs = javaSrcDirs.map { dir -> dir.replace("/java", "/kotlin") }
            sourceSet.java.srcDirs(javaSrcDirs + kotlinSrcDirs)
        }

        // Filter unit tests to be run with 'test' task
        tasks.named("test") {
            val testTasksFilter = options.testTasksFilter.get()
            setDependsOn(dependsOn.filter { it !is TaskProvider<*> || testTasksFilter(it) })
        }
    }

    testOptions {
        unitTests.all { it.setTestOptions(options.test) }
    }
}

private fun Project.configureRepositories() {
    repositories {
        mavenCentral()
        google()
    }
}
