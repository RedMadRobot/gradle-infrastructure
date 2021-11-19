package com.redmadrobot.build.detekt

import com.redmadrobot.build.WithDefaults
import org.gradle.api.provider.Property

@Suppress("LeakingThis")
internal abstract class DetektOptionsImpl : DetektOptions, WithDefaults<DetektOptionsImpl> {

    abstract override val detektDiffOptions: Property<DetektDiffOptions>

    init {
        detektDiffOptions.finalizeValueOnRead()
    }

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

    override fun setDefaults(defaults: DetektOptionsImpl) {
        detektDiffOptions.convention(defaults.detektDiffOptions)
    }
}
