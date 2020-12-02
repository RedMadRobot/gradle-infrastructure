package com.redmadrobot.build

import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.repositories

public class KotlinLibraryPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            rootProject.apply<RootProjectPlugin>()
            apply(plugin = "kotlin")

            java {
                targetCompatibility = JavaVersion.VERSION_1_8
                sourceCompatibility = JavaVersion.VERSION_1_8
            }

            kotlin.explicitApi()

            configureKotlin()
            configureRepositories()
            configureKotlinDependencies()
        }
    }
}

private fun Project.configureRepositories() {
    repositories {
        jcenter()
    }
}
