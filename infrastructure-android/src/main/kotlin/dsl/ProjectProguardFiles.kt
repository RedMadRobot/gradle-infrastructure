package com.redmadrobot.build.dsl

import org.gradle.api.Project
import java.io.File

/** Collects proguard rules from the specified [path]. */
public fun Project.collectProguardFiles(path: String = "proguard"): List<File> {
    return fileTree(path)
        .files
        .filter { it.extension == "pro" }
}
