package com.redmadrobot.build.kotlin

import com.redmadrobot.build.InfrastructurePlugin
import com.redmadrobot.build.kotlin.internal.configureKotlin
import com.redmadrobot.build.kotlin.internal.java
import com.redmadrobot.build.kotlin.internal.kotlin
import com.redmadrobot.build.kotlin.internal.setTestOptions
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

        val testOptions = createExtension<TestOptionsImpl>("test")

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
