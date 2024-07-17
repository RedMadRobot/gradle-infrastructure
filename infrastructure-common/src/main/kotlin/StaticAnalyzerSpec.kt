package com.redmadrobot.build

import org.gradle.api.file.DirectoryProperty

/** Options used for static analyzers' configurations. */
public interface StaticAnalyzerSpec {

    /** Directory with configs for static analyzers and other tools. */
    public val configsDir: DirectoryProperty

    /** Directory with reports of static analyzers and other tools. */
    public val reportsDir: DirectoryProperty

    public companion object {
        // Relative to root project directory.
        internal const val DEFAULT_CONFIGS_DIR = "config/"

        // Relative to root project build directory.
        internal const val DEFAULT_REPORTS_DIR = "reports/"
    }
}
