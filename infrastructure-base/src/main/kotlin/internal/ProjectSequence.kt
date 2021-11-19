package com.redmadrobot.build.internal

import org.gradle.api.Project

/** Iterates through the project hierarchy from the project parent to the root project. */
@PublishedApi
internal val Project.parents: Sequence<Project>
    get() = sequence {
        var project: Project? = project.parent
        while (project != null) {
            yield(project)
            project = project.parent
        }
    }
