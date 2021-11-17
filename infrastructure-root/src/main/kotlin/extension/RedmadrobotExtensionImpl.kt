package com.redmadrobot.build.extension

import org.gradle.api.JavaVersion

@Suppress("LeakingThis")
internal abstract class RedmadrobotExtensionImpl : RedmadrobotExtension {

    init {
        jvmTarget
            .convention(JavaVersion.VERSION_1_8)
            .finalizeValueOnRead()
    }
}
