package com.redmadrobot.build.extension

import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.api.artifacts.repositories.PasswordCredentials
import org.gradle.kotlin.dsl.credentials

/**
 * Adds RMR Nexus repo with name "rmrNexus".
 * Credentials should be provided through project properties `rmrNexusUsername` and `rmrNexusPassword`.
 *
 * See: https://docs.gradle.org/current/userguide/declaring_repositories.html#sec:handling_credentials
 */
fun RepositoryHandler.rmrNexus() {
    maven {
        name = "rmrNexus"
        setUrl("https://nexus.redmadrobot.com/repository/android/")
        credentials(PasswordCredentials::class)
    }
}
