package com.redmadrobot.build.kotlin.internal

import com.redmadrobot.build.InfrastructurePlugin
import com.redmadrobot.build.dsl.isRunningOnCi
import com.redmadrobot.build.internal.InternalGradleInfrastructureApi
import com.redmadrobot.build.internal.findBooleanProperty
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.*
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

@InternalGradleInfrastructureApi
public fun InfrastructurePlugin.configureKotlin(jvmTargetProperty: Provider<JavaVersion>) {
    val warningsAsErrors = project.getWarningsAsErrorsProperty()
    project.kotlinCompile {
        compilerOptions {
            jvmTarget = JvmTarget.fromTarget(jvmTargetProperty.get().toString())
            allWarningsAsErrors = warningsAsErrors
            freeCompilerArgs.add("-opt-in=kotlin.RequiresOptIn")
        }
    }
}

/** Use property value if it exists or fallback to true if running on CI. */
@OptIn(InternalGradleInfrastructureApi::class)
private fun Project.getWarningsAsErrorsProperty(): Boolean {
    return findBooleanProperty("warningsAsErrors") ?: isRunningOnCi
}
