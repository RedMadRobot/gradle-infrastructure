package com.redmadrobot.build

import com.android.build.gradle.BaseExtension
import com.redmadrobot.build.extension.redmadrobot
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.TaskProvider
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.repositories

abstract class BaseAndroidPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        rootProject.apply<RootProjectPlugin>()
        apply(plugin = "kotlin-android")

        configureKotlin()
        configureAndroid()
        configureRepositories()
    }
}

private fun Project.configureAndroid() = android<BaseExtension> {
    val androidSettings = redmadrobot.android
    compileSdkVersion(androidSettings.targetSdk)
    defaultConfig {
        minSdkVersion(androidSettings.minSdk)
        targetSdkVersion(androidSettings.targetSdk)
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

    // Add kotlin folder to all source sets
    for (sourceSet in sourceSets) {
        sourceSet.java.srcDirs("src/${sourceSet.name}/kotlin")
    }

    afterEvaluate {
        // Keep only release unit tests to reduce tests execution time
        tasks.named("test") {
            setDependsOn(dependsOn.filter { it !is TaskProvider<*> || it.name.endsWith("ReleaseUnitTest") })
        }
    }
}

private fun Project.configureRepositories() {
    repositories {
        jcenter()
        google()
    }
}
