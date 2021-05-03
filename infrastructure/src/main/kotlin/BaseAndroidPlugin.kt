package com.redmadrobot.build

import com.android.build.gradle.BaseExtension
import com.redmadrobot.build.extension.AndroidOptions
import com.redmadrobot.build.internal.android
import com.redmadrobot.build.internal.configureKotlin
import com.redmadrobot.build.internal.configureKotlinTestDependencies
import com.redmadrobot.build.internal.setTestOptions
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.tasks.TaskProvider
import org.gradle.kotlin.dsl.repositories
import java.io.File

public abstract class BaseAndroidPlugin : InfrastructurePlugin() {

    /** Should be called from [configure] in implementation. */
    protected fun Project.applyBaseAndroidPlugin(pluginId: String) {
        apply {
            plugin(pluginId)
            plugin("kotlin-android")
        }

        configureKotlin()
        configureAndroid(redmadrobotExtension.android)
        configureRepositories()
        configureKotlinTestDependencies(redmadrobotExtension.kotlinVersion, redmadrobotExtension.android.test)
    }
}

private fun Project.configureAndroid(options: AndroidOptions) = android<BaseExtension> {
    compileSdkVersion(options.targetSdk)
    defaultConfig {
        minSdkVersion(options.minSdk)
        targetSdkVersion(options.targetSdk)
    }

    // Set NDK version from env variable if exists
    val requestedNdkVersion = System.getenv("ANDROID_NDK_VERSION")
    if (requestedNdkVersion != null) ndkVersion = requestedNdkVersion

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

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
            val kotlinSrcDirs = javaSrcDirs.map { it.replace("/java", "/kotlin") }
            sourceSet.java.srcDirs(javaSrcDirs + kotlinSrcDirs)
        }

        // Keep only release unit tests to reduce tests execution time
        tasks.named("test") {
            setDependsOn(dependsOn.filter { it !is TaskProvider<*> || it.name.endsWith("ReleaseUnitTest") })
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
