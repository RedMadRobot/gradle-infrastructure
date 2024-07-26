import com.redmadrobot.build.dsl.*
import com.vanniktech.maven.publish.SonatypeHost

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
    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL, automaticRelease = true)
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
