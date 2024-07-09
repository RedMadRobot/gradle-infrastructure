package com.redmadrobot.build.internal

import org.gradle.api.Project
import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.kotlin.dsl.repositories

@InternalGradleInfrastructureApi
public fun Project.addRepositoriesIfNeed(block: RepositoryHandler.() -> Unit) {
    val addRepositories = findBooleanProperty("redmadrobot.add.repositories") == true
    if (addRepositories) repositories(block)
}
