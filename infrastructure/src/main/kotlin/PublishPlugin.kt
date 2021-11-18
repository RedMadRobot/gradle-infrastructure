package com.redmadrobot.build

import com.android.build.gradle.BaseExtension
import com.redmadrobot.build.dsl.isReleaseVersion
import com.redmadrobot.build.extension.PublishingOptionsImpl
import com.redmadrobot.build.internal.java
import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.jvm.tasks.Jar
import org.gradle.kotlin.dsl.*
import org.gradle.plugin.devel.GradlePluginDevelopmentExtension
import org.gradle.plugins.signing.Sign
import org.gradle.plugins.signing.SigningExtension

/**
 * Plugin with configurations for publication.
 *
 * Tied to `redmadrobot.publish` plugin ID.
 */
public open class PublishPlugin : InfrastructurePlugin() {

    public companion object {
        public const val PUBLICATION_NAME: String = "maven"
        public const val PLUGIN_PUBLICATION_NAME: String = "pluginMaven"
    }

    override fun Project.configure() {
        apply(plugin = "maven-publish")

        val options = createExtension<PublishingOptionsImpl>("publish")

        // Do it after project evaluate to be able to access publications created later
        afterEvaluate {
            val publicationName = when {
                plugins.hasPlugin("kotlin-android") -> configureAndroidPublication()
                plugins.hasPlugin("java-gradle-plugin") && isPluginAutomatedPublishing -> configurePluginPublication()
                else -> configurePublication()
            }

            publishing.publications.getByName<MavenPublication>(publicationName) {
                pom {
                    name.convention(project.name)
                    description.convention(project.description)
                    options.configurePom.get().invoke(this)
                }
            }

            if (options.signArtifacts.get()) {
                configureSigning(options.useGpgAgent.get())
            }
        }
    }

    private fun Project.configureAndroidPublication(): String {
        val android = extensions.getByType<BaseExtension>()
        if (version == Project.DEFAULT_VERSION) {
            version = checkNotNull(android.defaultConfig.versionName) {
                "You should specify either project 'version' or 'android.versionName' for publication."
            }
        }

        val sourcesJar = tasks.register<Jar>("sourcesJar") {
            archiveClassifier.set("sources")
            from(android.sourceSets["main"].java.srcDirs)
        }

        publishing {
            publications.create<MavenPublication>(PUBLICATION_NAME) {
                from(components["release"])
                artifact(sourcesJar.get())
            }
        }

        return PUBLICATION_NAME
    }

    private fun Project.configurePluginPublication(): String {
        java {
            withSourcesJar()
            withJavadocJar()
        }

        return PLUGIN_PUBLICATION_NAME
    }

    private fun Project.configurePublication(): String {
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
