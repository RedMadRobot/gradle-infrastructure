package com.redmadrobot.build.internal

import com.redmadrobot.build.InfrastructurePlugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.kotlin.dsl.getByName
import org.jetbrains.kotlin.gradle.dsl.KotlinProjectExtension

internal fun Project.java(configure: JavaPluginExtension.() -> Unit) {
    extensions.configure("java", configure)
}

public val InfrastructurePlugin.kotlin: KotlinProjectExtension get() = project.kotlin

internal val Project.kotlin: KotlinProjectExtension get() = extensions.getByName<KotlinProjectExtension>("kotlin")
