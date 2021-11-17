package com.redmadrobot.build.internal

import org.gradle.api.Project

public val Project.isInfrastructureRootProject: Boolean
    get() = plugins.hasPlugin("redmadrobot.root-project")

/** Returns project with applied root-project plugin or `null` if there are no such project. */
public tailrec fun Project.findInfrastructureRootProject(): Project? {
    if (isInfrastructureRootProject) return this
    return parent?.findInfrastructureRootProject()
}
