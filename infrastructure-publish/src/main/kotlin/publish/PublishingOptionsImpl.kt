package com.redmadrobot.build.publish

import com.redmadrobot.build.WithDefaults
import org.gradle.api.provider.Property
import org.gradle.api.publish.maven.MavenPom

@Suppress("LeakingThis")
internal abstract class PublishingOptionsImpl : PublishingOptions, WithDefaults<PublishingOptionsImpl> {

    abstract val configurePom: Property<MavenPom.() -> Unit>

    override fun pom(configure: MavenPom.() -> Unit) {
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

    override fun setDefaults(defaults: PublishingOptionsImpl) {
        signArtifacts.convention(defaults.signArtifacts)
        useGpgAgent.convention(defaults.useGpgAgent)
        configurePom.convention(defaults.configurePom)
    }
}
