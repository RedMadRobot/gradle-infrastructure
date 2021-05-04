package com.redmadrobot.build

import com.redmadrobot.build.internal.findStringProperty
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

/** Superseded with [BUILD_TYPE_QA]. */
@Deprecated(
    message = "Use BUILD_TYPE_QA instead. You can configure QA build type name to keep backward compatibility.",
    replaceWith = ReplaceWith("BUILD_TYPE_QA"),
)
public val BUILD_TYPE_STAGING: String get() = BUILD_TYPE_QA
