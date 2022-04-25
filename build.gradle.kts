import com.redmadrobot.build.dsl.developer
import com.redmadrobot.build.dsl.isRunningOnCi
import com.redmadrobot.build.dsl.mit
import com.redmadrobot.build.dsl.setGitHubProject

plugins {
    id("com.redmadrobot.detekt")
    id("com.redmadrobot.publish-config")
    id("com.github.ben-manes.versions") version "0.39.0"
    `kotlin-dsl` apply false
}

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
        plugin("io.gitlab.arturbosch.detekt")
    }

    group = "com.redmadrobot.build"
    version = "0.16.1-SNAPSHOT"
}
