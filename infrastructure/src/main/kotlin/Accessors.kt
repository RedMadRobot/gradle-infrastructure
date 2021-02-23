package com.redmadrobot.build

import com.android.build.gradle.BaseExtension
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.getByName
import org.gradle.kotlin.dsl.withType
import org.gradle.plugins.signing.SigningExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinProjectExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

internal val Project.publishing: PublishingExtension
    get() = extensions.getByName<PublishingExtension>("publishing")

internal fun Project.publishing(configure: PublishingExtension.() -> Unit) {
    extensions.configure("publishing", configure)
}

internal fun Project.signing(configure: SigningExtension.() -> Unit) {
    extensions.configure("signing", configure)
}

internal val Project.java: JavaPluginExtension get() = extensions.getByName<JavaPluginExtension>("java")

internal fun Project.java(configure: JavaPluginExtension.() -> Unit) {
    extensions.configure("java", configure)
}

internal val Project.kotlin: KotlinProjectExtension get() = extensions.getByName<KotlinProjectExtension>("kotlin")

internal val Project.android: BaseExtension get() = extensions.getByName<BaseExtension>("android")

internal fun <T : BaseExtension> Project.android(configure: T.() -> Unit) {
    extensions.configure("android", configure)
}

public inline fun Project.kotlinCompile(crossinline configure: KotlinCompile.() -> Unit) {
    tasks.withType<KotlinCompile> { configure() }
}

internal inline fun Project.kotlinTest(crossinline configure: Test.() -> Unit) {
    tasks.withType<Test> { configure() }
}
