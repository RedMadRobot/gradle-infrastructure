package com.redmadrobot.build

import com.redmadrobot.build.extension.redmadrobotExtension
import io.gitlab.arturbosch.detekt.Detekt
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.register
import org.gradle.kotlin.dsl.repositories

public class DetektPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            requireRootPlugin()
            apply(plugin = "io.gitlab.arturbosch.detekt")

            configureDependencies()
            configureDetektTasks()
        }
    }

    private fun Project.configureDependencies() {
        repositories {
            mavenCentral()
        }

        dependencies {
            "detektPlugins"("io.gitlab.arturbosch.detekt:detekt-formatting:1.15.0")
        }
    }

    private fun Project.configureDetektTasks() {
        detektTask("detektFormat") {
            description = "Reformats whole code base."
            disableDefaultRuleSets = true
            autoCorrect = true
        }

        detektTask("detektAll") {
            description = "Runs over whole code base without the starting overhead for each module."
        }
    }

    private inline fun Project.detektTask(name: String, crossinline configure: Detekt.() -> Unit) {
        tasks.register<Detekt>(name) {
            configure()
            parallel = true
            config.setFrom(redmadrobotExtension.configsDir.file("detekt/detekt.yml"))
            setSource(rootProject.files(rootProject.projectDir))
            reportsDir.set(redmadrobotExtension.reportsDir.asFile)
            include("**/*.kt")
            include("**/*.kts")
            exclude("**/res/**")
            exclude("**/build/**")
            exclude("**/.*/**")
            reports {
                xml.enabled = true
                txt.enabled = false
                html.enabled = false
            }
        }
    }
}
