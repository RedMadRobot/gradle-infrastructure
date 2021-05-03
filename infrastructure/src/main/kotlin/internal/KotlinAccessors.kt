package com.redmadrobot.build.internal

import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.kotlin.dsl.getByName
import org.jetbrains.kotlin.gradle.dsl.KotlinProjectExtension

internal fun Project.java(configure: JavaPluginExtension.() -> Unit) {
    extensions.configure("java", configure)
}

internal val Project.kotlin: KotlinProjectExtension get() = extensions.getByName<KotlinProjectExtension>("kotlin")
