package com.redmadrobot.build.internal.detekt

import org.eclipse.jgit.api.Git
import org.eclipse.jgit.diff.DiffEntry
import org.eclipse.jgit.diff.DiffEntry.ChangeType.*
import org.eclipse.jgit.lib.Repository
import org.eclipse.jgit.storage.file.FileRepositoryBuilder
import org.eclipse.jgit.treewalk.AbstractTreeIterator
import org.eclipse.jgit.treewalk.CanonicalTreeParser
import org.gradle.api.Project
import java.io.File

internal class GitFilesFinder(
    private val repository: Repository,
) {

    fun findChangedFilesOnBranch(branch: String, filterParams: FilterParams = FilterParams()): List<File> {
        return Git(repository).diff()
            .setOldTree(findTargetBranchTree(branch))
            .call()
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
    }

    private fun findTargetBranchTree(branch: String): AbstractTreeIterator {
        val branchCommit = repository.resolve("$branch^{tree}")
        val reader = repository.newObjectReader()

        return CanonicalTreeParser().apply { reset(reader, branchCommit) }
    }

    private fun DiffEntry.ChangeType.toInnerChangeType(): ChangeType {
        return when (this) {
            ADD, COPY -> ChangeType.ADD
            MODIFY, RENAME -> ChangeType.MODIFY
            DELETE -> ChangeType.DELETE
        }
    }

    companion object {

        fun create(project: Project): GitFilesFinder {
            val repository = FileRepositoryBuilder()
                .findGitDir(project.projectDir)
                .build()

            return GitFilesFinder(repository)
        }
    }

    data class FilterParams(
        val changeTypes: List<ChangeType> = emptyList(),
        val fileExtensions: List<String> = emptyList(),
    )

    enum class ChangeType {
        ADD,
        MODIFY,
        DELETE
    }
}
