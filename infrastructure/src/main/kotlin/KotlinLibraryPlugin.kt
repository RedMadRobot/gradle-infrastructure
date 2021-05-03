package com.redmadrobot.build

import com.redmadrobot.build.extension.TestOptions
import com.redmadrobot.build.internal.*
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.repositories
import org.gradle.kotlin.dsl.withType

public class KotlinLibraryPlugin : InfrastructurePlugin() {

    override fun Project.configure() {
        apply(plugin = "kotlin")

        java {
            targetCompatibility = JavaVersion.VERSION_1_8
            sourceCompatibility = JavaVersion.VERSION_1_8
        }

        kotlin.explicitApi()

        val extension = redmadrobotExtension
        configureKotlin()
        configureKotlinDependencies(extension.kotlinVersion)
        configureKotlinTest(extension.test)
        configureKotlinTestDependencies(extension.kotlinVersion, extension.test)
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
