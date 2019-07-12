package com.pytest_support.extensions

import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiDirectory
import com.jetbrains.python.psi.PyFile
import com.pytest_support.consts.CONFTEST


val PsiDirectory.testRoot: VirtualFile?
    get() = sequence {
        var file: VirtualFile? = virtualFile
        while (file !== null) {
            yield(file)
            file = virtualFile.parent
        }
    }.lastOrNull { it?.parent != project.baseDir }

fun PsiDirectory.conftests(): Sequence<PyFile> = sequence {
    if (project.baseDir != virtualFile) {
        findFile(CONFTEST)?.also { yield(it as PyFile) }
        parentDirectory?.also { yieldAll(it.conftests()) }
    }
}