package com.redmadrobot.build.internal

import org.gradle.api.internal.plugins.PluginRegistry
import org.gradle.plugin.use.internal.DefaultPluginId

@InternalGradleInfrastructureApi
public fun PluginRegistry.hasPlugin(pluginId: String): Boolean = lookup(DefaultPluginId.unvalidated(pluginId)) != null
