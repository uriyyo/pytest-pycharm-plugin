package com.uriyyo.pytest.extensions

import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.process.ProcessOutput
import com.intellij.openapi.module.ModuleUtilCore
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile
import com.jetbrains.extensions.python.toPsi
import com.jetbrains.python.psi.PyClass
import com.jetbrains.python.psi.PyFile
import com.jetbrains.python.psi.stubs.PyClassNameIndex
import com.jetbrains.python.psi.stubs.PyModuleNameIndex
import com.jetbrains.python.sdk.PySdkUtil
import com.jetbrains.python.sdk.PythonSdkType
import java.io.File

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

fun Project.execPythonCommand(
        vararg args: String,
        extraEnv: Map<String, String>? = null,
        timeout: Int = 5000,
        stdin: ByteArray? = null,
        needEOFMarker: Boolean = false
): ProcessOutput? {
    val sdk = this.projectFile
            ?.toPsi(this)
            ?.let { ModuleUtilCore.findModuleForFile(it as PsiFile) }
            ?.let { PythonSdkType.findLocalCPython(it) }
            ?: return null

    val cmd = GeneralCommandLine(*args)
    cmd.withExePath(sdk.homePath ?: return null)

    try {
        return PySdkUtil.getProcessOutput(
                cmd,
                File(sdk.homePath ?: return null).parent,
                extraEnv,
                timeout,
                stdin,
                needEOFMarker
        )
    } catch (e: Exception) {
        return null
    }

}