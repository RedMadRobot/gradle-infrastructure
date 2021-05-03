package com.redmadrobot.build

import com.redmadrobot.build.extension.redmadrobotExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.repositories

public class KotlinLibraryPlugin : InfrastructurePlugin() {

    override fun Project.configure() {
        apply(plugin = "kotlin")

        java {
            targetCompatibility = JavaVersion.VERSION_1_8
            sourceCompatibility = JavaVersion.VERSION_1_8
        }

        kotlin.explicitApi()

        configureKotlin()
        configureKotlinTest()
        configureRepositories()
        configureKotlinDependencies()
        configureKotlinTestDependencies(redmadrobotExtension.test)
    }
}

private fun Project.configureRepositories() {
    repositories {
        mavenCentral()
    }
}
