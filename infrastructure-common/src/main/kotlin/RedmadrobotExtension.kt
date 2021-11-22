package com.redmadrobot.build

import org.gradle.api.JavaVersion
import org.gradle.api.provider.Property

/** Extension used as a namespace for all infrastructure plugins' extensions. */
public interface RedmadrobotExtension : StaticAnalyzerSpec {

    /**
     * JVM version to be used as a target by Kotlin and Java compilers.
     * By default, used 1.8.
     */
    public val jvmTarget: Property<JavaVersion>

    public companion object {
        /** Extension name. */
        public const val NAME: String = "redmadrobot"
    }
}
