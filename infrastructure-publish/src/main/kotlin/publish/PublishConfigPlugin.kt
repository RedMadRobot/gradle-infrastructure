package com.redmadrobot.build.publish

import com.redmadrobot.build.InfrastructurePlugin
import org.gradle.api.Project

/**
 * Plugin that adds configurations for publication.
 * Used from [PublishPlugin].
 *
 * Tied to `com.redmadrobot.publish-config` plugin ID.
 */
public open class PublishConfigPlugin : InfrastructurePlugin() {

    internal lateinit var publishingOptions: PublishingOptionsImpl
        private set

    override fun Project.configure() {
        publishingOptions = createExtension("publishing")
    }
}
