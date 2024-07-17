package com.redmadrobot.build.kotlin

import org.gradle.api.provider.Provider
import org.gradle.api.tasks.testing.junit.JUnitOptions
import org.gradle.api.tasks.testing.junitplatform.JUnitPlatformOptions

/** Options used to configure tests. */
public interface TestOptions {

    /**
     * Flag for using Junit Jupiter Platform.
     * Use functions [useJunitPlatform] and [useJunit] to change it.
     */
    public val useJunitPlatform: Provider<Boolean>

    /** Specifies that JUnit Platform (JUnit 5) should be used to execute tests. */
    public fun useJunitPlatform(configure: JUnitPlatformOptions.() -> Unit = {})

    /** Specifies that JUnit 4 should be used to execute tests. */
    public fun useJunit(configure: JUnitOptions.() -> Unit = {})
}
