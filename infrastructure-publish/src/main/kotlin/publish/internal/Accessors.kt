package com.redmadrobot.build.publish.internal

import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.publish.PublishingExtension
import org.gradle.kotlin.dsl.getByName
import org.gradle.kotlin.dsl.getByType
import org.gradle.plugin.devel.GradlePluginDevelopmentExtension
import org.gradle.plugins.signing.SigningExtension

internal val Project.publishing: PublishingExtension
    get() = extensions.getByName<PublishingExtension>("publishing")

internal val Project.isPluginAutomatedPublishing: Boolean
    get() = extensions.getByType<GradlePluginDevelopmentExtension>().isAutomatedPublishing

internal fun Project.java(configure: JavaPluginExtension.() -> Unit) {
    extensions.configure("java", configure)
}

internal fun Project.publishing(configure: PublishingExtension.() -> Unit) {
    extensions.configure("publishing", configure)
}

internal fun Project.signing(configure: SigningExtension.() -> Unit) {
    extensions.configure("signing", configure)
}
