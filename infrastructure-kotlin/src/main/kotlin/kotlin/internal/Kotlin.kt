package com.redmadrobot.build.kotlin.internal

import com.redmadrobot.build.dsl.isRunningOnCi
import com.redmadrobot.build.internal.InternalGradleInfrastructureApi
import com.redmadrobot.build.internal.findBooleanProperty
import org.gradle.api.Project
import org.gradle.kotlin.dsl.*

@InternalGradleInfrastructureApi
public fun Project.configureKotlin() {
    val warningsAsErrors = getWarningsAsErrorsProperty()
    kotlinCompile {
        compilerOptions {
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
