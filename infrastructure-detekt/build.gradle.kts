description = "Plugin to make publication as simple as possible."

gradlePlugin {
    plugins {
        isAutomatedPublishing = false
        register("detekt") {
            id = "redmadrobot.detekt"
            implementationClass = "com.redmadrobot.build.detekt.DetektPlugin"
        }
    }
}

repositories {
    mavenCentral()
    google()
}

dependencies {
    api(projects.infrastructureBase)

    implementation(libs.detektGradle)
    implementation(libs.jgit)
    compileOnly(libs.androidGradle)
}