import com.redmadrobot.build.extension.developer
import com.redmadrobot.build.extension.isRunningOnCi
import com.redmadrobot.build.extension.mit
import com.redmadrobot.build.extension.setGitHubProject

plugins {
    id("redmadrobot.root-project") version "0.8"
    id("com.github.ben-manes.versions") version "0.36.0"
}

apply(plugin = "redmadrobot.detekt")

repositories {
    jcenter() // TODO #36: Remove JCenter when detekt-formatting will be moved to Maven Central
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
