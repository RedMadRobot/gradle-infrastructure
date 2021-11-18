package com.redmadrobot.build

import com.redmadrobot.build.extension.RedmadrobotExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * This plugin adds extension [RedmadrobotExtension] to the project.
 * Can be applied only to the root project.
 *
 * Tied to `redmadrobot.root-project` plugin ID.
 *
 * @see RedmadrobotExtension
 */
public class RootProjectPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        // FIXME: Add backward compatible implementation
    }
}
