package com.redmadrobot.build.internal

import com.redmadrobot.build.InfrastructurePlugin
import com.redmadrobot.build.extension.TestOptions
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.kotlin

public fun InfrastructurePlugin.configureKotlinTestDependencies(kotlinVersion: String, testOptions: TestOptions) {
    project.dependencies {
        val kotlinJunitModule = if (testOptions.useJunitPlatform) {
            "test-junit5"
        } else {
            "test-junit"
        }

        testImplementation(kotlin("test", version = kotlinVersion))
        testImplementation(kotlin(kotlinJunitModule, version = kotlinVersion))
    }
}

public fun Test.setTestOptions(testOptions: TestOptions) {
    if (testOptions.useJunitPlatform) {
        useJUnitPlatform {
            excludeEngines = testOptions.jUnitPlatformOptions.excludeEngines
            includeEngines = testOptions.jUnitPlatformOptions.includeEngines
            excludeTags = testOptions.jUnitPlatformOptions.excludeTags
            includeTags = testOptions.jUnitPlatformOptions.includeTags
        }
        testLogging { events("passed", "skipped", "failed") }
    } else {
        useJUnit()
    }
}
