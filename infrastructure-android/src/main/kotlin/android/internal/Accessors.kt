@file:Suppress("UnstableApiUsage") // We want to use new APIs

package com.redmadrobot.build.android.internal

import com.android.build.api.variant.AndroidComponentsExtension
import com.redmadrobot.build.android.AndroidOptions
import com.redmadrobot.build.kotlin.TestOptions
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionAware
import com.android.build.api.dsl.CommonExtension as ParameterizedCommonExtension

internal typealias CommonExtension = ParameterizedCommonExtension

@JvmName("androidCommon")
internal fun Project.android(configure: CommonExtension.() -> Unit) {
    android<CommonExtension>(configure)
}

internal fun <T : CommonExtension> Project.android(configure: T.() -> Unit) {
    extensions.configure("android", configure)
}

@JvmName("androidComponentsCommon")
internal fun Project.androidComponents(configure: AndroidComponentsExtension<*, *, *>.() -> Unit) {
    androidComponents<AndroidComponentsExtension<*, *, *>>(configure)
}

internal fun <T : AndroidComponentsExtension<*, *, *>> Project.androidComponents(configure: T.() -> Unit) {
    extensions.configure("androidComponents", configure)
}

internal val AndroidOptions.test: TestOptions
    get() = (this as ExtensionAware).extensions.getByName("test") as TestOptions
