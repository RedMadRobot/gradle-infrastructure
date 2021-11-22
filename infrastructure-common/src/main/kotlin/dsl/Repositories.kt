package com.redmadrobot.build.dsl

import org.gradle.api.Project
import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.api.artifacts.repositories.MavenArtifactRepository
import org.gradle.api.artifacts.repositories.PasswordCredentials
import org.gradle.kotlin.dsl.credentials
import org.gradle.kotlin.dsl.maven

/**
 * Checks that credentials for the repository with given [name] are existing.
 *
 * See: [Handling credentials](https://docs.gradle.org/current/userguide/declaring_repositories.html#sec:handling_credentials)
 */
public fun Project.credentialsExist(name: String): Boolean {
    return hasProperty("${name}Username") && hasProperty("${name}Password")
}

/**
 * Adds RMR Nexus repo with name "rmrNexus".
 * Credentials should be provided through project properties `rmrNexusUsername` and `rmrNexusPassword`.
 *
 * See:
 * [Handling Credentials](https://docs.gradle.org/current/userguide/declaring_repositories.html#sec:handling_credentials)
 *
 * The URL used for the repository is `https://nexus.redmadrobot.com/repository/android/`
 *
 * Example:
 * ```
 * repositories {
 *     rmrNexus()
 * }
 * ```
 */
public fun RepositoryHandler.rmrNexus(configure: MavenArtifactRepository.() -> Unit = {}): MavenArtifactRepository {
    return maven("https://nexus.redmadrobot.com/repository/android/") {
        name = "rmrNexus"
        credentials(PasswordCredentials::class)
        configure()
    }
}

/**
 * Adds GitHub Packages repository for the specified [repoName].
 * Credentials should be provided through project properties `githubPackagesUsername` and  `githubPackagesPassword`.
 *
 * See:
 * [Handling Credentials](https://docs.gradle.org/current/userguide/declaring_repositories.html#sec:handling_credentials)
 *
 * Example:
 * ```
 * repositories {
 *     githubPackages("RedMadRobot/gradle-infrastructure")
 * }
 * ```
 */
public fun RepositoryHandler.githubPackages(
    repoName: String,
    configure: MavenArtifactRepository.() -> Unit = {},
): MavenArtifactRepository {
    return maven("https://maven.pkg.github.com/$repoName") {
        name = "githubPackages"
        credentials(PasswordCredentials::class)
        configure()
    }
}
