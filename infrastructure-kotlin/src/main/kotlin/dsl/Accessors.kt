package com.redmadrobot.build.dsl

import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

/** Configures all tasks with type [KotlinCompile]. */
@Deprecated(
    "Use kotlin { ... } or tasks.withType<KotlinJvmCompile>() instead",
    ReplaceWith("kotlin(configure)")
)
public inline fun Project.kotlinCompile(crossinline configure: KotlinCompile.() -> Unit) {
    tasks.withType<KotlinCompile>().configureEach { configure() }
}
