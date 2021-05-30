package com.redmadrobot.build.extension

import com.redmadrobot.build.internal.findByName
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.plugins.ExtensionAware
import org.gradle.api.provider.Property
import org.gradle.api.publish.maven.MavenPom
import org.gradle.api.tasks.testing.TestFrameworkOptions
import org.gradle.api.tasks.testing.junit.JUnitOptions
import org.gradle.api.tasks.testing.junitplatform.JUnitPlatformOptions
import org.gradle.kotlin.dsl.create
import kotlin.properties.ReadOnlyProperty

public interface RedmadrobotExtension :
    TestOptionsSpec,
    PublishingOptionsSpec,
    StaticAnalyzersSpec,
    ExtensionAware {

    /** Kotlin version that should be used for all projects. */
    @Deprecated(
        level = DeprecationLevel.ERROR,
        message = "This option have not effect anymore. " +
            "Remove it and use `kotlin-bom` to align Kotlin version across all dependencies.",
    )
    @Suppress("unused_parameter")
    public var kotlinVersion: String
        set(value) = error("You should not use this.")
        get() = error("You should not use this.")

    public companion object {
        /** Extension name. */
        public const val NAME: String = "redmadrobot"

        /**
         * Provides delegate to add an extra property to [RedmadrobotExtensionImpl].
         *
         * It may be useful to use package `org.gradle.kotlin.dsl` for delegated properties because
         * members from this package are imported by default and declared property can be used without import.
         * This package is used by Gradle for generated accessors.
         *
         * ```
         * package org.gradle.kotlin.dsl
         *
         * val RedmadrobotExtension.android: AndroidOptions by RedmadrobotExtension.extensionProperty()
         * ```
         */
        public inline fun <reified V : Any> extensionProperty(): ReadOnlyProperty<RedmadrobotExtension, V> {
            return ReadOnlyProperty { thisRef, property ->
                thisRef.extensions.findByName<V>(property.name)
                    ?: thisRef.extensions.create(property.name)
            }
        }
    }
}

public interface TestOptionsSpec {

    /** Settings for JVM test task. */
    public val test: TestOptions

    /** Settings for JVM test task. */
    public fun test(configure: TestOptions.() -> Unit)
}

public interface StaticAnalyzersSpec : DetektOptionsSpec {

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

public interface DetektOptionsSpec {

    /** Settings for detekt task. */
    public val detekt: DetektOptions

    /** Settings for detekt task. */
    public fun detekt(configure: DetektOptions.() -> Unit)
}

public interface PublishingOptionsSpec {

    /** Settings for publishing. */
    public val publishing: PublishingOptions

    /** Settings for publishing. */
    public fun publishing(configure: PublishingOptions.() -> Unit)
}

public abstract class PublishingOptions {

    /**
     * Enables artifacts signing before publication.
     *
     * By default tries to use gpg-agent to sign artifacts, but you can disable it with setting
     * [useGpgAgent] to false.
     * If you don't use gpg-agent, requires signatory credentials to be configured in `gradle.properties`.
     * Read more: [Signing Plugin](https://docs.gradle.org/current/userguide/signing_plugin.html#signing_plugin)
     *
     * @see useGpgAgent
     */
    public abstract var signArtifacts: Property<Boolean>

    /**
     * Use gpg-agent to sign artifacts. Has effect only if [signArtifacts] is `true`.
     * By default is `true`.
     */
    public abstract val useGpgAgent: Property<Boolean>

    internal abstract val configurePom: Property<MavenPom.() -> Unit>

    /**
     * Configures POM file for all modules.
     * Place here only common configurations.
     */
    public fun pom(configure: MavenPom.() -> Unit) {
        configurePom.set(configure)
    }

    init {
        signArtifacts
            .convention(false)
            .finalizeValueOnRead()
        useGpgAgent
            .convention(true)
            .finalizeValueOnRead()
        configurePom
            .convention { /* no-op */ }
            .finalizeValueOnRead()
    }
}

public abstract class TestOptions {

    /** Flag for using Junit Jupiter Platform. */
    internal abstract val useJunitPlatform: Property<Boolean>

    /** Configurator for Test Framework. */
    internal abstract val configuration: Property<TestFrameworkOptions.() -> Unit>

    /** Specifies that JUnit Platform (JUnit 5) should be used to execute the tests. */
    public fun useJunitPlatform(configure: JUnitPlatformOptions.() -> Unit = {}) {
        useJunitPlatform.set(true)
        configuration.set { (this as JUnitPlatformOptions).configure() }
    }

    /** Specifies that JUnit should be used to execute the tests. */
    public fun useJunit(configure: JUnitOptions.() -> Unit = {}) {
        useJunitPlatform.set(false)
        configuration.set { (this as JUnitOptions).configure() }
    }

    init {
        useJunitPlatform
            .convention(true)
            .finalizeValueOnRead()
        configuration
            .convention { /* no-op */ }
            .finalizeValueOnRead()
    }
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
