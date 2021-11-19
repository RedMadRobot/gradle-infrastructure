package com.redmadrobot.build.kotlin.internal

import org.gradle.api.Project

internal fun Project.findBooleanProperty(propertyName: String): Boolean? {
    return findStringProperty(propertyName)?.toBoolean()
}

public fun Project.findStringProperty(propertyName: String): String? {
    return project.findProperty(propertyName) as? String
}
