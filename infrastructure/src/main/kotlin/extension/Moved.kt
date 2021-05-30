package com.redmadrobot.build.extension

import com.redmadrobot.build.dsl.contributor
import com.redmadrobot.build.dsl.credentialsExist
import com.redmadrobot.build.dsl.developer
import com.redmadrobot.build.dsl.githubPackages
import com.redmadrobot.build.dsl.isReleaseVersion
import com.redmadrobot.build.dsl.isRunningOnCi
import com.redmadrobot.build.dsl.isSnapshotVersion
import com.redmadrobot.build.dsl.mit
import com.redmadrobot.build.dsl.ossrh
import com.redmadrobot.build.dsl.ossrhSnapshots
import com.redmadrobot.build.dsl.rmrNexus
import com.redmadrobot.build.dsl.setGitHubProject
import org.gradle.api.Project
import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.api.artifacts.repositories.MavenArtifactRepository
import org.gradle.api.publish.maven.*

// region Version.kt
@Deprecated(
    "Moved to package com.redmadrobot.build.dsl",
    ReplaceWith("isSnapshotVersion", "com.redmadrobot.build.dsl.isSnapshotVersion"),
)
public val Project.isSnapshotVersion: Boolean by Project::isSnapshotVersion

@Deprecated(
    "Moved to package com.redmadrobot.build.dsl",
    ReplaceWith("isReleaseVersion", "com.redmadrobot.build.dsl.isReleaseVersion"),
)
public val Project.isReleaseVersion: Boolean by Project::isReleaseVersion
// endregion

// region MavenPom.kt
@Deprecated(
    "Moved to package com.redmadrobot.build.dsl",
    ReplaceWith("mit()", "com.redmadrobot.build.dsl.mit"),
)
public fun MavenPomLicenseSpec.mit() {
    mit()
}

@Deprecated(
    "Moved to package com.redmadrobot.build.dsl",
    ReplaceWith("setGitHubProject(projectName)", "com.redmadrobot.build.dsl.setGitHubProject"),
)
public fun MavenPom.setGitHubProject(projectName: String) {
    setGitHubProject(projectName)
}

@Deprecated(
    "Moved to package com.redmadrobot.build.dsl",
    ReplaceWith("developer(id, name, email, action)", "com.redmadrobot.build.dsl.developer"),
)
public fun MavenPomDeveloperSpec.developer(
    id: String,
    name: String,
    email: String,
    action: MavenPomDeveloper.() -> Unit = {},
) {
    developer(id, name, email, action)
}

@Deprecated(
    "Moved to package com.redmadrobot.build.dsl",
    ReplaceWith("contributor(name, email, action)", "com.redmadrobot.build.dsl.contributor"),
)
public fun MavenPomContributorSpec.contributor(
    name: String,
    email: String,
    action: MavenPomContributor.() -> Unit = {},
) {
    contributor(name, email, action)
}
// endregion

// region PublishingPredicates.kt
@Deprecated(
    "Moved to package com.redmadrobot.build.dsl",
    ReplaceWith("isRunningOnCi", "com.redmadrobot.build.dsl.isRunningOnCi"),
)
public val isRunningOnCi: Boolean by ::isRunningOnCi

@Deprecated(
    "Moved to package com.redmadrobot.build.dsl",
    ReplaceWith("credentialsExist(name)", "com.redmadrobot.build.dsl.credentialsExist"),
)
public fun Project.credentialsExist(name: String): Boolean = credentialsExist(name)
// endregion

// region Repositories.kt
@Deprecated(
    "Moved to package com.redmadrobot.build.dsl",
    ReplaceWith("rmrNexus(configure)", "com.redmadrobot.build.dsl.rmrNexus"),
)
public fun RepositoryHandler.rmrNexus(
    configure: MavenArtifactRepository.() -> Unit = {},
): MavenArtifactRepository = rmrNexus(configure)

@Deprecated(
    "Moved to package com.redmadrobot.build.dsl",
    ReplaceWith("githubPackages(repoName, configure)", "com.redmadrobot.build.dsl.githubPackages"),
)
public fun RepositoryHandler.githubPackages(
    repoName: String,
    configure: MavenArtifactRepository.() -> Unit = {},
): MavenArtifactRepository = githubPackages(repoName, configure)

@Deprecated(
    "Moved to package com.redmadrobot.build.dsl",
    ReplaceWith("ossrh(configure)", "com.redmadrobot.build.dsl.ossrh"),
)
public fun RepositoryHandler.ossrh(
    configure: MavenArtifactRepository.() -> Unit = {},
): MavenArtifactRepository = ossrh(configure)

@Deprecated(
    "Moved to package com.redmadrobot.build.dsl",
    ReplaceWith("ossrhSnapshots(configure)", "com.redmadrobot.build.dsl.ossrhSnapshots"),
)
public fun RepositoryHandler.ossrhSnapshots(
    configure: MavenArtifactRepository.() -> Unit = {},
): MavenArtifactRepository = ossrhSnapshots(configure)
// endregion
