package com.redmadrobot.build.extension

import org.gradle.api.provider.Property
import org.gradle.api.tasks.testing.TestFrameworkOptions
import org.gradle.api.tasks.testing.junit.JUnitOptions
import org.gradle.api.tasks.testing.junitplatform.JUnitPlatformOptions

public interface TestOptionsSpec {

    /** Settings for JVM test task. */
    public val test: TestOptions

    /** Settings for JVM test task. */
    public fun test(configure: TestOptions.() -> Unit)
}

public abstract class TestOptions {

    /** Flag for using Junit Jupiter Platform. */
    internal abstract val useJunitPlatform: Property<Boolean>

    /** Configurator for Test Framework. */
    internal abstract val configuration: Property<TestFrameworkOptions.() -> Unit>

    /** Specifies that JUnit Platform (JUnit 5) should be used to execute the tests. */
    public fun useJunitPlatform(configure: JUnitPlatformOptions.() -> Unit = {}) {
        useJunitPlatform.set(true)
        configuration.set { (this as JUnitPlatformOptions).configure() }
    }

    /** Specifies that JUnit should be used to execute the tests. */
    public fun useJunit(configure: JUnitOptions.() -> Unit = {}) {
        useJunitPlatform.set(false)
        configuration.set { (this as JUnitOptions).configure() }
    }

    init {
        useJunitPlatform
            .convention(true)
            .finalizeValueOnRead()
        configuration
            .convention { /* no-op */ }
            .finalizeValueOnRead()
    }
}
