package com.redmadrobot.build.internal

import org.gradle.api.Plugin
import org.gradle.api.Project

internal val Project.isRoot: Boolean
    get() = this == rootProject

internal inline fun <reified T : Plugin<*>> Project.checkAllSubprojectsContainPlugin(lazyMessage: (String) -> String) {
    val missingPluginModules = subprojects.filterNot { subproject -> subproject.plugins.hasPlugin<T>() }

    check(missingPluginModules.isEmpty()) {
        val modulesName = missingPluginModules.joinToString(", ") { project -> "\"${project.name}\"" }
        lazyMessage.invoke(modulesName)
    }
}
