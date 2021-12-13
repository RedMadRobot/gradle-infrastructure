package com.redmadrobot.build.dsl

import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.api.artifacts.repositories.MavenArtifactRepository
import org.gradle.api.artifacts.repositories.PasswordCredentials
import org.gradle.kotlin.dsl.credentials
import org.gradle.kotlin.dsl.maven

/**
 * Adds Sonatype OSSRH repository with name "ossrh" and the specified [host].
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
public fun RepositoryHandler.ossrh(
    host: OssrhHost = OssrhHost.S01,
    configure: MavenArtifactRepository.() -> Unit = {},
): MavenArtifactRepository {
    return maven("${host.value}/service/local/staging/deploy/maven2/") {
        name = "ossrh"
        credentials(PasswordCredentials::class)
        configure()
    }
}

/**
 * Adds Sonatype OSSRH Snapshots repository with name "ossrhSnapshots" and the specified [host].
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
    host: OssrhHost = OssrhHost.S01,
    configure: MavenArtifactRepository.() -> Unit = {},
): MavenArtifactRepository {
    return maven("${host.value}/content/repositories/snapshots/") {
        name = "ossrhSnapshots"
        configure()
    }
}

/** OSSRH hosts. */
public enum class OssrhHost(public val value: String) {

    /** Host used since February 2021. */
    S01("https://s01.oss.sonatype.org"),

    /** Legacy host used before February 2021. */
    LEGACY("https://oss.sonatype.org"),
}
