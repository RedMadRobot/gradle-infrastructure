package com.redmadrobot.build

import com.redmadrobot.build.extension.TestOptions
import com.redmadrobot.build.extension.redmadrobot
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.kotlin

internal fun Project.configureKotlin() {
    kotlinCompile {
        kotlinOptions {
            jvmTarget = "1.8"
            allWarningsAsErrors = findProperty("warningsAsErrors") == true
            freeCompilerArgs += "-Xopt-in=kotlin.RequiresOptIn"
        }
    }
}

internal fun Project.configureKotlinTest() {
    kotlinTest { configure(redmadrobot.testOptions) }
}

internal fun Project.configureKotlinDependencies(configuration: String = "api") {
    dependencies {
        val kotlinVersion = redmadrobot.kotlinVersion
        configuration(kotlin("stdlib-jdk8", version = kotlinVersion))
    }
}

internal fun Project.configureKotlinTestDependencies(testOptions: TestOptions) {
    dependencies {
        val kotlinVersion = redmadrobot.kotlinVersion

        if (testOptions.useJunitPlatform) {
            "testImplementation"(kotlin("test-junit5", version = kotlinVersion))
        } else {
            "testImplementation"(kotlin("test", version = kotlinVersion))
        }
    }
}
