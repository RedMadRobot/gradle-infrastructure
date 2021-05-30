package com.redmadrobot.build.extension

import org.gradle.api.provider.Property

public interface DetektOptionsSpec {

    /** Settings for detekt task. */
    public val detekt: DetektOptions

    /** Settings for detekt task. */
    public fun detekt(configure: DetektOptions.() -> Unit)
}

public abstract class DetektOptions {

    /** Options for detektDiff task. */
    internal abstract val detektDiffOptions: Property<DetektDiffOptions>

    /** Enable Detekt checks only for modified files provided by git (compare with [branch]). */
    public fun checkOnlyDiffWithBranch(
        branch: String,
        configure: DetektDiffOptions.() -> Unit = {},
    ) {
        require(branch.isNotBlank()) { "Base branch should not be blank." }

        detektDiffOptions.set(
            DetektDiffOptions().apply {
                configure()
                baseBranch = branch
            },
        )
    }

    init {
        detektDiffOptions.finalizeValueOnRead()
    }
}

public class DetektDiffOptions {

    /** Base branch to compare changes. */
    internal var baseBranch: String = ""

    /** List of file extensions to check. */
    public var fileExtensions: Set<String> = setOf(".kt", ".kts")
}
