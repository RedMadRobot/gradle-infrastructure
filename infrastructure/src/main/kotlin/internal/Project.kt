package com.redmadrobot.build.internal

import org.gradle.api.Plugin
import org.gradle.api.Project

internal val Project.isInfrastructureRootProject: Boolean
    get() = plugins.hasPlugin("redmadrobot.root-project")

/** Returns project with applied root-project plugin or `null` if there are no such project. */
internal tailrec fun Project.findInfrastructureRootProject(): Project? {
    if (isInfrastructureRootProject) return this
    return parent?.findInfrastructureRootProject()
}

internal inline fun <reified T : Plugin<*>> Project.checkAllSubprojectsContainPlugin(lazyMessage: (String) -> String) {
    val missingPluginModules = subprojects.filterNot { subproject -> subproject.plugins.hasPlugin<T>() }

    check(missingPluginModules.isEmpty()) {
        val modulesName = missingPluginModules.joinToString(", ") { project -> "\"${project.name}\"" }
        lazyMessage.invoke(modulesName)
    }
}
