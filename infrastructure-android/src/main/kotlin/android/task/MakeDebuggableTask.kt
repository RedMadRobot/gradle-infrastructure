package com.redmadrobot.build.android.task

import org.gradle.api.DefaultTask
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction

/** Adds `android:debuggable="true"` to the manifest. */
public abstract class MakeDebuggableTask : DefaultTask() {

    /** The merged manifest file. */
    @get:InputFile
    public abstract val mergedManifest: RegularFileProperty

    /** Resulting manifest with debuggable flag set to true. */
    @get:OutputFile
    public abstract val debuggableManifest: RegularFileProperty

    @TaskAction
    internal fun addDebuggableTag() {
        var manifest = mergedManifest.get().asFile.readText()
        manifest = if ("android:debuggable" in manifest) {
            manifest.replace(
                oldValue = "android:debuggable=\"false\"",
                newValue = "android:debuggable=\"true\"",
            )
        } else {
            manifest.replace(
                oldValue = "<application",
                newValue = "<application\n        android:debuggable=\"true\""
            )
        }

        debuggableManifest.get().asFile.writeText(manifest)
    }
}
