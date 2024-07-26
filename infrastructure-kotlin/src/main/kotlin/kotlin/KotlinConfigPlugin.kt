package com.redmadrobot.build.kotlin

import com.redmadrobot.build.InfrastructurePlugin
import com.redmadrobot.build.internal.InternalGradleInfrastructureApi
import com.redmadrobot.build.internal.hasPlugin
import org.gradle.api.Project
import org.gradle.api.internal.plugins.PluginRegistry
import javax.inject.Inject

/**
 * Plugin that adds configurations for Kotlin projects.
 * Used from [KotlinLibraryPlugin].
 *
 * Tied to `com.redmadrobot.kotlin-config` plugin ID.
 */
public open class KotlinConfigPlugin @Inject constructor(
    private val pluginRegistry: PluginRegistry,
) : InfrastructurePlugin() {

    @InternalGradleInfrastructureApi
    public lateinit var testOptions: TestOptionsImpl
        private set

    @InternalGradleInfrastructureApi
    override fun Project.configure() {
        check(pluginRegistry.hasPlugin("kotlin")) {
            """
            Kotlin Gradle Plugin not found. Make sure you have added it as a build dependency to the project.
            Possible solutions:
            1. Add KGP with "apply false" to the root project:
                 plugins {
                     kotlin("jvm") version <version> apply false
                 }
            2. Add KGP as a dependency to buildSrc or other included build module:
                 dependencies {
                     implementation(kotlin("gradle-plugin", version = <version>))
                 }
            """.trimIndent()
        }

        testOptions = createExtension("test", publicType = TestOptions::class)
    }
}
