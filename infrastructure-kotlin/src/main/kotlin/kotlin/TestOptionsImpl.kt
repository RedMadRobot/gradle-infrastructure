package com.redmadrobot.build.kotlin

import com.redmadrobot.build.WithDefaults
import com.redmadrobot.build.internal.InternalGradleInfrastructureApi
import com.redmadrobot.build.kotlin.internal.setFinalValue
import org.gradle.api.provider.Property
import org.gradle.api.tasks.testing.TestFrameworkOptions
import org.gradle.api.tasks.testing.junit.JUnitOptions
import org.gradle.api.tasks.testing.junitplatform.JUnitPlatformOptions

@InternalGradleInfrastructureApi
@Suppress("LeakingThis")
public abstract class TestOptionsImpl : TestOptions, WithDefaults<TestOptionsImpl> {

    abstract override val useJunitPlatform: Property<Boolean>

    /** Configurator for Test Framework. */
    internal abstract val configuration: Property<TestFrameworkOptions.() -> Unit>

    init {
        useJunitPlatform
            .convention(true)
            .finalizeValueOnRead()
        configuration
            .convention { /* no-op */ }
            .finalizeValueOnRead()
    }

    override fun useJunitPlatform(configure: JUnitPlatformOptions.() -> Unit) {
        useJunitPlatform.setFinalValue(true)
        configuration.set { (this as JUnitPlatformOptions).configure() }
    }

    override fun useJunit(configure: JUnitOptions.() -> Unit) {
        useJunitPlatform.setFinalValue(false)
        configuration.set { (this as JUnitOptions).configure() }
    }

    override fun setDefaults(defaults: TestOptionsImpl) {
        useJunitPlatform.convention(defaults.useJunitPlatform)
        configuration.convention(defaults.configuration)
    }
}
