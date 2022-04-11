package com.redmadrobot.build.android.internal

import org.gradle.api.Project
import java.io.File

/** Collects proguard rules from 'proguard' dir. */
internal fun Project.projectProguardFiles(): List<File> {
    return fileTree("proguard")
        .files
        .filter { it.extension == "pro" }
}
