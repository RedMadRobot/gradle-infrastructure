package com.redmadrobot.build.internal

import com.redmadrobot.build.extension.TestOptions
import com.redmadrobot.build.extension.TestOptionsImpl
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
