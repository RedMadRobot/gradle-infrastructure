package com.redmadrobot.build.publish

import com.android.build.gradle.BaseExtension
import com.redmadrobot.build.InfrastructurePlugin
import com.redmadrobot.build.dsl.isReleaseVersion
import com.redmadrobot.build.publish.internal.isPluginAutomatedPublishing
import com.redmadrobot.build.publish.internal.java
import com.redmadrobot.build.publish.internal.publishing
import com.redmadrobot.build.publish.internal.signing
import org.gradle.api.Project
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.jvm.tasks.Jar
import org.gradle.kotlin.dsl.*
import org.gradle.plugins.signing.Sign

/**
 * Plugin that configures publication using specified [PublishingOptions].
 *
 * Tied to `redmadrobot.publish` plugin ID.
 */
public open class PublishPlugin : InfrastructurePlugin() {

    override fun Project.configure() {
        apply(plugin = "maven-publish")
        val configPlugin = plugins.apply(PublishConfigPlugin::class)

        // Do it after project evaluate to be able to access publications created later
        afterEvaluate {
            val publicationName = when {
                plugins.hasPlugin("kotlin-android") -> configureAndroidPublication()
                plugins.hasPlugin("java-gradle-plugin") && isPluginAutomatedPublishing -> configurePluginPublication()
                else -> configurePublication()
            }

            val options = configPlugin.publishingOptions
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

    public companion object {
        public const val PUBLICATION_NAME: String = "maven"
        public const val PLUGIN_PUBLICATION_NAME: String = "pluginMaven"
    }
}
