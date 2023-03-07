import com.redmadrobot.build.dsl.*
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion

plugins {
    id("com.redmadrobot.kotlin-library")
    id("com.redmadrobot.publish")
}

// Keep gradle-infrastructure compatible with older versions of Gradle.
kotlinCompile {
    compilerOptions {
        apiVersion.set(KotlinVersion.KOTLIN_1_4)
        languageVersion.set(KotlinVersion.KOTLIN_1_4)
        freeCompilerArgs.add("-Xuse-ir") // Needed as long as languageVersion is less than 1.5
    }
}

publishing {
    repositories {
        if (isRunningOnCi) githubPackages("RedMadRobot/gradle-infrastructure")
        if (isReleaseVersion && credentialsExist("ossrh")) ossrh()
    }
}

// Don't publish markers to OSSRH repository
afterEvaluate {
    tasks.withType<PublishToMavenRepository>()
        .matching { task -> task.repository.name == "ossrh" && "PluginMarker" in task.name }
        .configureEach { enabled = false }
}
