package com.redmadrobot.build

import com.redmadrobot.build.internal.android
import org.gradle.api.Project
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.jvm.tasks.Jar
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.register

public class AndroidPublishPlugin : PublishPlugin() {

    override fun Project.configureAndroidPublication(): String {
        checkVersionSpecified()

        val sourcesJar = tasks.register<Jar>("sourcesJar") {
            archiveClassifier.set("sources")
            from(android.sourceSets["main"].java.srcDirs)
        }

        publishing {
            publications.create<MavenPublication>(PUBLICATION_NAME) {
                from(components[BUILD_TYPE_RELEASE])
                artifact(sourcesJar.get())
            }
        }

        return PUBLICATION_NAME
    }

    private fun Project.checkVersionSpecified() {
        if (version == Project.DEFAULT_VERSION) {
            version = checkNotNull(android.defaultConfig.versionName) {
                "You should specify either project 'version' or 'android.versionName' for publication."
            }
        }
    }
}
