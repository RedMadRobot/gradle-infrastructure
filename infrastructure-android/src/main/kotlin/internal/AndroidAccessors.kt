package com.redmadrobot.build.internal

import com.android.build.gradle.BaseExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByName

internal val Project.android: BaseExtension get() = extensions.getByName<BaseExtension>("android")

internal fun <T : BaseExtension> Project.android(configure: T.() -> Unit) {
    extensions.configure("android", configure)
}
