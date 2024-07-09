package com.redmadrobot.build

import org.gradle.api.JavaVersion
import org.gradle.api.provider.Property

/** Extension used as a namespace for all infrastructure plugins' extensions. */
public interface RedmadrobotExtension : StaticAnalyzerSpec {

    /**
     * JVM version to be used as a target by Kotlin and Java compilers.
     * Use [JVM Toolchains](https://kotl.in/gradle/jvm/toolchain) instead.
     */
    @Deprecated(
        "Use JVM Toolchains instead. See https://kotl.in/gradle/jvm/toolchain",
        level = DeprecationLevel.ERROR
    )
    public val jvmTarget: Property<JavaVersion>

    public companion object {
        /** Extension name. */
        public const val NAME: String = "redmadrobot"
    }
}
