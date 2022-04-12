package com.redmadrobot.build.kotlin

import com.redmadrobot.build.InfrastructurePlugin
import com.redmadrobot.build.internal.InternalGradleInfrastructureApi
import com.redmadrobot.build.kotlin.internal.configureKotlin
import com.redmadrobot.build.kotlin.internal.java
import com.redmadrobot.build.kotlin.internal.kotlin
import com.redmadrobot.build.kotlin.internal.setTestOptions
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.repositories
import org.gradle.kotlin.dsl.withType

/**
 * Plugin that applies default configurations for Kotlin library project.
 * Should be applied in place of `kotlin` plugin.
 *
 * Tied to `com.redmadrobot.kotlin-library` plugin ID.
 */
public class KotlinLibraryPlugin : InfrastructurePlugin() {

    @InternalGradleInfrastructureApi
    override fun Project.configure() {
        apply(plugin = "kotlin")
        val configPlugin = plugins.apply(KotlinConfigPlugin::class)

        // Enable Explicit API mode for libraries by default
        kotlin.explicitApi()

        configureKotlin(configPlugin.jvmTarget)
        configureKotlinTest(configPlugin.testOptions)
        configureRepositories()

        afterEvaluate {
            java {
                targetCompatibility = configPlugin.jvmTarget.get()
                sourceCompatibility = configPlugin.jvmTarget.get()
            }
        }
    }
}

@OptIn(InternalGradleInfrastructureApi::class)
private fun Project.configureKotlinTest(options: TestOptions) {
    tasks.withType<Test>().configureEach { setTestOptions(options) }
}

private fun Project.configureRepositories() {
    repositories {
        mavenCentral()
    }
}
