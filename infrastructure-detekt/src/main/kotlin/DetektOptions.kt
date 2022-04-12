package com.redmadrobot.build.detekt

import org.gradle.api.provider.Provider

/** Options for detekt. */
public interface DetektOptions {

    /** Options for detektDiff task. */
    public val detektDiffOptions: Provider<DetektDiffOptions>

    /** Enable Detekt checks only for modified files provided by git (compare with [branch]). */
    public fun checkOnlyDiffWithBranch(
        branch: String,
        configure: DetektDiffOptions.() -> Unit = {},
    )
}

/** Options to enable detekt on diffs. */
public class DetektDiffOptions {

    /** Base branch to compare changes. */
    internal var baseBranch: String = ""

    /** List of file extensions to check. */
    public var fileExtensions: Set<String> = setOf(".kt", ".kts")
}
