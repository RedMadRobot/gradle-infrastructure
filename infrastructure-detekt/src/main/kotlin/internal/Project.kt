package com.redmadrobot.build.detekt.internal

import org.gradle.api.Project

internal val Project.hasKotlinPlugin: Boolean
    get() = extensions.findByName("kotlin") != null
