plugins {
    id("redmadrobot.root-project") version "0.7"
    id("com.github.ben-manes.versions") version "0.36.0"
}

apply(plugin = "redmadrobot.detekt")

redmadrobot {
    publishing.signArtifacts = true
}
