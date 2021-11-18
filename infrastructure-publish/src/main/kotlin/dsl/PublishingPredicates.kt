package com.redmadrobot.build.dsl

import org.gradle.api.Project

/**
 * Checks that credentials for the repository with given [name] are exist.
 *
 * See: [Handling credentials](https://docs.gradle.org/current/userguide/declaring_repositories.html#sec:handling_credentials)
 */
public fun Project.credentialsExist(name: String): Boolean {
    return hasProperty("${name}Username") && hasProperty("${name}Password")
}
