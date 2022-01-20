package com.redmadrobot.build.kotlin.internal

import com.redmadrobot.build.InfrastructurePlugin
import com.redmadrobot.build.dsl.isRunningOnCi
import com.redmadrobot.build.dsl.kotlinCompile
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.provider.Provider

public fun InfrastructurePlugin.configureKotlin(jvmTargetProperty: Provider<JavaVersion>) {
    val warningsAsErrors = project.getWarningsAsErrorsProperty()
    project.kotlinCompile {
        kotlinOptions {
            jvmTarget = jvmTargetProperty.get().toString()
            allWarningsAsErrors = warningsAsErrors
            freeCompilerArgs += "-opt-in=kotlin.RequiresOptIn"
        }
    }
}

/** Use property value if it exists or fallback to true if running on CI. */
private fun Project.getWarningsAsErrorsProperty(): Boolean {
    return findBooleanProperty("warningsAsErrors") ?: isRunningOnCi
}
