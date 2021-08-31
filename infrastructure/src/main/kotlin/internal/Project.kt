package com.redmadrobot.build.internal

import org.gradle.api.Plugin
import org.gradle.api.Project

internal val Project.isRootInfrastructureProject: Boolean
    get() = this == findInfrastructureRootProject()

/** Returns project with applied root-project plugin or `null` if there are no such project. */
internal tailrec fun Project.findInfrastructureRootProject(): Project? {
    if (plugins.hasPlugin("redmadrobot.root-project")) return this
    return parent?.findInfrastructureRootProject()
}

internal inline fun <reified T : Plugin<*>> Project.checkAllSubprojectsContainPlugin(lazyMessage: (String) -> String) {
    val missingPluginModules = subprojects.filterNot { subproject -> subproject.plugins.hasPlugin<T>() }

    check(missingPluginModules.isEmpty()) {
        val modulesName = missingPluginModules.joinToString(", ") { project -> "\"${project.name}\"" }
        lazyMessage.invoke(modulesName)
    }
}
