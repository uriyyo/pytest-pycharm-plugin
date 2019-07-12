package com.pytest_support.extensions

import com.jetbrains.python.psi.PyFile
import com.jetbrains.python.psi.PyFunction
import com.pytest_support.consts.PYTEST_FILES


fun PyFile.fixtures(): Sequence<PyFunction> = sequence {
    yieldAll(topLevelFunctions)
    containingDirectory
            .conftests()
            .forEach { yieldAll(it.topLevelFunctions.asSequence().fixtures()) }

    val rootPath = containingDirectory.testRoot?.path ?: ""

    project
            .pyFiles()
            .filter { file -> !file.virtualFile.path.startsWith(rootPath) }
            .map { it.topLevelFunctions }
            .flatten()
            .fixtures()
            .forEach { yield(it) }
}


fun PyFile.functions(): Sequence<PyFunction> = sequence {
    yieldAll(topLevelFunctions)
    topLevelClasses.forEach { yieldAll(it.methods.toList()) }
}

val PyFile.isPytestFile: Boolean
    get() = name in PYTEST_FILES

val PyFile.isTestFile: Boolean
    get() = name.startsWith("test_")