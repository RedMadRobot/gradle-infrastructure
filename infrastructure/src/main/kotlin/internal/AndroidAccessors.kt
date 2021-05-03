package com.redmadrobot.build.internal

import com.android.build.gradle.BaseExtension
import org.gradle.api.Project

internal fun <T : BaseExtension> Project.android(configure: T.() -> Unit) {
    extensions.configure("android", configure)
}
