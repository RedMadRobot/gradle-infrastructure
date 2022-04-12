package com.redmadrobot.build.kotlin.internal

import com.redmadrobot.build.internal.InternalGradleInfrastructureApi
import org.gradle.api.Project

@OptIn(InternalGradleInfrastructureApi::class)
internal fun Project.findBooleanProperty(propertyName: String): Boolean? {
    return findStringProperty(propertyName)?.toBoolean()
}

@InternalGradleInfrastructureApi
public fun Project.findStringProperty(propertyName: String): String? {
    return providers.gradleProperty(propertyName).orNull
}
