import com.redmadrobot.build.extension.*

plugins {
    id("redmadrobot.root-project") version "0.8.2"
    id("com.github.ben-manes.versions") version "0.38.0"
    `maven-publish`
}

apply(plugin = "redmadrobot.detekt")

subprojects {
    apply {
        plugin("redmadrobot.kotlin-library")
        plugin("redmadrobot.publish")
    }

    group = "com.redmadrobot.build"
    version = "0.9-SNAPSHOT"

    publishing {
        repositories {
            if (isRunningOnCi) githubPackages("RedMadRobot/gradle-infrastructure")
            if (isReleaseVersion && credentialsExist("ossrh")) ossrh()
        }
    }
}

redmadrobot {
    publishing {
        signArtifacts = !isRunningOnCi

        pom {
            setGitHubProject("RedMadRobot/gradle-infrastructure")

            licenses {
                mit()
            }

            developers {
                developer(id = "osipxd", name = "Osip Fatkullin", email = "o.fatkullin@redmadrobot.com")
            }
        }
    }
}
