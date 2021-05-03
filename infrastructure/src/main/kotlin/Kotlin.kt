package com.redmadrobot.build

import com.redmadrobot.build.extension.TestOptions
import com.redmadrobot.build.extension.isRunningOnCi
import com.redmadrobot.build.internal.findBooleanProperty
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.kotlin

internal fun Project.configureKotlin() {
    val warningsAsErrors = getWarningsAsErrorsProperty()
    kotlinCompile {
        kotlinOptions {
            jvmTarget = "1.8"
            allWarningsAsErrors = warningsAsErrors
            freeCompilerArgs += "-Xopt-in=kotlin.RequiresOptIn"
        }
    }
}

/** Use property value if it exists or fallback to true if running on CI. */
private fun Project.getWarningsAsErrorsProperty(): Boolean {
    return findBooleanProperty("warningsAsErrors") ?: isRunningOnCi
}

internal fun Project.configureKotlinDependencies(kotlinVersion: String, configuration: String = "api") {
    dependencies {
        configuration(kotlin("stdlib-jdk8", version = kotlinVersion))
    }
}

internal fun Project.configureKotlinTestDependencies(kotlinVersion: String, testOptions: TestOptions) {
    dependencies {
        val kotlinJunitModule = if (testOptions.useJunitPlatform) {
            "test-junit5"
        } else {
            "test-junit"
        }

        "testImplementation"(kotlin("test", version = kotlinVersion))
        "testImplementation"(kotlin(kotlinJunitModule, version = kotlinVersion))
    }
}
