package com.redmadrobot.build.detekt.internal

import com.android.build.gradle.AppExtension
import com.android.build.gradle.BaseExtension
import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.TestExtension
import io.gitlab.arturbosch.detekt.DetektPlugin
import org.gradle.api.DomainObjectSet
import org.gradle.api.Project

internal val Project.hasKotlinPlugin: Boolean
    get() = extensions.findByName("kotlin") != null

internal val Project.isKotlinAndroidProject: Boolean
    get() = plugins.hasPlugin("kotlin-android")

internal val Project.hasDetektPlugin: Boolean
    get() = plugins.hasPlugin<DetektPlugin>()

@Suppress("DEPRECATION") // Is there the new API to get source sets for variants?
internal val BaseExtension.variants: DomainObjectSet<out com.android.build.gradle.api.BaseVariant>?
    get() = when (this) {
        is AppExtension -> applicationVariants
        is LibraryExtension -> libraryVariants
        is TestExtension -> applicationVariants
        else -> null
    }
