package com.redmadrobot.build.extension

import org.gradle.api.Project
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.model.ObjectFactory
import org.gradle.api.tasks.testing.junitplatform.JUnitPlatformOptions
import org.gradle.kotlin.dsl.getByType

public open class RedmadrobotExtension(objects: ObjectFactory) {

    public companion object {
        public const val NAME: String = "redmadrobot"

        // Relative to root project directory.
        internal const val DEFAULT_CONFIGS_DIR = "config/"

        // Relative to root project build directory.
        internal const val DEFAULT_REPORTS_DIR = "reports/"
    }

    /** Kotlin version that should be used for all projects. */
    public var kotlinVersion: String = "1.4.20"

    /** Directory where stored configs for static analyzers. */
    public val configsDir: DirectoryProperty = objects.directoryProperty()

    /** Directory where will be stored static analyzers reports. */
    public val reportsDir: DirectoryProperty = objects.directoryProperty()

    /** Settings for android modules. */
    public val android: AndroidOptions = AndroidOptions()

    /** Settings for JVM test task */
    public val test: TestOptions = TestOptions()

    public fun android(configure: AndroidOptions.() -> Unit) {
        android.run(configure)
    }

    public fun test(configure: TestOptions.() -> Unit) {
        test.run(configure)
    }
}

public class AndroidOptions {

    /** Minimal Android SDK that will be applied to all android modules. */
    public var minSdk: Int = 21

    /** Target Android SDK that will be applied to all android modules. */
    public var targetSdk: Int = 30

    /** Settings for Android test task */
    public val test: TestOptions = TestOptions()

    public fun test(configure: TestOptions.() -> Unit) {
        test.run(configure)
    }
}

public class TestOptions {

    /** Flag for using Junit Jupiter Platform */
    internal var useJunitPlatform: Boolean = true
        private set

    /** Options for JUnit Platform */
    internal val jUnitPlatformOptions by lazy { JUnitPlatformOptions() }

    public fun useJunitPlatform(testFrameworkConfigure: JUnitPlatformOptions.() -> Unit = {}) {
        useJunitPlatform = true
        testFrameworkConfigure.invoke(jUnitPlatformOptions)
    }

    public fun useJunit() {
        useJunitPlatform = false
    }
}

internal val Project.redmadrobotExtension: RedmadrobotExtension
    get() = rootProject.extensions.getByType()