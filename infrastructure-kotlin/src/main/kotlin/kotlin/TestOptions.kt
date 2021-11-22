package com.redmadrobot.build.kotlin

import org.gradle.api.provider.Provider
import org.gradle.api.tasks.testing.junit.JUnitOptions
import org.gradle.api.tasks.testing.junitplatform.JUnitPlatformOptions

public interface TestOptions {

    /** Flag for using Junit Jupiter Platform. Use functions [useJunit] or [useJunitPlatform]. */
    public val useJunitPlatform: Provider<Boolean>

    /** Specifies that JUnit Platform (JUnit 5) should be used to execute the tests. */
    public fun useJunitPlatform(configure: JUnitPlatformOptions.() -> Unit = {})

    /** Specifies that JUnit should be used to execute the tests. */
    public fun useJunit(configure: JUnitOptions.() -> Unit = {})
}
