package com.redmadrobot.build

import com.redmadrobot.build.detekt.DetektConfiguration
import com.redmadrobot.build.internal.detektPlugins
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.repositories

/**
 * Plugin with common configurations for detekt.
 * Should be applied to root project only.
 *
 * Tied to `redmadrobot.detekt` plugin ID.
 */
public class DetektPlugin : InfrastructurePlugin() {

    override fun Project.configure() {
        apply(plugin = "io.gitlab.arturbosch.detekt")

        configureDependencies()

        DetektConfiguration(this).configure(redmadrobotExtension, infrastructureRootProject)
    }

    internal companion object {
        const val BASELINE_KEYWORD = "Baseline"
    }
}

private fun Project.configureDependencies() {
    repositories {
        mavenCentral()
    }

    dependencies {
        detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:1.18.1")
    }
}
