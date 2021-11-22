description = "Small plugins to reduce boilerplate in Gradle build scripts."

gradlePlugin {
    plugins {
        register("root-project") {
            id = "redmadrobot.root-project"
            implementationClass = "com.redmadrobot.build.RootProjectPlugin"
        }
    }
}

repositories {
    mavenCentral()
    google()
    gradlePluginPortal()
}

dependencies {
    api(projects.infrastructurePublish)
    api(projects.infrastructureAndroid)
    api(projects.infrastructureKotlin)
}
