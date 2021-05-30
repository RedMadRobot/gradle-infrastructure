package com.redmadrobot.build.extension

import org.gradle.api.provider.Property
import org.gradle.api.publish.maven.MavenPom

@Suppress("LeakingThis")
internal abstract class PublishingOptionsImpl : PublishingOptions {

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
}
