package com.redmadrobot.build

import com.redmadrobot.build.extension.TestOptions
import com.redmadrobot.build.extension.TestOptionsImpl
import com.redmadrobot.build.internal.configureKotlin
import com.redmadrobot.build.internal.java
import com.redmadrobot.build.internal.kotlin
import com.redmadrobot.build.internal.setTestOptions
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.repositories
import org.gradle.kotlin.dsl.withType

/**
 * Plugin with default configurations for Kotlin library project.
 * Should be applied in place of `kotlin` plugin.
 *
 * Tied to `redmadrobot.kotlin-library` plugin ID.
 */
public class KotlinLibraryPlugin : InfrastructurePlugin() {

    override fun Project.configure() {
        apply(plugin = "kotlin")

        val testOptions = getOrCreateExtension<TestOptionsImpl>("test")

        java {
            targetCompatibility = JavaVersion.VERSION_1_8
            sourceCompatibility = JavaVersion.VERSION_1_8
        }

        // Enable Explicit API mode for libraries by default
        kotlin.explicitApi()

        val extension = redmadrobotExtension
        configureKotlin(extension.jvmTarget)
        configureKotlinTest(testOptions)
        configureRepositories()
    }
}

private fun Project.configureKotlinTest(options: TestOptions) {
    tasks.withType<Test>().configureEach { setTestOptions(options) }
}

private fun Project.configureRepositories() {
    repositories {
        mavenCentral()
    }
}
