package com.redmadrobot.build.detekt

import org.eclipse.jgit.api.Git
import org.eclipse.jgit.diff.DiffEntry
import org.eclipse.jgit.lib.Repository
import org.eclipse.jgit.storage.file.FileRepositoryBuilder
import org.eclipse.jgit.treewalk.AbstractTreeIterator
import org.eclipse.jgit.treewalk.CanonicalTreeParser
import org.gradle.api.DefaultTask
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.*
import java.io.File
import java.io.Serializable

internal abstract class CollectGitDiffFilesTask : DefaultTask() {

    @get:Input
    abstract val branch: Property<String>

    @get:Input
    @get:Optional
    abstract val filterParams: Property<FilterParams>

    @get:InputDirectory
    abstract val projectDir: DirectoryProperty

    @get:Internal
    abstract val outputFiles: ConfigurableFileCollection

    @TaskAction
    fun find() {
        val projectDir = projectDir.get().asFile

        val repository = FileRepositoryBuilder()
            .findGitDir(projectDir)
            .build()

        val branch = branch.get()
        val filterParams = filterParams.getOrElse(FilterParams())

        val changedFilesPaths = repository.findAllChangedFiles(branch)
            .filter { entry ->
                filterParams.changeTypes.isEmpty() ||
                    entry.changeType.toInnerChangeType() in filterParams.changeTypes
            }
            .map(DiffEntry::getNewPath)
            .filter { path ->
                filterParams.fileExtensions.isEmpty() ||
                    filterParams.fileExtensions.any(path::endsWith)
            }
            .map(::File)

        outputFiles.setFrom(changedFilesPaths)
    }

    private fun Repository.findAllChangedFiles(target: String): List<DiffEntry> {
        return Git(this).diff()
            .setOldTree(findBaseBranchTree(target))
            .call()
    }

    private fun Repository.findBaseBranchTree(branch: String): AbstractTreeIterator {
        val branchCommit = resolve("$branch^{tree}")
        val reader = newObjectReader()

        return CanonicalTreeParser().apply { reset(reader, branchCommit) }
    }

    private fun DiffEntry.ChangeType.toInnerChangeType(): ChangeType {
        return when (this) {
            DiffEntry.ChangeType.ADD, DiffEntry.ChangeType.COPY -> ChangeType.ADD
            DiffEntry.ChangeType.MODIFY, DiffEntry.ChangeType.RENAME -> ChangeType.MODIFY
            DiffEntry.ChangeType.DELETE -> ChangeType.DELETE
        }
    }

    data class FilterParams(
        val changeTypes: List<ChangeType> = emptyList(),
        val fileExtensions: Set<String> = emptySet(),
    ) : Serializable {

        companion object {
            const val serialVersionUID = 1L
        }
    }

    enum class ChangeType {
        ADD,
        MODIFY,
        DELETE
    }
}
