package com.redmadrobot.build.dsl

import com.android.build.api.dsl.AndroidSourceSet
import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Incubating
import org.gradle.kotlin.dsl.get

/**
 * Adds source set root with the given [name], shared between [variant1] and [variant2] source sets.
 * By default [name] is a combination of `variant1` and `variant2` names.
 * ```
 *  android {
 *      // Here we need to share sources between debug and QA builds
 *      addSharedSourceSetRoot(BUILD_TYPE_DEBUG, BUILD_TYPE_QA)
 *
 *      // We can specify name for the source set root if need
 *      addSharedSourceSetRoot(BUILD_TYPE_DEBUG, BUILD_TYPE_QA, name = "debugPanel")
 *  }
 * ```
 */
@Incubating
public fun CommonExtension<*, *, *, *>.addSharedSourceSetRoot(
    variant1: String,
    variant2: String,
    name: String = "$variant1${variant2.capitalize()}",
) {
    val variant1SourceSets = sourceSets[variant1]
    val variant2SourceSets = sourceSets[variant2]
    val root = "src/$name"

    variant1SourceSets.addRoot(root)
    variant2SourceSets.addRoot(root)
}

@Suppress("UnstableApiUsage")
private fun AndroidSourceSet.addRoot(path: String) {
    java.srcDirs("$path/java")
    kotlin.srcDirs("$path/java", "$path/kotlin")
    resources.srcDir("$path/resources")
    res.srcDir("$path/res")
    assets.srcDir("$path/assets")
    manifest.srcFile("$path/AndroidManifest.xml")
    aidl.srcDir("$path/aidl")
    renderscript.srcDir("$path/rs")
    jniLibs.srcDir("$path/jniLibs")
    shaders.srcDir("$path/shaders")
    mlModels.srcDir("$path/ml")
}
