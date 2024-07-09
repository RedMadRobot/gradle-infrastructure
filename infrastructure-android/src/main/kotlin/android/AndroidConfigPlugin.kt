package com.redmadrobot.build.android

import com.redmadrobot.build.InfrastructurePlugin
import com.redmadrobot.build.StaticAnalyzerSpec
import com.redmadrobot.build.internal.InternalGradleInfrastructureApi
import com.redmadrobot.build.internal.hasPlugin
import com.redmadrobot.build.kotlin.KotlinConfigPlugin
import org.gradle.api.Project
import org.gradle.api.internal.plugins.PluginRegistry
import org.gradle.kotlin.dsl.apply
import javax.inject.Inject

/**
 * Plugin that adds configurations for Android projects.
 * Used from [BaseAndroidPlugin].
 *
 * Tied to `com.redmadrobot.android-config` plugin ID.
 */
public open class AndroidConfigPlugin @Inject constructor(
    private val pluginRegistry: PluginRegistry,
) : InfrastructurePlugin() {

    internal lateinit var androidOptions: AndroidOptionsImpl
        private set

    internal val staticAnalyzerSpec: StaticAnalyzerSpec
        get() = redmadrobotExtension

    @InternalGradleInfrastructureApi
    override fun Project.configure() {
        check(pluginRegistry.hasPlugin("com.android.application")) {
            """
            Android Gradle Plugin not found. Make sure you have added it as a build dependency to the project.
            Possible solutions:
            1. Add AGP with "apply false" to the root project:
                 plugins {
                     id("com.android.application") version <version> apply false
                 }
            2. Add AGP as a dependency to buildSrc or other included build module:
                 dependencies {
                     implementation("com.android.tools.build:gradle:<version>")
                 }
            """.trimIndent()
        }

        val kotlinConfigPlugin = plugins.apply(KotlinConfigPlugin::class)
        androidOptions = createExtension("android")
        androidOptions.setTestDefaults(kotlinConfigPlugin.testOptions)
    }
}
