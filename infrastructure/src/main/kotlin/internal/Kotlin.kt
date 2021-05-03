package com.redmadrobot.build.internal

import com.redmadrobot.build.InfrastructurePlugin
import com.redmadrobot.build.extension.isRunningOnCi
import com.redmadrobot.build.kotlinCompile
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.kotlin

public fun InfrastructurePlugin.configureKotlin() {
    val warningsAsErrors = project.getWarningsAsErrorsProperty()
    project.kotlinCompile {
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

public fun InfrastructurePlugin.configureKotlinDependencies(kotlinVersion: String, configuration: String = api) {
    project.dependencies {
        configuration(kotlin("stdlib-jdk8", version = kotlinVersion))
    }
}
