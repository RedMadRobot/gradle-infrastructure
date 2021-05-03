package com.redmadrobot.build

/** Debug build type name. */
public const val BUILD_TYPE_DEBUG: String = "debug"

/** Release build type name. */
public const val BUILD_TYPE_RELEASE: String = "release"

/** QA build type name. */
public val BUILD_TYPE_QA: String = "qa"

/** Superseded with [BUILD_TYPE_QA]. */
@Deprecated(
    message = "Use BUILD_TYPE_QA instead. You can configure QA build type name to keep backward compatibility.",
    replaceWith = ReplaceWith("BUILD_TYPE_QA"),
)
public val BUILD_TYPE_STAGING: String get() = BUILD_TYPE_QA
