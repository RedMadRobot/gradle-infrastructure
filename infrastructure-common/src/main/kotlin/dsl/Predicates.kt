package com.redmadrobot.build.dsl

/** Returns `true` if build is running on CI. */
public val isRunningOnCi: Boolean
    get() = System.getenv("CI") == "true"
