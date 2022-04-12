package com.redmadrobot.build.kotlin

import com.redmadrobot.build.InfrastructurePlugin
import com.redmadrobot.build.internal.InternalGradleInfrastructureApi
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

    @InternalGradleInfrastructureApi
    public lateinit var testOptions: TestOptionsImpl
        private set

    internal val jvmTarget: Provider<JavaVersion>
        get() = redmadrobotExtension.jvmTarget

    @InternalGradleInfrastructureApi
    override fun Project.configure() {
        testOptions = createExtension("test")
    }
}
