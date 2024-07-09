import com.redmadrobot.build.dsl.*

plugins {
    id("com.redmadrobot.detekt")
    id("com.redmadrobot.publish-config")
    alias(libs.plugins.versions)
    `kotlin-dsl` apply false
}

redmadrobot {
    jvmTarget = JavaVersion.VERSION_11

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

    dependencies {
        detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:1.22.0")
    }

    group = "com.redmadrobot.build"
    version = "0.19-SNAPSHOT"
}
