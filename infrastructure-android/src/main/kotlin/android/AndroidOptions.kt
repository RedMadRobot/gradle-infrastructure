package com.redmadrobot.build.android

import org.gradle.api.provider.Property
import org.gradle.api.tasks.TaskProvider

/** Options for android projects. */
public interface AndroidOptions {

    /** Minimal Android SDK to be used in all android modules. */
    public val minSdk: Property<Int>

    /** Target Android SDK to be used in all android modules. */
    public val targetSdk: Property<Int>

    /**
     * Compile Android SDK to be used in all android modules.
     * It can be version number ("33") or version code ("T").
     * Uses [targetSdk] as compile SDK if not configured.
     */
    public val compileSdk: Property<String>

    /**
     * Build Tools version to be used in all android modules.
     *
     * By default, uses the version from environment variable `ANDROID_BUILD_TOOLS_VERSION`,
     * or default version for current Android Gradle Plugin if the variable is not present.
     */
    public val buildToolsVersion: Property<String>

    /**
     * NDK to be used in all android modules.
     * By default, uses the version from the environment variable `ANDROID_NDK_VERSION` if it is set.
     */
    public val ndkVersion: Property<String>

    /**
     * Filters test tasks that should be run on ':test'.
     * It is useful if you don't want to run tests for all build variants when run 'test' task.
     *
     * Filter should return `true` if the task should be executed of `false`, otherwise.
     * By default, it returns `true` only for release unit tests.
     */
    public val testTasksFilter: Property<(TaskProvider<*>) -> Boolean>

    public companion object
}
