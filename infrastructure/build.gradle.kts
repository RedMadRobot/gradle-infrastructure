description = "Small plugins to reduce boilerplate in Gradle build scripts."

gradlePlugin {
    plugins {
        register("root-project") {
            id = "redmadrobot.root-project"
            implementationClass = "com.redmadrobot.build.RootProjectPlugin"
        }
        register("kotlin-library") {
            id = "redmadrobot.kotlin-library"
            implementationClass = "com.redmadrobot.build.KotlinLibraryPlugin"
        }
        register("publish") {
            id = "redmadrobot.publish"
            implementationClass = "com.redmadrobot.build.PublishPlugin"
        }
        register("detekt") {
            id = "redmadrobot.detekt"
            implementationClass = "com.redmadrobot.build.DetektPlugin"
        }
    }
}

repositories {
    mavenCentral()
}

dependencies {
    api(kotlin("gradle-plugin", version = "1.5.10"))
    implementation("io.gitlab.arturbosch.detekt:detekt-gradle-plugin:1.17.1")
    implementation("org.eclipse.jgit:org.eclipse.jgit:5.11.0.202103091610-r")
}
