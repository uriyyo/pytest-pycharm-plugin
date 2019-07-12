package com.pytest_support.extensions

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiNamedElement
import com.intellij.util.toArray
import com.jetbrains.python.psi.PyFunction

fun Sequence<PyFunction>.fixtures() = filter { it.isFixture }

fun Sequence<PyFunction>.fixturesOrTests() = filter { it.isTest || it.isFixture }

fun Sequence<PyFunction>.toParameter(name: String) = mapNotNull { it.parameterList.findParameterByName(name) }

fun Sequence<PsiElement>.toPsiArray() = toList().toArray(emptyArray())

fun Sequence<PsiNamedElement>.names() = mapNotNull { it.name }

fun Sequence<PyFunction>.withName(name: String?) = filter { it.name == name }

fun Sequence<PyFunction>.returnType() = mapNotNull { it.returnType }