package com.redmadrobot.build.extension

import org.gradle.api.provider.Provider

public interface DetektOptionsSpec {

    /** Settings for detekt task. */
    public val detekt: DetektOptions

    /** Settings for detekt task. */
    public fun detekt(configure: DetektOptions.() -> Unit)
}

public interface DetektOptions {

    /** Options for detektDiff task. */
    public val detektDiffOptions: Provider<DetektDiffOptions>

    /** Enable Detekt checks only for modified files provided by git (compare with [branch]). */
    public fun checkOnlyDiffWithBranch(
        branch: String,
        configure: DetektDiffOptions.() -> Unit = {},
    )
}

public class DetektDiffOptions {

    /** Base branch to compare changes. */
    internal var baseBranch: String = ""

    /** List of file extensions to check. */
    public var fileExtensions: Set<String> = setOf(".kt", ".kts")
}
