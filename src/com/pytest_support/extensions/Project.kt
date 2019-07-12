package com.pytest_support.extensions

import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VfsUtilCore
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.VirtualFileVisitor
import com.intellij.psi.PsiManager
import com.jetbrains.python.codeInsight.imports.PythonImportUtils
import com.jetbrains.python.psi.*
import com.jetbrains.python.psi.types.PyModuleType
import com.jetbrains.python.psi.types.PyType
import com.pytest_support.utils.GenerateUtils


fun allChildes(virtualFile: VirtualFile): List<VirtualFile> =
        mutableListOf(*virtualFile.children)
                .also {
                    VfsUtilCore.visitChildrenRecursively(virtualFile, object : VirtualFileVisitor<Any>() {
                        override fun visitFile(file: VirtualFile): Boolean {
                            it.add(file)
                            return true
                        }
                    })
                }

fun Project.pyFiles(): Sequence<PyFile> =
        baseDir
                .children
                .asSequence()
                .map { allChildes(it) }
                .flatten()
                .map { PsiManager.getInstance(this).findFile(it) }
                .filterIsInstance<PyFile>()

fun getElementType(element: PyElement, name: String, module: String): PyType? {
    val reference = element.reference ?: return null

    val autoImportQuickFix = PythonImportUtils.proposeImportFix(element, reference) ?: return null

    return autoImportQuickFix
            .candidates
            .asSequence()
            .map { it.path }
            .mapNotNull { it.toString() }
            .filter { module in it || "builtin" in it }
            .map { element.project.instanceType(it, name) }
            .firstOrNull()
}

fun Project.elementType(name: String): PyType? =
        PyElementGenerator.getInstance(this)
                .createFromText(
                        LanguageLevel.PYTHON36,
                        PyExpressionStatement::class.java,
                        name
                )
                .let { getElementType(it.expression, name, "_pytest") }

fun Project.instanceType(module: String, component: String): PyType? =
        GenerateUtils.generateCode(
                this,
                GenerateUtils.createInstanceCode(module, component),
                PyFromImportStatement::class.java
        )

fun Project.moduleType(module: String): PyModuleType? =
        GenerateUtils.generateCode(
                this,
                GenerateUtils.createModuleCode(module),
                PyImportStatement::class.java
        )

fun Project.moduleFunctions(module: String): List<PyFunction> =
        moduleType(module)
                ?.module
                ?.topLevelFunctions
                ?: emptyList()


fun Project.allFixtureAndTests() =
        pyFiles()
                .flatMap { it.functions() }
                .fixturesOrTests()