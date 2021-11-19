package com.redmadrobot.build.kotlin.internal

import com.redmadrobot.build.kotlin.TestOptions
import com.redmadrobot.build.kotlin.TestOptionsImpl
import org.gradle.api.tasks.testing.Test

public fun Test.setTestOptions(testOptions: TestOptions) {
    val configure = (testOptions as TestOptionsImpl).configuration.get()
    if (testOptions.useJunitPlatform.get()) {
        useJUnitPlatform(configure)
        testLogging { events("passed", "skipped", "failed") }
    } else {
        useJUnit(configure)
    }
}
