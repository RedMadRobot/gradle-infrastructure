package com.redmadrobot.build

import com.redmadrobot.build.extension.RedmadrobotExtension
import com.redmadrobot.build.internal.detektPlugins
import io.gitlab.arturbosch.detekt.Detekt
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.register
import org.gradle.kotlin.dsl.repositories

public class DetektPlugin : InfrastructurePlugin() {

    override fun Project.configure() {
        apply(plugin = "io.gitlab.arturbosch.detekt")

        configureDependencies()
        configureDetektTasks(redmadrobotExtension)
    }
}

private fun Project.configureDependencies() {
    repositories {
        mavenCentral()
    }

    dependencies {
        detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:1.17.1")
    }
}

private fun Project.configureDetektTasks(extension: RedmadrobotExtension) {
    detektTask(extension, "detektFormat") {
        description = "Reformats whole code base."
        disableDefaultRuleSets = true
        autoCorrect = true
    }

    detektTask(extension, "detektAll") {
        description = "Runs over whole code base without the starting overhead for each module."
    }
}

private inline fun Project.detektTask(
    extension: RedmadrobotExtension,
    name: String,
    crossinline configure: Detekt.() -> Unit,
) {
    tasks.register<Detekt>(name) {
        configure()
        parallel = true
        config.setFrom(extension.configsDir.file("detekt/detekt.yml"))
        baseline.set(extension.configsDir.file("detekt/baseline.xml"))
        setSource(rootProject.files(rootProject.projectDir))
        reportsDir.set(extension.reportsDir.asFile)
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
