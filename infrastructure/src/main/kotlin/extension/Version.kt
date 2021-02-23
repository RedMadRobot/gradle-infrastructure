package com.redmadrobot.build.extension

import org.gradle.api.Project

/** Returns `true` if version contains `-SNAPSHOT` suffix. Opposites to [isReleaseVersion]. */
public val Project.isSnapshotVersion: Boolean
    get() = version.toString().endsWith("-SNAPSHOT")

/** Returns `true` if version is not snapshot. Opposites to [isSnapshotVersion]. */
public val Project.isReleaseVersion: Boolean
    get() = !isSnapshotVersion
