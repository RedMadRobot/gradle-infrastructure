package com.redmadrobot.build

import com.redmadrobot.build.extension.TestOptions
import com.redmadrobot.build.extension.redmadrobotExtension
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
    kotlinTest { configure(redmadrobotExtension.test) }
}

internal fun Project.configureKotlinDependencies(configuration: String = "api") {
    dependencies {
        val kotlinVersion = redmadrobotExtension.kotlinVersion
        configuration(kotlin("stdlib-jdk8", version = kotlinVersion))
    }
}

internal fun Project.configureKotlinTestDependencies(testOptions: TestOptions) {
    dependencies {
        val kotlinVersion = redmadrobotExtension.kotlinVersion

        val kotlinJunitModule = if (testOptions.useJunitPlatform) {
            "test-junit5"
        } else {
            "test-junit"
        }

        "testImplementation"(kotlin("test", version = kotlinVersion))
        "testImplementation"(kotlin(kotlinJunitModule, version = kotlinVersion))
    }
}
