package com.redmadrobot.build.internal

import org.gradle.api.Project

internal val Project.isInfrastructureRootProject: Boolean
    get() = plugins.hasPlugin("redmadrobot.root-project")

internal val Project.hasKotlinPlugin: Boolean
    get() = extensions.findByName("kotlin") != null

/** Returns project with applied root-project plugin or `null` if there are no such project. */
internal tailrec fun Project.findInfrastructureRootProject(): Project? {
    if (isInfrastructureRootProject) return this
    return parent?.findInfrastructureRootProject()
}
