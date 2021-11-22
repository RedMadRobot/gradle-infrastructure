package com.redmadrobot.build

import org.gradle.api.file.DirectoryProperty

public interface StaticAnalyzerSpec {

    /** Directory where stored configs for static analyzers. */
    public val configsDir: DirectoryProperty

    /** Directory where will be stored static analyzers reports. */
    public val reportsDir: DirectoryProperty

    public companion object {
        // Relative to root project directory.
        internal const val DEFAULT_CONFIGS_DIR = "config/"

        // Relative to root project build directory.
        internal const val DEFAULT_REPORTS_DIR = "reports/"
    }
}
