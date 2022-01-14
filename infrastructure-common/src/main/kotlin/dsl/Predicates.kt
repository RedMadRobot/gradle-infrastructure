package com.redmadrobot.build.dsl

import org.gradle.api.Project

/** Returns `true` if build is running on CI. */
public val Project.isRunningOnCi: Boolean
    get() = providers.environmentVariable("CI").forUseAtConfigurationTime().orNull == "true"
