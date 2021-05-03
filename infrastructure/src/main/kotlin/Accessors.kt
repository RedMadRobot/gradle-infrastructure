package com.redmadrobot.build

import com.android.build.gradle.BaseExtension
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.kotlin.dsl.getByName
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.KotlinProjectExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

internal fun Project.java(configure: JavaPluginExtension.() -> Unit) {
    extensions.configure("java", configure)
}

internal val Project.kotlin: KotlinProjectExtension get() = extensions.getByName<KotlinProjectExtension>("kotlin")

internal val Project.android: BaseExtension get() = extensions.getByName<BaseExtension>("android")

public inline fun Project.kotlinCompile(crossinline configure: KotlinCompile.() -> Unit) {
    tasks.withType<KotlinCompile> { configure() }
}
