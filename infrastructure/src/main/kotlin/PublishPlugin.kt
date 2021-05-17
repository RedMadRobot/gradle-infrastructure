package com.redmadrobot.build

import com.redmadrobot.build.extension.isReleaseVersion
import com.redmadrobot.build.internal.java
import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.kotlin.dsl.*
import org.gradle.plugin.devel.GradlePluginDevelopmentExtension
import org.gradle.plugins.signing.Sign
import org.gradle.plugins.signing.SigningExtension

public open class PublishPlugin : InfrastructurePlugin() {

    public companion object {
        public const val PUBLICATION_NAME: String = "maven"
        public const val PLUGIN_PUBLICATION_NAME: String = "pluginMaven"
    }

    override fun Project.configure() {
        apply(plugin = "maven-publish")

        // Do it after project evaluate to be able to access publications created later
        afterEvaluate {
            val publicationName = when {
                plugins.hasPlugin("kotlin-android") -> configureAndroidPublication()
                plugins.hasPlugin("java-gradle-plugin") && isPluginAutomatedPublishing -> configurePluginPublication()
                else -> configurePublication()
            }

            val options = redmadrobotExtension.publishing

            publishing.publications.getByName<MavenPublication>(publicationName) {
                pom {
                    name.convention(project.name)
                    description.convention(project.description)
                    options.configurePom(this)
                }
            }

            if (options.signArtifacts) {
                configureSigning(options.useGpgAgent)
            }
        }
    }

    protected open fun Project.configureAndroidPublication(): String {
        error(
            """
            The project was recognized as Android-related because the plugin 'kotlin-android' was found.
            If you want to publish android artifacts, you should use 'infrastructure-android'
            instead of 'infrastructure'.
            You can change it in gradle.settings.kts
            """.trimIndent(),
        )
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

    private fun Project.configureSigning(useGpgAgent: Boolean) {
        apply(plugin = "signing")

        signing {
            if (useGpgAgent) useGpgCmd()
            sign(publishing.publications)
        }

        tasks.withType<Sign>().configureEach {
            onlyIf { isReleaseVersion }
        }
    }

    // Accessors

    protected val publishing: PublishingExtension
        get() = project.extensions.getByName<PublishingExtension>("publishing")

    private val isPluginAutomatedPublishing: Boolean
        get() = project.extensions.getByType<GradlePluginDevelopmentExtension>().isAutomatedPublishing

    protected fun publishing(configure: PublishingExtension.() -> Unit) {
        project.extensions.configure("publishing", configure)
    }

    private fun signing(configure: SigningExtension.() -> Unit) {
        project.extensions.configure("signing", configure)
    }
}
