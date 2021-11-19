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
}

dependencies {
    api(projects.infrastructurePublish)
    api(projects.infrastructureDetekt)
    api(projects.infrastructureKotlin)
}
