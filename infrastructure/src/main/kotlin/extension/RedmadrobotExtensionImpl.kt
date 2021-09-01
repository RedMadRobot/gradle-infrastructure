package com.redmadrobot.build.extension

import org.gradle.api.JavaVersion
import org.gradle.api.model.ObjectFactory
import org.gradle.kotlin.dsl.newInstance
import javax.inject.Inject

@Suppress("LeakingThis")
internal abstract class RedmadrobotExtensionImpl @Inject constructor(objects: ObjectFactory) : RedmadrobotExtension {

    override val publishing: PublishingOptions = objects.newInstance<PublishingOptionsImpl>()

    override fun publishing(configure: PublishingOptions.() -> Unit) {
        publishing.configure()
    }

    override val test: TestOptions = objects.newInstance<TestOptionsImpl>()

    override fun test(configure: TestOptions.() -> Unit) {
        test.configure()
    }

    override val detekt: DetektOptions = objects.newInstance<DetektOptionsImpl>()

    override fun detekt(configure: DetektOptions.() -> Unit) {
        detekt.configure()
    }

    init {
        jvmTarget
            .convention(JavaVersion.VERSION_1_8)
            .finalizeValueOnRead()
    }
}
