## [Unreleased]

### Added

- Memory leak fix for Kotlin 1.5.0 until 1.5.10 released ([KT-46368](https://youtrack.jetbrains.com/issue/KT-46368))

### Changed

- Update Gradle 7.0 -> 7.0.2

### Dependencies

- Update Kotlin 1.4.32 -> 1.5.0

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

[unreleased]: https://github.com/RedMadRobot/gradle-infrastructure/compare/v0.9...main
[0.9]: https://github.com/RedMadRobot/gradle-infrastructure/compare/v0.8.2...v0.9
[0.8.2]: https://github.com/RedMadRobot/gradle-infrastructure/compare/v0.8.1..v0.8.2
[0.8.1]: https://github.com/RedMadRobot/gradle-infrastructure/compare/v0.8...v0.8.1
[0.8]: https://github.com/RedMadRobot/gradle-infrastructure/compare/v0.7...v0.8
[0.7]: https://github.com/RedMadRobot/gradle-infrastructure/compare/v0.6...v0.7
