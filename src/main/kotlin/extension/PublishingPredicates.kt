package com.redmadrobot.build.extension

import org.gradle.api.Project

/** Returns `true` if version contains `-SNAPSHOT` suffix. */
val Project.isSnapshotVersion: Boolean
    get() = version.toString().endsWith("-SNAPSHOT")

/**
 * Checks that credentials for the repository with given [name] are exist.
 *
 * See: https://docs.gradle.org/current/userguide/declaring_repositories.html#sec:handling_credentials
 */
fun Project.credentialsExist(name: String): Boolean {
    return hasProperty("${name}Username") && hasProperty("${name}Password")
}
