package com.redmadrobot.build.extension

import org.gradle.api.file.DirectoryProperty
import org.gradle.api.model.ObjectFactory
import org.gradle.api.publish.maven.MavenPom
import org.gradle.api.tasks.testing.junitplatform.JUnitPlatformOptions
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

public open class RedmadrobotExtension(objects: ObjectFactory) {

    public companion object {
        /** Extension name. */
        public const val NAME: String = "redmadrobot"

        // Relative to root project directory.
        internal const val DEFAULT_CONFIGS_DIR = "config/"

        // Relative to root project build directory.
        internal const val DEFAULT_REPORTS_DIR = "reports/"

        /**
         * Provides delegate to add an extra field to [RedmadrobotExtension].
         * ```
         * val RedmadrobotExtension.extra: ExtraOptions by RedmadrobotExtension.field { ExtraOptions() }
         * ```
         */
        public fun <V : Any> field(
            defaultValue: RedmadrobotExtension.() -> V,
        ): ReadWriteProperty<RedmadrobotExtension, V> {
            return object : ReadWriteProperty<RedmadrobotExtension, V> {
                override fun getValue(thisRef: RedmadrobotExtension, property: KProperty<*>): V {
                    @Suppress("UNCHECKED_CAST")
                    return thisRef.extraFields.getOrPut(property.name) { defaultValue(thisRef) } as V
                }

                override fun setValue(thisRef: RedmadrobotExtension, property: KProperty<*>, value: V) {
                    thisRef.extraFields[property.name] = value
                }
            }
        }
    }

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

    /** Directory where stored configs for static analyzers. */
    public val configsDir: DirectoryProperty = objects.directoryProperty()

    /** Directory where will be stored static analyzers reports. */
    public val reportsDir: DirectoryProperty = objects.directoryProperty()

    /** Settings for publishing. */
    public val publishing: PublishingOptions = PublishingOptions()

    /** Settings for publishing. */
    public fun publishing(configure: PublishingOptions.() -> Unit) {
        publishing.configure()
    }

    /** Settings for JVM test task. */
    public val test: TestOptions = TestOptions()

    /** Settings for JVM test task. */
    public fun test(configure: TestOptions.() -> Unit) {
        test.configure()
    }

    /** Settings for detekt task. */
    public val detekt: DetektOptions = DetektOptions()

    /** Settings for detekt task. */
    public fun detekt(configure: DetektOptions.() -> Unit) {
        detekt.configure()
    }

    /**
     * Provides storage for additional extension fields.
     * @see field
     */
    private val extraFields = mutableMapOf<String, Any?>()
}

public class PublishingOptions internal constructor() {

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
    public var signArtifacts: Boolean = false

    /** Use gpg-agent to sign artifacts. Has effect only if [signArtifacts] is true. */
    public var useGpgAgent: Boolean = true

    internal var configurePom: MavenPom.() -> Unit = {}

    /**
     * Configures POM file for all modules.
     * Place here only common configurations.
     */
    public fun pom(configure: MavenPom.() -> Unit) {
        configurePom = configure
    }
}

public class TestOptions {

    /** Flag for using Junit Jupiter Platform. */
    internal var useJunitPlatform: Boolean = true
        private set

    /** Options for JUnit Platform. */
    internal val jUnitPlatformOptions by lazy { JUnitPlatformOptions() }

    public fun useJunitPlatform(testFrameworkConfigure: JUnitPlatformOptions.() -> Unit = {}) {
        useJunitPlatform = true
        testFrameworkConfigure.invoke(jUnitPlatformOptions)
    }

    public fun useJunit() {
        useJunitPlatform = false
    }
}

public class DetektOptions {

    /** Options for detektDiff task. */
    internal var detektDiffOptions: DetektDiffOptions? = null
        private set

    /** Enable Detekt checks only for modified files provided by git (compare with [branch]). */
    public fun checkOnlyDiffWithBranch(
        branch: String,
        configure: DetektDiffOptions.() -> Unit = {},
    ) {
        require(branch.isNotBlank()) { "Base branch should not be blank." }

        detektDiffOptions = DetektDiffOptions().apply {
            configure()
            baseBranch = branch
        }
    }
}

public class DetektDiffOptions {

    /** Base branch to compare changes. */
    internal var baseBranch: String = ""

    /** List of file extensions to check. */
    public var fileExtensions: Set<String> = setOf(".kt", ".kts")
}
