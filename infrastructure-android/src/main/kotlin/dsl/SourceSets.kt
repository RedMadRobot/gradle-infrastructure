package com.redmadrobot.build.dsl

import com.android.build.api.dsl.AndroidSourceSet
import com.redmadrobot.build.android.internal.CommonExtension
import org.gradle.api.NamedDomainObjectContainer

/** Use `sourceSets.addSharedSourceSetRoot` instead. */
@Deprecated(
    "Use sourceSets.addSharedSourceSetRoot(...) instead.",
    ReplaceWith("this.sourceSets.addSharedSourceSetRoot(variant1, variant2, name)")
)
public fun CommonExtension.addSharedSourceSetRoot(
    variant1: String,
    variant2: String,
    name: String = "$variant1${variant2.replaceFirstChar { it.uppercaseChar() }}",
) {
    sourceSets.addSharedSourceSetRoot(variant1, variant2, name)
}

/**
 * Adds source set root with the given [name], shared between [variant1] and [variant2] source sets.
 * By default [name] is a combination of `variant1` and `variant2` names.
 * ```
 *  android {
 *      // Here we need to share sources between debug and QA builds
 *      sourceSets.addSharedSourceSetRoot(BUILD_TYPE_DEBUG, BUILD_TYPE_QA)
 *
 *      // We can specify name for the source set root if need
 *      sourceSets.addSharedSourceSetRoot(BUILD_TYPE_DEBUG, BUILD_TYPE_QA, name = "debugPanel")
 *  }
 * ```
 */
public fun NamedDomainObjectContainer<out AndroidSourceSet>.addSharedSourceSetRoot(
    variant1: String,
    variant2: String,
    name: String = "$variant1${variant2.replaceFirstChar { it.uppercaseChar() }}",
) {
    val root = "src/$name"
    getByName(variant1).addRoot(root)
    getByName(variant2).addRoot(root)
}

/** Works similar to [AndroidSourceSet.setRoot], but adds the new [path] to the existing roots. */
private fun AndroidSourceSet.addRoot(path: String) {
    java.directories.add("$path/java")
    kotlin.directories.add("$path/java")
    kotlin.directories.add("$path/kotlin")
    resources.directories.add("$path/resources")
    res.directories.add("$path/res")
    assets.directories.add("$path/assets")
    manifest.srcFile("$path/AndroidManifest.xml")
    aidl.directories.add("$path/aidl")
    renderscript.directories.add("$path/rs")
    jniLibs.directories.add("$path/jniLibs")
    shaders.directories.add("$path/shaders")
    mlModels.directories.add("$path/ml")
}
