package com.redmadrobot.build.publish

import com.android.build.api.dsl.LibraryExtension
import com.redmadrobot.build.InfrastructurePlugin
import com.redmadrobot.build.dsl.isReleaseVersion
import com.redmadrobot.build.internal.InternalGradleInfrastructureApi
import com.redmadrobot.build.publish.internal.isPluginAutomatedPublishing
import com.redmadrobot.build.publish.internal.java
import com.redmadrobot.build.publish.internal.publishing
import com.redmadrobot.build.publish.internal.signing
import org.gradle.api.Project
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.kotlin.dsl.*
import org.gradle.plugins.signing.Sign

/**
 * Plugin that configures publication using specified [PublishingOptions].
 *
 * Tied to `com.redmadrobot.publish` plugin ID.
 */
public open class PublishPlugin : InfrastructurePlugin() {

    @InternalGradleInfrastructureApi
    override fun Project.configure() {
        apply(plugin = "maven-publish")
        val configPlugin = plugins.apply(PublishConfigPlugin::class)

        val publicationName = when {
            plugins.hasPlugin("com.android.library") -> configureAndroidLibraryPublication()
            plugins.hasPlugin("java-gradle-plugin") && isPluginAutomatedPublishing -> configurePluginPublication()
            plugins.hasPlugin("org.gradle.version-catalog") -> configureVersionCatalogPublication()
            plugins.hasPlugin("java") -> configureJavaLibraryPublication()

            else -> {
                logger.warn(
                    """
                    Can not automatically configure publishing for the project ${project.name}.
                    Project type has not recognized.
                    """.trimIndent()
                )
                return
            }
        }

        // Do it after project evaluate to be able to access publications created later
        afterEvaluate {
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

    private fun Project.configureAndroidLibraryPublication(): String {
        extensions.configure<LibraryExtension>("android") {
            publishing {
                singleVariant("release") {
                    withSourcesJar()
                    withJavadocJar()
                }
            }
        }

        publishing {
            publications.create<MavenPublication>(PUBLICATION_NAME) {
                afterEvaluate {
                    from(components["release"])
                }
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

    private fun Project.configureVersionCatalogPublication(): String {
        publishing {
            publications.create<MavenPublication>(PUBLICATION_NAME) {
                from(components["versionCatalog"])
            }
        }

        return PUBLICATION_NAME
    }

    private fun Project.configureJavaLibraryPublication(): String {
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
