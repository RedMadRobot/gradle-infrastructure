import com.redmadrobot.build.dsl.*

plugins {
    id("redmadrobot.root-project") version "0.12.1"
    id("com.github.ben-manes.versions") version "0.39.0"
    `maven-publish`
    `kotlin-dsl` apply false
}

apply(plugin = "redmadrobot.detekt")

redmadrobot {
    publishing {
        signArtifacts.set(!isRunningOnCi)

        pom {
            setGitHubProject("RedMadRobot/gradle-infrastructure")

            licenses {
                mit()
            }

            developers {
                developer(id = "osipxd", name = "Osip Fatkullin", email = "o.fatkullin@redmadrobot.com")
                developer(id = "rwqwr", name = "Roman Ivanov", email = "r.ivanov@redmadrobot.com")
            }
        }
    }
}

subprojects {
    apply {
        plugin("org.gradle.kotlin.kotlin-dsl")
        plugin("redmadrobot.kotlin-library")
        plugin("redmadrobot.publish")
        plugin("redmadrobot.detekt")
    }

    group = "com.redmadrobot.build"
    version = "0.12.2"

    // Keep gradle-infrastructure compatible with older versions of Gradle.
    kotlinCompile {
        kotlinOptions {
            apiVersion = "1.4"
            languageVersion = "1.4"
        }
    }

    publishing {
        repositories {
            if (isRunningOnCi) githubPackages("RedMadRobot/gradle-infrastructure")
            if (isReleaseVersion && credentialsExist("ossrh")) ossrh()
        }
    }
}
