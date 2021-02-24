package com.redmadrobot.build

import com.redmadrobot.build.extension.isReleaseVersion
import com.redmadrobot.build.extension.redmadrobotExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.jvm.tasks.Jar
import org.gradle.kotlin.dsl.*
import org.gradle.plugins.signing.Sign

public class PublishPlugin : Plugin<Project> {

    public companion object {
        public const val PUBLICATION_NAME: String = "maven"
        public const val PLUGIN_PUBLICATION_NAME: String = "pluginMaven"
    }

    override fun apply(target: Project) {
        with(target) {
            apply(plugin = "maven-publish")

            val publicationName = when {
                plugins.hasPlugin("kotlin-android") -> configureAndroidPublication()
                plugins.hasPlugin("java-gradle-plugin") -> configurePluginPublication()
                else -> configurePublication()
            }

            // Do it after project evaluate to be able to access publications created later
            afterEvaluate {
                val redmadrobot = redmadrobotExtension

                publishing.publications.getByName<MavenPublication>(publicationName) {
                    pom {
                        name.convention(project.name)
                        description.convention(project.description)
                        redmadrobot.publishing.configurePom(this)
                    }
                }

                if (redmadrobot.publishing.signArtifacts) {
                    configureSigning(publicationName, redmadrobot.publishing.useGpgAgent)
                }
            }
        }
    }

    private fun Project.configureAndroidPublication(): String {
        val sourcesJar = tasks.register<Jar>("sourcesJar") {
            archiveClassifier.set("sources")
            from(android.sourceSets["main"].java.srcDirs)
        }

        // Pre-create publication to make it configurable
        publishing.publications.create<MavenPublication>(PUBLICATION_NAME)

        // Because the components are created only during the afterEvaluate phase, you must
        // configure your publications using the afterEvaluate() lifecycle method.
        afterEvaluate {
            if (version == Project.DEFAULT_VERSION) {
                version = checkNotNull(android.defaultConfig.versionName) {
                    "You should specify either project 'version' or 'android.versionName' for publication."
                }
            }

            publishing {
                publications.getByName<MavenPublication>(PUBLICATION_NAME) {
                    from(components["release"])
                    artifact(sourcesJar.get())
                }
            }
        }

        return PUBLICATION_NAME
    }

    private fun Project.configurePluginPublication(): String {
        @Suppress("UnstableApiUsage")
        java {
            withSourcesJar()
            withJavadocJar()
        }

        return PLUGIN_PUBLICATION_NAME
    }

    private fun Project.configurePublication(): String {
        @Suppress("UnstableApiUsage")
        java {
            withSourcesJar()
            withJavadocJar()
        }

        publishing {
            publications.create<MavenPublication>(PUBLICATION_NAME) {
                from(components["java"])
            }
        }

        return PUBLICATION_NAME
    }

    private fun Project.configureSigning(publicationName: String, useGpgAgent: Boolean) {
        apply(plugin = "signing")

        signing {
            if (useGpgAgent) useGpgCmd()
            sign(publishing.publications[publicationName])
        }

        tasks.withType<Sign>().configureEach {
            onlyIf { isReleaseVersion }
        }
    }
}
