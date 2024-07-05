package com.redmadrobot.build.kotlin.internal

import com.redmadrobot.build.InfrastructurePlugin
import com.redmadrobot.build.internal.InternalGradleInfrastructureApi
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.kotlin.dsl.getByName
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.KotlinProjectExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile

internal fun Project.java(configure: JavaPluginExtension.() -> Unit) {
    extensions.configure("java", configure)
}

@InternalGradleInfrastructureApi
public val InfrastructurePlugin.kotlin: KotlinProjectExtension get() = project.kotlin

internal val Project.kotlin: KotlinProjectExtension get() = extensions.getByName<KotlinProjectExtension>("kotlin")

internal inline fun Project.kotlinCompile(crossinline configure: KotlinJvmCompile.() -> Unit) {
    tasks.withType<KotlinJvmCompile>().configureEach { configure() }
}
