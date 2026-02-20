import com.redmadrobot.build.dsl.*

plugins {
    id("com.vanniktech.maven.publish")
    signing
}

publishing {
    repositories {
        if (isRunningOnCi) githubPackages("RedMadRobot/gradle-infrastructure")
    }
}

mavenPublishing {
    publishToMavenCentral(automaticRelease = true)
    signAllPublications()

    coordinates(artifactId = project.name)

    pom {
        name = project.name
        description = project.description

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

// Disable signing when publishing to Maven Local (no signing required)
gradle.taskGraph.whenReady {
    if (allTasks.any { it is PublishToMavenLocal }) {
        tasks.withType<Sign>().configureEach { isEnabled = false }
    }
}

// com.vanniktech.maven.publish uses afterEvaluate
// so we want to change tasks after-after evaluate
afterEvaluate {
    afterEvaluate {
        // Don't publish markers to Maven Central repository
        tasks.withType<PublishToMavenRepository>()
            .matching { task -> task.repository.name == "mavenCentral" && "PluginMarker" in task.name }
            .configureEach { enabled = false }
    }
}
