import com.redmadrobot.build.dsl.*
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion

plugins {
    id("com.redmadrobot.kotlin-library")
    id("com.redmadrobot.publish")
}

publishing {
    repositories {
        if (isRunningOnCi) githubPackages("RedMadRobot/gradle-infrastructure")
        if (isReleaseVersion && credentialsExist("ossrh")) ossrh()
    }
}

kotlin {
    // Keep gradle-infrastructure compatible with older versions of Gradle.
    // Language version should be in sync with the one used in Gradle
    // https://docs.gradle.org/current/userguide/compatibility.html#kotlin
    compilerOptions {
        apiVersion = KotlinVersion.KOTLIN_1_8
        languageVersion = KotlinVersion.KOTLIN_1_8
    }
}

afterEvaluate {
    // Don't publish markers to OSSRH repository
    tasks.withType<PublishToMavenRepository>()
        .matching { task -> task.repository.name == "ossrh" && "PluginMarker" in task.name }
        .configureEach { enabled = false }
}
