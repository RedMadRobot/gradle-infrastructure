package com.redmadrobot.build.extension

import org.gradle.api.provider.Property
import org.gradle.api.publish.maven.MavenPom

public interface PublishingOptionsSpec {

    /** Settings for publishing. */
    public val publishing: PublishingOptions

    /** Settings for publishing. */
    public fun publishing(configure: PublishingOptions.() -> Unit)
}

public interface PublishingOptions {

    /**
     * Enables artifacts signing before publication.
     *
     * By default tries to use gpg-agent to sign artifacts, but you can disable it with setting
     * [useGpgAgent] to false.
     * If you don't use gpg-agent, requires signatory credentials to be configured in `gradle.properties`.
     * Read more: [Signing Plugin](https://docs.gradle.org/current/userguide/signing_plugin.html#signing_plugin)
     *
     * @see useGpgAgent
     */
    public val signArtifacts: Property<Boolean>

    /**
     * Use gpg-agent to sign artifacts. Has effect only if [signArtifacts] is `true`.
     * By default is `true`.
     */
    public val useGpgAgent: Property<Boolean>

    /**
     * Configures POM file for all modules.
     * Place here only common configurations.
     */
    public fun pom(configure: MavenPom.() -> Unit)
}
