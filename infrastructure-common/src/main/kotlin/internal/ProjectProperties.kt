package com.redmadrobot.build.internal

import org.gradle.api.Project

@InternalGradleInfrastructureApi
public fun Project.findBooleanProperty(propertyName: String): Boolean? {
    return findStringProperty(propertyName)?.toBoolean()
}

@InternalGradleInfrastructureApi
public fun Project.findStringProperty(propertyName: String): String? {
    return providers.gradleProperty(propertyName).orNull
}
