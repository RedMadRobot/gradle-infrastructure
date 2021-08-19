package com.redmadrobot.build.internal

import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFile

internal fun DirectoryProperty.getFileIfExists(path: String): RegularFile? {
    return get().file(path).takeIf { it.asFile.exists() }
}
