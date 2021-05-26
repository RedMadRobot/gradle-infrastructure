package com.redmadrobot.build.internal

import com.redmadrobot.build.extension.TestOptions
import org.gradle.api.tasks.testing.Test

public fun Test.setTestOptions(testOptions: TestOptions) {
    if (testOptions.useJunitPlatform) {
        useJUnitPlatform {
            excludeEngines = testOptions.jUnitPlatformOptions.excludeEngines
            includeEngines = testOptions.jUnitPlatformOptions.includeEngines
            excludeTags = testOptions.jUnitPlatformOptions.excludeTags
            includeTags = testOptions.jUnitPlatformOptions.includeTags
        }
        testLogging { events("passed", "skipped", "failed") }
    } else {
        useJUnit()
    }
}
