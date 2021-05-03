# infrastructure <GitHub path="RedMadRobot/gradle-infrastructure"/>
[![Version](https://img.shields.io/maven-central/v/com.redmadrobot.build/infrastructure?style=flat-square)][mavenCentral] [![Build Status](https://img.shields.io/github/workflow/status/RedMadRobot/gradle-infrastructure/CI/main?style=flat-square)][ci] [![License](https://img.shields.io/github/license/RedMadRobot/gradle-infrastructure?style=flat-square)][license]

Small plugins to reduce boilerplate in Gradle build scripts.

> :warning: It is designed to use with Gradle Kotlin DSL and can't be used from Groovy DSL.

---
<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->


- [Installation](#installation)
- [Plugins](#plugins)
  - [kotlin-library](#kotlin-library)
  - [publish](#publish)
  - [detekt](#detekt)
- [Android Plugins](#android-plugins)
  - [application and android-library](#application-and-android-library)
- [Usage](#usage)
  - [Configuration](#configuration)
  - [Warnings as errors](#warnings-as-errors)
  - [Share sources between build types](#share-sources-between-build-types)
  - [Configure junit test execution options](#configure-junit-test-execution-options)
- [Samples](#samples)
- [Troubleshooting](#troubleshooting)
  - [Tests failed - `No value has been specified for property 'localResourcesFile'`](#tests-failed---no-value-has-been-specified-for-property-localresourcesfile)
  - [`Android resource linking failed` or `Unresolved reference: R`](#android-resource-linking-failed-or-unresolved-reference-r)
  - [Build failed on CI - `No version of NDK matched the requested version`](#build-failed-on-ci---no-version-of-ndk-matched-the-requested-version)
  - [`Could not resolve` or `Could not find` dependencies](#could-not-resolve-or-could-not-find-dependencies)
- [Contributing](#contributing)
- [License](#license)

<!-- END doctoc generated TOC please keep comment here to allow auto update -->

## Installation

Add resolution strategy to `settings.gradle.kts`:
```kotlin
pluginManagement {
    repositories {
        google() // Required if you use infrastructure-android
        gradlePluginPortal()
    }

    resolutionStrategy {
        eachPlugin {
            if (requested.id.namespace == "redmadrobot") {
                // For pure Kotlin projects
                useModule("com.redmadrobot.build:infrastructure:${requested.version}")
                // For Android projects
                useModule("com.redmadrobot.build:infrastructure-android:${requested.version}")
            }
        }
    }
}
```

Apply `redmadrobot.root-project` to your root project:
```kotlin
plugins {
    id("redmadrobot.root-project") version "0.8.2"
}
```

Then you can apply any of plugins where you need:
```kotlin
plugins {
    id("redmadrobot.kotlin-library")
    id("redmadrobot.publish")
    id("redmadrobot.application")
}
```

> :bulb: **Hint**  
> If you'll try to apply any of plugins to the root project, it will require 'version'.
> You can apply it with `apply(plugin = "...")` syntax to let it use `root-project`s plugin version.
> ```kotlin
> plugins {
>     id("redmadrobot.root-project") version "0.8.2"
> }
> 
> apply(plugin = "redmadrobot.detekt")
> ```

*Look at [samples](#samples) for quick start.*

## Plugins

### kotlin-library

Common configurations for pure Kotlin libraries.

- Applies plugin `kotlin`
- Specifies `jvmTarget` 1.8
- Adds repository `mavenCentral`
- Adds `kotlin-stdlib-jdk8` as dependency with `api` scope
- Adds `kotlin-test` and `junit` as test dependencies
- Enables [explicit API mode][explicit-api]

### publish

Common publish configurations for both Android and Kotlin libraries.

- Applies plugin `maven-publish`
- Adds sources and javadocs to publication

You should specify publishing repositories manually. You can also use [predicates] for publication:
```kotlin
publishing {
    repositories {
        // Unconditional publication
        rmrNexus()
        // Publication with conditions
        if (isRunningOnCi) githubPackages("RedMadRobot/gradle-infrastructure")
        if (isReleaseVersion && credentialsExist("ossrh")) ossrh()
    }
}
```

#### Signing

You can configure publication in extension `redmadrobot.publishing`:
```kotlin
redmadrobot {
    publishing {
        signArtifacts = true    // Enables artifacts signing, required for publication to OSSRH
        useGpgAgent = true      // By default use gpg-agent for artifacts signing
    }
}
```

Read more about singing configuration in [Signing Plugin][signing-plugin] docs.

#### Customize POM

You can configure POM properties common for all modules.

> Note: there are extension-functions to simplify common configuration use-cases.
> All available extensions you can find [here][MavenPom].

```kotlin
redmadrobot {
    publishing {
        pom {
            // Configure <url>, <scm> and <issueManagement> tags for GitHub project by it's name
            setGitHubProject("RedMadRobot/gradle-infrastructure")
            
            licenses { 
                mit() // Add MIT license
            }
            
            developers {
                // Shorthand to add a developer
                developer(id = "jdoe", name = "John Dow", email = "john@doe.com")
            }
        }
    }
}
```

#### Customize publication for module

Use `publishing` extension in module build script to configure publication for the single module.
Take publication name from `PUBLICATION_NAME` constant.
Read more in [Maven Publish plugin][maven-publish] docs.

```kotlin
publishing {
    publications {
        getByName<MavenPublication>(PUBLICATION_NAME) {
            // Configure publication here
        }
    }
}

// or even shorter
publishing.publications.getByName<MavenPublication>(PUBLICATION_NAME) {
    // Configure publication here
}
```

### detekt

- Adds repository `mavenCentral`
- Applies `detekt` plugin with `detekt-formatting`
- Configures additional tasks:
    - `detektAll` - Runs Detekt over the whole codebase
    - `detektFormat` - Reformats the whole codebase with Detekt

## Android Plugins

> These plugins require `infrastructure-android` to use instead of `infrastructure`.

### application and android-library

Common configurations for Android libraries and application.

Both:
- Specifies `jvmTarget` and `compatibility` 1.8
- Specifies default compile, min and target SDK
- Disables `aidl`, `renderScript` and `shaders` [build-features]
- Adds `kotlin/` dirs to source sets
- Adds repositories `mavenCentral` and `google`
- Adds `kotlin-stdlib-jdk8` as dependency
- Adds `kotlin-test` and `junit` as test dependency
- Applies [android-cache-fix-gradle-plugin](https://github.com/gradle/android-cache-fix-gradle-plugin)

Library:
- Applies plugin `com.android.library`
- Disables `buildConfig` and `resValues` [build-features]
- Enables [explicit API mode][explicit-api]

Application:
- Applies plugin `com.android.application`
- Adds all proguard files from `proguard` folder
- Configures `debug`, `staging` and `release` build types
- Adds `LOCK_ORIENTATION` and `CRASH_REPORTS_ENABLED` BuildConfig variables which `false` only for `debug` build type
- Configures Android Lint [default options][lint-options]

## Usage

### Configuration

You can configure the plugins with an extension named `redmadrobot`.
Look for available properties with description in [RedmadrobotExtension].

The extension should be configured in root project.
```kotlin
// root project build.gradle.kts

redmadrobot {
    configsDir.set(file("path/to/configs/"))
}
```

### Warnings as errors

By default, infrastructure plugins enable Kotlin compiler's option `allWarningsAsErrors` (`-Werror`) on CI.
You can change it by defining the `warningsAsErrors` project property.

[Read more about Gradle project properties][project-properties]

### Share sources between build types

You can share sources between two build types.  
For example, you need to use debug panel in both debug and staging builds, and don't want to write similar duplicating code for each of these build types.
You can do it with one line with [addSharedSourceSetRoot] extension-function:
```kotlin
android {
    // We need to share sources between debug and staging builds
    addSharedSourceSetRoot(BUILD_TYPE_DEBUG, BUILD_TYPE_STAGING)

    // We can specify name for the source set root if need
    addSharedSourceSetRoot(BUILD_TYPE_DEBUG, BUILD_TYPE_STAGING, name = "debugPanel")
}
```

### Configure junit test execution options

By default, the plugin uses the JUnit Platform to run tests.
If you want to configure it, for example include an engine, you can do it using the `test` extension for both JVM and android.
```kotlin
redmadrobot {
    test {
        useJunitPlatform {
            includeEngines("spek2")
        }
    }
    android {
        test {
            useJunitPlatform {
                includeEngines("spek2")
            }
        }
    }
}
```

If you want to use JUnit 4 framework to run tests, you need to specify `useJunit()` in test block.
```kotlin
redmadrobot {
    test {
        useJunit()
    }
}
```

## Samples

Look for samples in [samples] package.

If you need closer to life samples, check these projects:
- [mapmemory] - Multi module, kotlin library, publication, detekt
- [itemsadapter] - Multi module, android library, publication, detekt
- [redmadrobot-android-ktx] - Multi module, android library, publication, detekt
- [infrastructure] - Gradle plugin, publication

## Troubleshooting

### Tests failed - `No value has been specified for property 'localResourcesFile'`

```
A problem was found with the configuration of task ':mylib:generateReleaseUnitTestStubRFile' (type 'GenerateLibraryRFileTask').
> No value has been specified for property 'localResourcesFile'.
```

It is a [known bug](https://issuetracker.google.com/issues/161586464) in AGP 4.1.0 caused when `androidResources` is disabled.
As workaround, you can enable this build feature for module:
```kotlin
android {
    // TODO: Remove when bug in AGP will be fixed.
    //  https://issuetracker.google.com/issues/161586464
    buildFeatures.androidResources = true
}
```

### `Android resource linking failed` or `Unresolved reference: R`

```
Execution failed for task ':app:processDebugResources'.
> A failure occurred while executing com.android.build.gradle.internal.tasks.Workers$ActionFacade
   > Android resource linking failed
     AAPT: error: resource style/TextAppearance.App.Headline4 (aka com.example.app.debug:style/TextAppearance.App.Headline4) not found.
     error: resource style/TextAppearance.App.Body2 (aka com.example.app.debug:style/TextAppearance.App.Body2) not found.
     error: resource style/Theme.App (aka com.example.app.debug:style/Theme.App) not found.
     error: resource style/Theme.App (aka com.example.app.debug:style/Theme.App) not found.
     error: failed linking references.
```

Build feature `androidResources` is disabled by default for android libraries.
If you get these errors you should enable it:

```kotlin
android {
    buildFeatures.androidResources = true
}
```

### Build failed on CI - `No version of NDK matched the requested version`

```
Execution failed for task ':app:stripDebugDebugSymbols'.
> No version of NDK matched the requested version 21.0.6113669. Versions available locally: 21.1.6352462
```

It is because NDK version on CI differs from a requested version.
You can change requested version by setting `android.ndkVersion`.

Plugins `redmadrobot.android-library` and `redmadrobot.application` by default apply NDK version from env variable `ANDROID_NDK_VERSION` if it set.

### `Could not resolve` or `Could not find` dependencies

```
> Could not resolve all files for configuration ':app:debugRuntimeClasspath'.
   > Could not find com.xwray:groupie:2.7.2
     Searched in the following locations:
       - ...
     Required by:
         project :app > com.xwray:groupie:2.7.2
```

It may be because of `gradle-infrastructure` uses `mavenCentral` instead of `jcenter` by default.
[JCenter is at the end of life][jcenter-end] and should mot be used anymore.
Unfortunately not all libraries migrated to Maven Central yet.
To avoid these errors, declare `jcenter` repository in your build script and configure it to be used only for missing dependencies.

```kotlin
repositories {
    jcenter {
        content {
            // It is useful to add a link to the issue about migration from JCenter
            // https://github.com/lisawray/groupie/issues/384
            includeModule("com.xwray", "groupie")
        }
    }
}
```

## Contributing
Merge requests are welcome.
For major changes, please open an issue first to discuss what you would like to change.

## License

[MIT][license]

[samples]: samples/
[RedmadrobotExtension]: infrastructure/src/main/kotlin/extension/RedmadrobotExtension.kt
[MavenPom]: infrastructure/src/main/kotlin/extension/MavenPom.kt
[predicates]: infrastructure/src/main/kotlin/extension/PublishingPredicates.kt
[addSharedSourceSetRoot]: infrastructure/src/main/kotlin/extension/SourceSets.kt
[lint-options]: https://github.com/RedMadRobot/gradle-infrastructure/blob/2e96c04cbb9d15ca508d1d4b4a8b1e2da4bab6af/infrastructure/src/main/kotlin/AndroidApplicationPlugin.kt#L63-L72

[infrastructure]: #
[itemsadapter]: https://github.com/RedMadRobot/itemsadapter
[redmadrobot-android-ktx]: https://github.com/RedMadRobot/redmadrobot-android-ktx
[mapmemory]: https://github.com/RedMadRobot/mapmemory

[mavenCentral]: https://search.maven.org/artifact/com.redmadrobot.build/infrastructure
[ci]: https://github.com/RedMadRobot/gradle-infrastructure/actions
[license]: LICENSE

[build-features]: https://developer.android.com/reference/tools/gradle-api/com/android/build/api/dsl/BuildFeatures
[explicit-api]: https://kotlinlang.org/docs/reference/whatsnew14.html#explicit-api-mode-for-library-authors
[signing-plugin]: https://docs.gradle.org/current/userguide/signing_plugin.html
[maven-publish]: https://docs.gradle.org/current/userguide/publishing_maven.html
[project-properties]: https://docs.gradle.org/current/userguide/build_environment.html#sec:project_properties

[jcenter-end]: https://jfrog.com/blog/into-the-sunset-bintray-jcenter-gocenter-and-chartcenter/
