package com.redmadrobot.build.dsl

import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.api.artifacts.repositories.MavenArtifactRepository
import org.gradle.api.artifacts.repositories.PasswordCredentials
import org.gradle.kotlin.dsl.credentials
import org.gradle.kotlin.dsl.maven

/**
 * Adds Sonatype OSSRH repository with name "ossrh".
 * Requires credentials to be specified in project properties `ossrhUsername` and `ossrhPassword`.
 *
 * See:
 * [Handling Credentials](https://docs.gradle.org/current/userguide/declaring_repositories.html#sec:handling_credentials)
 * [OSSRH Guide](https://central.sonatype.org/pages/ossrh-guide.html)
 *
 * Example:
 * ```
 * repositories {
 *     ossrh()
 * }
 * ```
 * @see ossrhSnapshots
 */
public fun RepositoryHandler.ossrh(configure: MavenArtifactRepository.() -> Unit = {}): MavenArtifactRepository {
    return maven("https://oss.sonatype.org/service/local/staging/deploy/maven2/") {
        name = "ossrh"
        credentials(PasswordCredentials::class)
        configure()
    }
}

/**
 * Adds Sonatype OSSRH Snapshots repository with name "ossrhSnapshots".
 *
 * Example:
 * ```
 * repositories {
 *     ossrhSnapshots()
 * }
 * ```
 * @see ossrh
 */
public fun RepositoryHandler.ossrhSnapshots(
    configure: MavenArtifactRepository.() -> Unit = {},
): MavenArtifactRepository {
    return maven("https://oss.sonatype.org/content/repositories/snapshots/") {
        name = "ossrhSnapshots"
        configure()
    }
}
