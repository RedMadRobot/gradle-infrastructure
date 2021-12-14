#!/usr/bin/env bash
# TODO: Configure publishing from CI

set -e

# Publication to OSSRH should not be run in parallel
./gradlew :infrastructure-common:publish
./gradlew :infrastructure-publish:publish
./gradlew :infrastructure-detekt:publish
./gradlew :infrastructure-kotlin:publish
./gradlew :infrastructure-android:publish

# Publication to Gradle Plugins Portal
./gradlew publishPlugins
