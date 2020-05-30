package com.uriyyo.pytest.extensions

import com.intellij.openapi.project.Project
import com.jetbrains.python.psi.PyClass
import com.jetbrains.python.psi.PyFile
import com.jetbrains.python.psi.stubs.PyClassNameIndex
import com.jetbrains.python.psi.stubs.PyModuleNameIndex

fun Project.findClass(name: String, includeNonProjectItems: Boolean = true): Collection<PyClass> =
        PyClassNameIndex.find(name, this, includeNonProjectItems)

fun Project.findModule(name: String, includeNonProjectItems: Boolean = true): Collection<PyFile> =
        PyModuleNameIndex.find(name, this, includeNonProjectItems)

fun Project.findDeepModule(name: String): Collection<PyFile> {
    if ("." !in name)
        return findModule(name)

    val parts = name.split('.').toMutableList()
    val parent = parts.removeAt(0)
    val fileName = parts.removeAt(parts.size - 1)

    return findModule(parent)
            .map {
                parts.fold(it.containingDirectory) { acc, name -> acc?.findSubdirectory(name) }
            }
            .map { it?.findFile("$fileName.py") }
            .filterIsInstance<PyFile>()
}
