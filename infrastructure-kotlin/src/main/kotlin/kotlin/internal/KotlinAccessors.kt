package com.redmadrobot.build.kotlin.internal

import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile

internal fun Project.java(configure: JavaPluginExtension.() -> Unit) {
    extensions.configure("java", configure)
}

internal inline fun Project.kotlinCompile(crossinline configure: KotlinJvmCompile.() -> Unit) {
    tasks.withType<KotlinJvmCompile>().configureEach { configure() }
}
