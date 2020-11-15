package com.redmadrobot.build.extension

import com.android.build.gradle.BaseExtension
import com.redmadrobot.build.BUILD_TYPE_DEBUG
import com.redmadrobot.build.BUILD_TYPE_RELEASE
import com.redmadrobot.build.BUILD_TYPE_STAGING
import org.gradle.kotlin.dsl.get

/**
 * Adds sources from one build type to another build type with syntax `<from> to <to>`.
 * You can use constants [BUILD_TYPE_DEBUG], [BUILD_TYPE_RELEASE] and [BUILD_TYPE_STAGING]
 * for predefined build types.
 *
 * ```
 *  android {
 *      // Here we need to use debug sources in staging builds
 *      mergeSourceSets(BUILD_TYPE_DEBUG to BUILD_TYPE_STAGING)
 *  }
 * ```
 */
fun BaseExtension.mergeSourceSets(mapping: Pair<String, String>) {
    mergeSourceSets(mapping.first, mapping.second)
}

/**
 * Adds sources from build type with name [from] to build type with name [to].
 * You can use constants [BUILD_TYPE_DEBUG], [BUILD_TYPE_RELEASE] and [BUILD_TYPE_STAGING]
 * for predefined build types.
 * ```
 *  android {
 *      // Here we need to use debug sources in staging builds
 *      mergeSourceSets(from = BUILD_TYPE_DEBUG, to = BUILD_TYPE_STAGING)
 *  }
 * ```
 */
fun BaseExtension.mergeSourceSets(from: String, to: String) {
    val fromSourceSet = sourceSets[from].java
    val toSourceSet = sourceSets[to].java
    toSourceSet.setSrcDirs(fromSourceSet.srcDirs + toSourceSet.srcDirs)
}
