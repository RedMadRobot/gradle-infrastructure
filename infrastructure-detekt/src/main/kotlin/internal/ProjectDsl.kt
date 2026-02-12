package com.redmadrobot.build.detekt.internal

import io.gitlab.arturbosch.detekt.DetektPlugin
import org.gradle.api.Project

internal val Project.hasKotlinPlugin: Boolean
    get() = extensions.findByName("kotlin") != null

internal val Project.isKotlinAndroidProject: Boolean
    get() = plugins.hasPlugin("kotlin-android")

internal val Project.hasDetektPlugin: Boolean
    get() = plugins.hasPlugin<DetektPlugin>()
