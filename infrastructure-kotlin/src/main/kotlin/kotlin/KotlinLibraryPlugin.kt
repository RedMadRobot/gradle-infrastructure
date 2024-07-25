package com.redmadrobot.build.kotlin

import com.redmadrobot.build.internal.InternalGradleInfrastructureApi
import com.redmadrobot.build.internal.addRepositoriesIfNeed
import com.redmadrobot.build.kotlin.internal.setTestOptions
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.kotlinExtension

/**
 * Plugin that applies default configurations for a Kotlin library project.
 * Should be applied in place of `kotlin` plugin.
 *
 * Tied to `com.redmadrobot.kotlin-library` plugin ID.
 */
public class KotlinLibraryPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        target.configure()
    }

    @OptIn(InternalGradleInfrastructureApi::class)
    private fun Project.configure() {
        apply(plugin = "kotlin")
        val configPlugin = plugins.apply(KotlinConfigPlugin::class)

        // Enable Explicit API mode for libraries by default
        kotlinExtension.explicitApi()

        configureKotlinTest(configPlugin.testOptions)
        configureRepositories()
    }
}

@OptIn(InternalGradleInfrastructureApi::class)
private fun Project.configureKotlinTest(options: TestOptions) {
    tasks.withType<Test>().configureEach { setTestOptions(options) }
}

@OptIn(InternalGradleInfrastructureApi::class)
private fun Project.configureRepositories() {
    addRepositoriesIfNeed {
        mavenCentral()
    }
}
