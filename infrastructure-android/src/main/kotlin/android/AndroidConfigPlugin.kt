package com.redmadrobot.build.android

import com.redmadrobot.build.InfrastructurePlugin
import com.redmadrobot.build.kotlin.KotlinConfigPlugin
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.provider.Property
import org.gradle.kotlin.dsl.apply

/**
 * Plugin that adds configurations for Android projects.
 * Used from [BaseAndroidPlugin].
 *
 * Tied to `com.redmadrobot.android-config` plugin ID.
 */
public open class AndroidConfigPlugin : InfrastructurePlugin() {

    internal lateinit var androidOptions: AndroidOptionsImpl
        private set

    internal val jvmTarget: Property<JavaVersion>
        get() = redmadrobotExtension.jvmTarget

    override fun Project.configure() {
        val kotlinConfigPlugin = plugins.apply(KotlinConfigPlugin::class)
        androidOptions = createExtension("android")
        androidOptions.setTestDefaults(kotlinConfigPlugin.testOptions)
    }
}
