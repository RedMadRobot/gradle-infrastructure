package com.redmadrobot.build.dsl

import com.redmadrobot.build.kotlin.internal.findStringProperty
import org.gradle.api.Project

/** Debug build type name. */
public const val BUILD_TYPE_DEBUG: String = "debug"

/** Release build type name. */
public const val BUILD_TYPE_RELEASE: String = "release"

/**
 * QA build type name.
 * You can change this value via project property `redmadrobot.android.build.type.qa`.
 */
public var BUILD_TYPE_QA: String = "qa"
    private set

private var qaBuildTypeFinalized: Boolean = false

internal fun Project.finalizeQaBuildType() {
    if (qaBuildTypeFinalized) return
    BUILD_TYPE_QA = findStringProperty("redmadrobot.android.build.type.qa") ?: BUILD_TYPE_QA
    qaBuildTypeFinalized = true
}
