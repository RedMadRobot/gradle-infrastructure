package com.redmadrobot.build

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.jvm.tasks.Jar
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.register

public class PublishPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            apply(plugin = "maven-publish")

            when {
                plugins.hasPlugin("kotlin-android") -> configureAndroidPublication()
                else -> configurePublication()
            }
        }
    }

    private fun Project.configureAndroidPublication() {
        val sourcesJar = tasks.register<Jar>("sourcesJar") {
            archiveClassifier.set("sources")
            from(android.sourceSets["main"].java.srcDirs)
        }

        // Because the components are created only during the afterEvaluate phase, you must
        // configure your publications using the afterEvaluate() lifecycle method.
        afterEvaluate {
            if (version == Project.DEFAULT_VERSION) {
                version = checkNotNull(android.defaultConfig.versionName) {
                    "You should specify either project 'version' or 'android.versionName' for publication."
                }
            }

            publishing {
                publications.create<MavenPublication>("maven") {
                    from(components["release"])
                    artifact(sourcesJar.get())
                }
            }
        }
    }

    private fun Project.configurePublication() {
        java {
            @Suppress("UnstableApiUsage")
            withSourcesJar()
        }

        publishing {
            publications.create<MavenPublication>("maven") {
                from(components["java"])
            }
        }
    }
}
