import com.redmadrobot.build.extension.*

plugins {
    id("redmadrobot.root-project") version "0.9"
    id("com.github.ben-manes.versions") version "0.38.0"
    `maven-publish`
    `kotlin-dsl` apply false
}

apply(plugin = "redmadrobot.detekt")

redmadrobot {
    kotlinVersion = "1.5.10"

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

subprojects {
    apply {
        plugin("org.gradle.kotlin.kotlin-dsl")
        plugin("redmadrobot.kotlin-library")
        plugin("redmadrobot.publish")
    }

    group = "com.redmadrobot.build"
    version = "0.10-SNAPSHOT"

    publishing {
        repositories {
            if (isRunningOnCi) githubPackages("RedMadRobot/gradle-infrastructure")
            if (isReleaseVersion && credentialsExist("ossrh")) ossrh()
        }
    }
}
