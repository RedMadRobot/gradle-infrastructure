## [Unreleased]

### Dependencies

- Android Gradle Plugin 4.2.2 -> 7.0.3 
- Android cache fix Gradle plugin 2.4.3 -> 2.4.5
- Kotlin 1.5.31 -> 1.6.10
- JGit 5.12.0 -> 6.0.0

### Added

- Added workaround for the case when detekt ignores kotlin sources when run on Android project with type resolution (detekt/detekt#4177)

## [0.13] (2021-12-13)

### Change plugins naming convention

> **Breaking change!**

Plugins group changed from `redmadrobot` to `com.redmadrobot`.
This change allows us to publish infrastructure plugins to [Gradle Plugins Portal](https://plugins.gradle.org/) and make it easier to add plugins to project.

Changing `resolutionStrategy` in `settings.gradle.kts` is not needed anymore.
To make migration easier, it is allowed to apply plugins with deprecated IDs if you've specified `resolutionStrategy`, but it will throw a warning to console.

### Switch to multi-modular structure

> **Breaking change!**

<details>
<summary>Why multi-modular structure?</summary>

Until now plugins' structure inside module `infrastructure` was looking like this:

[![](https://mermaid.ink/img/eyJjb2RlIjoiZ3JhcGggQlRcbnJvb3RbXCJyb290LXByb2plY3RcIl1cbmtvdGxpbi1saWJyYXJ5IC0tPiByb290XG5kZXRla3QgLS0-IHJvb3RcbnB1Ymxpc2ggLS0-IHJvb3RcbmFwcGxpY2F0aW9uIC0tPiByb290XG5hbmRyb2lkLWxpYnJhcnkgLS0-IHJvb3QiLCJtZXJtYWlkIjp7InRoZW1lIjoiZGVmYXVsdCJ9LCJ1cGRhdGVFZGl0b3IiOnRydWUsImF1dG9TeW5jIjp0cnVlLCJ1cGRhdGVEaWFncmFtIjp0cnVlfQ)](https://mermaid-js.github.io/mermaid-live-editor/edit/#eyJjb2RlIjoiZ3JhcGggQlRcbnJvb3RbXCJyb290LXByb2plY3RcIl1cbmtvdGxpbi1saWJyYXJ5IC0tPiByb290XG5kZXRla3QgLS0-IHJvb3RcbnB1Ymxpc2ggLS0-IHJvb3RcbmFwcGxpY2F0aW9uIC0tPiByb290XG5hbmRyb2lkLWxpYnJhcnkgLS0-IHJvb3QiLCJtZXJtYWlkIjoie1xuICBcInRoZW1lXCI6IFwiZGVmYXVsdFwiXG59IiwidXBkYXRlRWRpdG9yIjp0cnVlLCJhdXRvU3luYyI6dHJ1ZSwidXBkYXRlRGlhZ3JhbSI6dHJ1ZX0)

Plugin `root-project` was used to add configurations for all others plugins.
With such structure all plugins should know about `root-project` plugin to access configs, and `root-project` should also know about all plugins to hold their configs.
It is possible only if all plugins are declared in the same module with `root-project`.
So it was the main stopping factor from breaking `infrastructure` to several independent modules.

Multi-modular structure gives important benefits to us:

- Allows modules to be updated independently, allowing users to pick what version of each module they want to use.
- Makes `infrastructure` more scalable.
  Because it is not more required to change `root-project` when adding a new plugin.
- Enables to use only "what you want" and don't bring extra dependencies to project.
---
</details>

Since now plugins are separated to modules:

[![](https://mermaid.ink/img/eyJjb2RlIjoiZ3JhcGggQlRcbnN1YmdyYXBoIGluZnJhc3RydWN0dXJlLWFuZHJvaWRcbiAgICBhbmRyb2lkLWNvbmZpZ1tjb20ucmVkbWFkcm9ib3QuYW5kcm9pZC1jb25maWddXG4gICAgY29tLnJlZG1hZHJvYm90LmFwcGxpY2F0aW9uIC0tPiBhbmRyb2lkLWNvbmZpZ1xuICAgIGNvbS5yZWRtYWRyb2JvdC5hbmRyb2lkLWxpYnJhcnkgLS0-IGFuZHJvaWQtY29uZmlnXG5lbmRcblxuc3ViZ3JhcGggaW5mcmFzdHJ1Y3R1cmUtZGV0ZWt0XG4gICAgY29tLnJlZG1hZHJvYm90LmRldGVrdFxuZW5kXG5cbnN1YmdyYXBoIGluZnJhc3RydWN0dXJlLWtvdGxpblxuICAgIGtvdGxpbi1jb25maWdbY29tLnJlZG1hZHJvYm90LmtvdGxpbi1jb25maWddXG4gICAgY29tLnJlZG1hZHJvYm90LmtvdGxpbi1saWJyYXJ5IC0tPiBrb3RsaW4tY29uZmlnXG5lbmRcblxuc3ViZ3JhcGggaW5mcmFzdHJ1Y3R1cmUtcHVibGlzaFxuICAgIHB1Ymxpc2gtY29uZmlnW2NvbS5yZWRtYWRyb2JvdC5wdWJsaXNoLWNvbmZpZ11cbiAgICBjb20ucmVkbWFkcm9ib3QucHVibGlzaCAtLT4gcHVibGlzaC1jb25maWdcbmVuZFxuXG5hbmRyb2lkLWNvbmZpZyAtLT4ga290bGluLWNvbmZpZyIsIm1lcm1haWQiOnsidGhlbWUiOiJkZWZhdWx0In0sInVwZGF0ZUVkaXRvciI6dHJ1ZSwiYXV0b1N5bmMiOnRydWUsInVwZGF0ZURpYWdyYW0iOnRydWV9)](https://mermaid-js.github.io/mermaid-live-editor/edit/#eyJjb2RlIjoiZ3JhcGggQlRcbnN1YmdyYXBoIGluZnJhc3RydWN0dXJlLWFuZHJvaWRcbiAgICBhbmRyb2lkLWNvbmZpZ1tjb20ucmVkbWFkcm9ib3QuYW5kcm9pZC1jb25maWddXG4gICAgY29tLnJlZG1hZHJvYm90LmFwcGxpY2F0aW9uIC0tPiBhbmRyb2lkLWNvbmZpZ1xuICAgIGNvbS5yZWRtYWRyb2JvdC5hbmRyb2lkLWxpYnJhcnkgLS0-IGFuZHJvaWQtY29uZmlnXG5lbmRcblxuc3ViZ3JhcGggaW5mcmFzdHJ1Y3R1cmUtZGV0ZWt0XG4gICAgY29tLnJlZG1hZHJvYm90LmRldGVrdFxuZW5kXG5cbnN1YmdyYXBoIGluZnJhc3RydWN0dXJlLWtvdGxpblxuICAgIGtvdGxpbi1jb25maWdbY29tLnJlZG1hZHJvYm90LmtvdGxpbi1jb25maWddXG4gICAgY29tLnJlZG1hZHJvYm90LmtvdGxpbi1saWJyYXJ5IC0tPiBrb3RsaW4tY29uZmlnXG5lbmRcblxuc3ViZ3JhcGggaW5mcmFzdHJ1Y3R1cmUtcHVibGlzaFxuICAgIHB1Ymxpc2gtY29uZmlnW2NvbS5yZWRtYWRyb2JvdC5wdWJsaXNoLWNvbmZpZ11cbiAgICBjb20ucmVkbWFkcm9ib3QucHVibGlzaCAtLT4gcHVibGlzaC1jb25maWdcbmVuZFxuXG5hbmRyb2lkLWNvbmZpZyAtLT4ga290bGluLWNvbmZpZyIsIm1lcm1haWQiOiJ7XG4gIFwidGhlbWVcIjogXCJkZWZhdWx0XCJcbn0iLCJ1cGRhdGVFZGl0b3IiOnRydWUsImF1dG9TeW5jIjp0cnVlLCJ1cGRhdGVEaWFncmFtIjp0cnVlfQ)

Plugin `root-project` is deprecated now and will be removed in further versions.
Extensions are added to project via `*-config` plugins (exception is `detekt` plugin, so it is designed to be applied to root project).

If you want to get the same behavior as applying `root-project`, you can apply all config plugins to the root project:

```kotlin
apply {
    id("com.redmadrobot.android-config") version "0.13"
    id("com.redmadrobot.publish-config") version "0.13"
    id("com.redmadrobot.detekt") version "0.13"
}
```

### Configs behavior changes

- `*-config` plugins can be applied to any project.
  Config from inner project will inherit options from outer project.
- Options `redmadrobot.android.test` are inherited from `redmadrobot.test`

### Dependencies

- Kotlin 1.5.30 -> 1.5.31
- Detekt 1.18.1 -> 1.19.0

### Added

- Added repository extension `jitpack()` (#82)

### Changed

- **Breaking change!** Repositories `ossrh` and `ossrhSnapshots` now use host `s01.oss.sonatype.org` by default.
  To keep legacy host `oss.sonatype.org`, use `ossrh(LEGACY)` instead.
- **Breaking change!** Directory with ProGuard rules now intended to be in `application` project instead of root project.
- Default `targetSdk` changed from 30 to 31.
- Gradle updated to 7.3.1

### Fixed

- Non-Kotlin modules are excluded from checking that the module contains detekt [#81](https://github.com/RedMadRobot/gradle-infrastructure/issues/81)

## [0.12.2] (2021-11-15)

### Fixed

- Fixed proguard file detection which was broken after file extensions check was added.

## [0.12.1] (2021-09-07)

### Fixed

- Fixed regex to create `detekt[Variant]All`, `detektBaseline[Variant]All` when infrastructure root project differs from Gradle root project.

## [0.12] (2021-09-01)

### Dependencies

- Kotlin Gradle Plugin 1.5.20 -> 1.5.30
- Android Gradle Plugin 4.2.1 -> 4.2.2
- Android cache fix Gradle plugin 2.4.0 -> 2.4.3
- detekt 1.17.1 -> 1.18.1

### Added

- Plugin `redmadrobot.root-project` now can be applied to any project that should be considered as "root" project for **gradle-infrastructure**.
  It may be useful for projects where you need to apply **gradle-infrastructure** only to particular subprojects.
- Added Detekt tasks `detekt[Variant]All` with type resolution
- Added the `detektBaselineAll` task to create a baseline file for Detekt rules.
- Added `detektBaseline[Variant]All` tasks to create a baseline file for Detekt rules with type resolution.
- Added option `redmadrobot.jvmTarget` to specify target JVM for Kotlin and Java compilers.
- Added option `redmadrobot.android.testTasksFilter` to filter tasks that should be run on ':test'.

### Changed

- Android libraries' publication moved back to `infrastructure` from `infrastructure-android`.
- Use only files with extension `.pro` from `proguard` directory as inputs for R8.

### Housekeeping

- Kotlin API version changed from 1.3 to 1.4
- Gradle 7.1.1 -> 7.2

## [0.11] (2021-07-06)

### Dependencies

- Kotlin Gradle Plugin 1.5.10 -> 1.5.20
- JGit 5.11.0 -> 5.12.0

### Added

- Added ability to configure `compileSdk` separately from `targetSdk`
- Added ability to configure `buildToolsVersion` for all android modules

### Housekeeping

- Update Gradle 7.0.2 -> 7.1.1
- Added KDoc comments to the all plugins

## [0.10] (2021-05-31)

### Lazy Configuration

> **Breaking change!**

`RedmadrobotExtension` migrated to [lazy properties API](https://docs.gradle.org/current/userguide/lazy_configuration.html#lazy_properties).
Now all extension fields are properties.
You should use method `.set(value)` instead of assignment operator `=` to assign value.

```diff
redmadrobot {
    android {
-       minApi = 26
+       minApi.set(26)
    }
}
```

This change helps us to check the Redmadrobot extension not used before it was configured.

### Plugins no more add Kotlin dependencies

> **Breaking change!**

Previously plugins `kotlin-library`, `android-library` and `application` used to add `kotlin-stdlib-jdk8` and `kotlin-test` dependencies by default.
It was a problem because:

1. Sometimes you don't want to add these dependencies
2. Or want to add it with different configuration (for example `compileOnly` instead of `implementation`)

**gradle-infrastructure** should add only options valid for all our projects or default options that can be changed if needed.
Default applied dependencies can't be removed if needed, so they should not be applied by default.

#### Another problem is `redmadrobot.kotlinVersion`.

> Option `redmadrobot.kotlinVersion` is deprecated and will not take any effect.

We've introduced this option to make it possible to change version of default kotlin dependencies.
This option affects only dependencies added by gradle-infrastructure, not all Kotlin dependencies, and it is confusing.
Moreover, this version does not affect Kotlin Gradle Plugin because it uses version specified in gradle-infrastructure at compilation time.

A more convenient way to align the Kotlin version for all dependencies including transitive ones is to use `kotlin-bom`:

```kotlin
dependencies {
    // Align versions of all Kotlin components 
    implementation(platform(kotlin("bom", version = "1.5.10")))

    // Now you can add Kotlin components without version
    implementation(kotlin("stdlib-jdk8"))
    testImplementation(kotlin("test-junit5"))
}
```

### Added

- Specified default location for Detekt baseline: `$configsDir/detekt/baseline.xml`
- Added the ability to check the Detekt only on changed files ([#40](https://github.com/RedMadRobot/gradle-infrastructure/issues/40)).

### Changed

- Update Gradle 7.0 -> 7.0.2
- Flag `publishing.signArtifacts` affects all artifacts not only the main one
- Method `test.useJunit()` can take lambda to configure JUnit framework
- Accessor `redmadrobot.android` can be used without imports
- `RedmadrobotExtension` is [extension-aware](https://docs.gradle.org/current/dsl/org.gradle.api.plugins.ExtensionAware.html) since now
- **Breaking change!** All extensions and constants moved to package `com.redmadrobot.build.dsl` to make it possible to import all extensions via single import

### Fixed

- Publication not configured when `redmadrobot.publish` applied before other plugins
- Look at flag `gradlePlugin.isAutomatedPublishing` when configuring gradle plugin publication

### Dependencies

- Update Kotlin 1.4.32 -> 1.5.10
- Update AGP 4.1.3 -> 4.2.1
- Update Detekt 1.16.0 -> 1.17.1

## [0.9] (2021-05-04)

### infrastructure-android

Module `infrastructure` no more depends on AGP and doesn't require the `google()` repository to be applied.

Plugins `redmadrobot.application` and `redmadrobot.android-library` was moved to `infrastructure-android` module. 
You should specify it in settings.gradle.kts to be able to use it:

```diff
resolutionStrategy {
    eachPlugin {
        if (requested.id.namespace == "redmadrobot") {
-            useModule("com.redmadrobot.build:infrastructure:${requested.version}")
+            useModule("com.redmadrobot.build:infrastructure-android:${requested.version}")
        }
    }
}
```

> **Breaking change!**
> If you use `redmadrobot.android` in the root project, you should add the following import:
>
> ```kotlin
> import com.redmadrobot.build.extension.android
> ```

### QA build type name

`BUILD_TYPE_STAGING` superseded with `BUILD_TYPE_QA`.
To keep backward compatibility you can configure QA build type name using project property in `gradle.properties`:

```properties
redmadrobot.android.build.type.qa=staging
```

### Added

- Apply [android-cache-fix plugin](https://github.com/gradle/android-cache-fix-gradle-plugin) to android projects (#44)

### Changed

- Updated Gradle 6.8.3 -> 7.0
- Completely removed `jcenter` from repositories (#36)

### Fixed

- Flag `warningsAsErrors` now should work.
  It is enabled by default on CI.

## [0.8.2] (2021-03-30)

### JCenter only for exclusive content

JCenter is at the end of life and should mot be used anymore.
Now `jcenter()` used in `gradle-infrastructure` only for exclusive content that not migrated to Maven Central yet.
You can get errors like this:

```
> Could not resolve all files for configuration ':app:debugRuntimeClasspath'.
   > Could not find com.xwray:groupie:2.7.2
     Searched in the following locations:
       - ...
     Required by:
         project :app > com.xwray:groupie:2.7.2
```

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

### Housekeeping

- Detekt 1.15.0 -> 1.16.0
- Kotlin 1.4.31 -> 1.4.32
- AGP 4.1.2 -> 4.1.3

## [0.8.1] (2021-02-28)

### Fixed

- Add back `jcenter()` for `detekt-formatting`

### Housekeeping

- Kotlin updated to 1.4.31

## [0.8] (2021-02-25)

### POM Configuration

Now you can configure common for all modules POM properties in `redmadrobot.publishing` extension.
There are extension-functions to cover common configuration use-cases.

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

Use `publishing` extension in module build script to configure POM for this module.
Take publication name from `PUBLICATION_NAME` constant.

```kotlin
publishing {
    publications {
        getByName<MavenPublication>(PUBLICATION_NAME) {
            pom {
                // Configure POM here
            }
        }
    }
}

// or even shorter
publishing.publications.getByName<MavenPublication>(PUBLICATION_NAME) {
    pom {
        // Configure POM here
    }
}
```

### Added

- Add javadoc publication for kotlin libraries and gradle plugins

### Fixed

- Fixed gradle plugins signing

## [0.7] (2021-02-25)

### JCenter dropped

> **Breaking change!** Removed adding `jcenter()` repository, replaced with `mavenCentral()`.
> Check all your dependencies can be resolved. If you still want `jcenter`, add it manually. 

Replace `rmrBintray(...)` with `ossrh()` or `ossrhSnapshots()`:
```diff
publishing {
    repositories {
-        if (!isSnapshotVersion) rmrBintray("infrastructure")
+        if (isReleaseVersion) ossrh()
    }
}
```

To be able to publish artifacts to OSSRH you should sign the artifacts.
Signing can be enabled in extension `redmadrobot.publishing`:

```kotlin
redmadrobot {
    publishing {
        signArtifacts = true    // false by default
        useGpgAgent = true      // true by default
    }
}
```

Read [Signing Plugin](https://docs.gradle.org/current/userguide/signing_plugin.html#signing_plugin) docs for more information about signing configuration.

Pull request: #35

### Added

- New extension `isReleaseVersion` opposite to existing `isSnapshotVersion`

### Fixed

- Fix Gradle plugins publication (#29)

### Housekeeping

- Update Gradle to 6.8.3
- Update Kotlin to 1.4.30
- Update AGP to 4.1.2
- Update dependencies in samples
- Added CHANGELOG.md :)

[unreleased]: https://github.com/RedMadRobot/gradle-infrastructure/compare/v0.13...main
[0.13]: https://github.com/RedMadRobot/gradle-infrastructure/compare/v0.12.2...v0.13
[0.12.2]: https://github.com/RedMadRobot/gradle-infrastructure/compare/v0.12.1...v0.12.2
[0.12.1]: https://github.com/RedMadRobot/gradle-infrastructure/compare/v0.12...v0.12.1
[0.12]: https://github.com/RedMadRobot/gradle-infrastructure/compare/v0.11...v0.12
[0.11]: https://github.com/RedMadRobot/gradle-infrastructure/compare/v0.10...v0.11
[0.10]: https://github.com/RedMadRobot/gradle-infrastructure/compare/v0.9...v0.10
[0.9]: https://github.com/RedMadRobot/gradle-infrastructure/compare/v0.8.2...v0.9
[0.8.2]: https://github.com/RedMadRobot/gradle-infrastructure/compare/v0.8.1..v0.8.2
[0.8.1]: https://github.com/RedMadRobot/gradle-infrastructure/compare/v0.8...v0.8.1
[0.8]: https://github.com/RedMadRobot/gradle-infrastructure/compare/v0.7...v0.8
[0.7]: https://github.com/RedMadRobot/gradle-infrastructure/compare/v0.6...v0.7
