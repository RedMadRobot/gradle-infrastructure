description = "Small plugins to reduce boilerplate in Gradle build scripts."

gradlePlugin {
    plugins {
        isAutomatedPublishing = false
        register("kotlin-library") {
            id = "redmadrobot.kotlin-library"
            implementationClass = "com.redmadrobot.build.KotlinLibraryPlugin"
        }
    }
}

repositories {
    mavenCentral()
    google()
}

dependencies {
    api(projects.infrastructureBase)
    api(kotlin("gradle-plugin", version = libs.versions.kotlin.get()))
    compileOnly(libs.androidGradle)
    implementation(libs.detektGradle)
    implementation(libs.jgit)
}
