package com.redmadrobot.build.kotlin

import com.redmadrobot.build.InfrastructurePlugin
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.provider.Provider

/**
 * Plugin that adds configurations for Kotlin projects.
 * Used from [KotlinLibraryPlugin].
 *
 * Tied to `com.redmadrobot.kotlin-config` plugin ID.
 */
public open class KotlinConfigPlugin : InfrastructurePlugin() {

    public lateinit var testOptions: TestOptionsImpl
        private set

    internal val jvmTarget: Provider<JavaVersion>
        get() = redmadrobotExtension.jvmTarget

    override fun Project.configure() {
        testOptions = createExtension("test")
    }
}
