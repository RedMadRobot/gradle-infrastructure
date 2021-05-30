package com.redmadrobot.build.extension

import org.gradle.api.provider.Property
import org.gradle.api.publish.maven.MavenPom

public interface PublishingOptionsSpec {

    /** Settings for publishing. */
    public val publishing: PublishingOptions

    /** Settings for publishing. */
    public fun publishing(configure: PublishingOptions.() -> Unit)
}

public abstract class PublishingOptions {

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
    public abstract val signArtifacts: Property<Boolean>

    /**
     * Use gpg-agent to sign artifacts. Has effect only if [signArtifacts] is `true`.
     * By default is `true`.
     */
    public abstract val useGpgAgent: Property<Boolean>

    internal abstract val configurePom: Property<MavenPom.() -> Unit>

    /**
     * Configures POM file for all modules.
     * Place here only common configurations.
     */
    public fun pom(configure: MavenPom.() -> Unit) {
        configurePom.set(configure)
    }

    init {
        signArtifacts
            .convention(false)
            .finalizeValueOnRead()
        useGpgAgent
            .convention(true)
            .finalizeValueOnRead()
        configurePom
            .convention { /* no-op */ }
            .finalizeValueOnRead()
    }
}
