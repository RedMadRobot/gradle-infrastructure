package com.redmadrobot.build.internal

import org.gradle.api.Plugin
import org.gradle.api.plugins.PluginContainer
import org.gradle.kotlin.dsl.hasPlugin

internal inline fun <reified T : Plugin<*>> PluginContainer.hasPlugin(): Boolean {
    return hasPlugin(T::class)
}
