import org.jetbrains.kotlin.gradle.dsl.kotlinExtension

// Align JVM target across all modules
kotlinExtension.jvmToolchain(17)
