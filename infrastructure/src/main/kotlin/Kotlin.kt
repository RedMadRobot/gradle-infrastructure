package com.redmadrobot.build

import com.redmadrobot.build.extension.TestOptions
import com.redmadrobot.build.extension.isRunningOnCi
import com.redmadrobot.build.extension.redmadrobotExtension
import com.redmadrobot.build.internal.findBooleanProperty
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.kotlin
import org.gradle.kotlin.dsl.withType

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

internal fun Project.configureKotlinTest() {
    tasks.withType<Test>().configureEach {
        setTestOptions(redmadrobotExtension.test)
    }
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
