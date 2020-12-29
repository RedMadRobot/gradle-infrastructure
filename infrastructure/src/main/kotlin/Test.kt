package com.redmadrobot.build

import com.redmadrobot.build.extension.TestOptions
import org.gradle.api.tasks.testing.Test

internal fun Test.configure(testOptions: TestOptions) {
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
