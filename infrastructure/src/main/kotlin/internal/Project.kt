package com.redmadrobot.build.internal

import org.gradle.api.Project

internal val Project.isRoot: Boolean
    get() = this == rootProject
