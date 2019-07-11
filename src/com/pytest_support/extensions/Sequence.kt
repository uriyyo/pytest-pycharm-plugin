package com.pytest_support.extensions

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiNamedElement
import com.intellij.util.toArray
import com.jetbrains.python.psi.PyFunction
import com.pytest_support.utils.FixtureUtils
import com.pytest_support.utils.TypeUtils

fun Sequence<PyFunction>.fixtures() = filter { FixtureUtils.isFixture(it) }

fun Sequence<PyFunction>.fixturesOrTests() = filter { FixtureUtils.isTestFunctionOrFixture(it) }

fun Sequence<PyFunction>.toParameter(name: String) = mapNotNull { it.parameterList.findParameterByName(name) }

fun Sequence<PsiElement>.toPsiArray() = toList().toArray(emptyArray())

fun Sequence<PsiNamedElement>.names() = mapNotNull { it.name }

fun Sequence<PyFunction>.withName(name: String?) = filter { it.name == name }

fun Sequence<PyFunction>.returnType() = mapNotNull { TypeUtils.getFunctionReturnType(it) }