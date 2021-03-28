import com.redmadrobot.build.extension.developer
import com.redmadrobot.build.extension.isRunningOnCi
import com.redmadrobot.build.extension.mit
import com.redmadrobot.build.extension.setGitHubProject

plugins {
    id("redmadrobot.root-project") version "0.8.1"
    id("com.github.ben-manes.versions") version "0.38.0"
}

apply(plugin = "redmadrobot.detekt")

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
