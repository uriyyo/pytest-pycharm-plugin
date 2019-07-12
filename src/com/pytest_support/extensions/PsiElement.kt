package com.pytest_support.extensions

import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import com.jetbrains.python.psi.PyFile
import com.jetbrains.python.psi.PyFunction
import com.pytest_support.consts.functionParameter

fun PsiElement.isFixtureReference(): Boolean = functionParameter.accepts(this)

fun PsiElement.isTestOrFixtureParameter(): Boolean = function.let { it.isTest || it.isFixture }

val PsiElement.isInsideTestFile: Boolean
    get() = (containingFile.originalFile as? PyFile)?.let { it.isTestFile || it.isPytestFile } ?: false

val PsiElement.function: PyFunction?
    get() = PsiTreeUtil.getContextOfType(this, PyFunction::class.java)