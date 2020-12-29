package com.redmadrobot.build.extension

import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.api.artifacts.repositories.MavenArtifactRepository
import org.gradle.api.artifacts.repositories.PasswordCredentials
import org.gradle.kotlin.dsl.credentials
import org.gradle.kotlin.dsl.maven

/**
 * Adds RMR Nexus repo with name "rmrNexus".
 * Credentials should be provided through project properties `rmrNexusUsername` and `rmrNexusPassword`.
 *
 * See: https://docs.gradle.org/current/userguide/declaring_repositories.html#sec:handling_credentials
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
        apply(configure)
    }
}

/**
 * Adds GitHub Packages repository for the specified [repoName].
 * Credentials should be provided through project properties `githubPackagesUsername` and  `githubPackagesPassword`.
 *
 * See: https://docs.gradle.org/current/userguide/declaring_repositories.html#sec:handling_credentials
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
    configure: MavenArtifactRepository.() -> Unit = {}
): MavenArtifactRepository {
    return maven("https://maven.pkg.github.com/$repoName") {
        name = "githubPackages"
        credentials(PasswordCredentials::class)
        apply(configure)
    }
}

/**
 * Adds RMR Bintray repository to .
 *
 * Example:
 * ```
 * repositories {
 *     rmrBintray()
 * }
 * ```
 */
public fun RepositoryHandler.rmrBintray(configure: MavenArtifactRepository.() -> Unit = {}): MavenArtifactRepository {
    return maven("https://dl.bintray.com/redmadrobot-opensource/android", configure)
}

/**
 * Adds RMR Bintray repository for the specified [packageName].
 * Credentials should be provided through project properties `bintrayUsername` and  `bintrayPassword`.
 *
 * See: https://docs.gradle.org/current/userguide/declaring_repositories.html#sec:handling_credentials
 */
public fun RepositoryHandler.rmrBintray(
    packageName: String,
    publish: Boolean = false,
    configure: MavenArtifactRepository.() -> Unit = {}
): MavenArtifactRepository {
    val params = ";publish=${if (publish) 1 else 0}"
    return maven("https://api.bintray.com/maven/redmadrobot-opensource/android/$packageName/$params") {
        name = "bintray"
        credentials(PasswordCredentials::class)
        apply(configure)
    }
}
