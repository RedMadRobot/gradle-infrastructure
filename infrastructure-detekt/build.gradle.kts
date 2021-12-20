plugins {
    id("gradle-plugin-commons")
}

description = "Additional functionality for detekt"

gradlePlugin {
    plugins {
        register("detekt") {
            id = "com.redmadrobot.detekt"
            displayName = "Infrastructure Detekt Plugin"
            description = project.description
            implementationClass = "com.redmadrobot.build.detekt.DetektPlugin"
        }
    }
}

repositories {
    mavenCentral()
    google()
}

dependencies {
    api(projects.infrastructureCommon)

    implementation(libs.detektGradle)
    implementation(libs.jgit)
    compileOnly(libs.androidTools.gradle)
}
