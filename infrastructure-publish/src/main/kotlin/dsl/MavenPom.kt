package com.redmadrobot.build.dsl

import org.gradle.api.publish.maven.*

/** Adds [MIT License](https://opensource.org/licenses/mit-license.php) to publication. */
public fun MavenPomLicenseSpec.mit() {
    license {
        name.set("MIT License")
        url.set("https://opensource.org/licenses/mit-license.php")
    }
}

private const val GITHUB_DOMAIN = "github.com"

/**
 * Configures `<url>`, `<issueManagement>` and `<scm>` with the given GitHub [projectName].
 * ```
 * pom {
 *     setGitHubProject("RedMadRobot/gradle-infrastructure")
 * }
 * ```
 */
public fun MavenPom.setGitHubProject(projectName: String) {
    val gitHubUrl = "https://$GITHUB_DOMAIN/$projectName"

    url.set(gitHubUrl)

    issueManagement {
        url.set("$gitHubUrl/issues")
        system.set("GitHub Issues")
    }

    scm {
        url.set(gitHubUrl)
        connection.set("scm:git:git://$GITHUB_DOMAIN/$projectName.git")
        developerConnection.set("scm:git:ssh://git@$GITHUB_DOMAIN:$projectName.git")
    }
}

/** Adds developer to publication. */
public fun MavenPomDeveloperSpec.developer(
    id: String,
    name: String,
    email: String,
    action: MavenPomDeveloper.() -> Unit = {},
) {
    developer {
        this.id.set(id)
        this.name.set(name)
        this.email.set(email)
        action()
    }
}

/** Adds contributor to publication. */
public fun MavenPomContributorSpec.contributor(
    name: String,
    email: String,
    action: MavenPomContributor.() -> Unit = {},
) {
    contributor {
        this.name.set(name)
        this.email.set(email)
        action()
    }
}
