package com.redmadrobot.build.kotlin

import com.redmadrobot.build.InfrastructurePlugin
import org.gradle.api.Project

/**
 * Plugin that adds configurations for Kotlin projects.
 * Used from [KotlinLibraryPlugin].
 *
 * Tied to `redmadrobot.kotlin-config` plugin ID.
 */
public open class KotlinConfigPlugin : InfrastructurePlugin() {

    public lateinit var testOptions: TestOptionsImpl
        private set

    override fun Project.configure() {
        testOptions = createExtension("test")
    }
}
