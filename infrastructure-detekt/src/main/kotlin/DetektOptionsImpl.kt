package com.redmadrobot.build.detekt

import org.gradle.api.provider.Property

@Suppress("LeakingThis")
internal abstract class DetektOptionsImpl : DetektOptions {

    abstract override val detektDiffOptions: Property<DetektDiffOptions>

    override fun checkOnlyDiffWithBranch(
        branch: String,
        configure: DetektDiffOptions.() -> Unit,
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
