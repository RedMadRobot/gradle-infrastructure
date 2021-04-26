package com.redmadrobot.build.internal

import org.gradle.api.Project

internal fun Project.findBooleanProperty(propertyName: String): Boolean? {
    return (project.findProperty(propertyName) as? String)?.toBoolean()
}
