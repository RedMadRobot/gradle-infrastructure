package com.redmadrobot.build

import com.redmadrobot.build.internal.android
import org.gradle.api.Project
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.jvm.tasks.Jar
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.getByName
import org.gradle.kotlin.dsl.register

public class AndroidPublishPlugin : PublishPlugin() {

    override fun Project.configureAndroidPublication(): String {
        val sourcesJar = tasks.register<Jar>("sourcesJar") {
            archiveClassifier.set("sources")
            from(android.sourceSets["main"].java.srcDirs)
        }

        // Pre-create publication to make it configurable on signing configuration
        publishing.publications.create<MavenPublication>(PUBLICATION_NAME)

        // Because the components are created only during the afterEvaluate phase, you must
        // configure your publications using the afterEvaluate() lifecycle method.
        afterEvaluate {
            if (version == Project.DEFAULT_VERSION) {
                version = checkNotNull(android.defaultConfig.versionName) {
                    "You should specify either project 'version' or 'android.versionName' for publication."
                }
            }

            publishing {
                publications.getByName<MavenPublication>(PUBLICATION_NAME) {
                    from(components["release"])
                    artifact(sourcesJar.get())
                }
            }
        }

        return PUBLICATION_NAME
    }
}
