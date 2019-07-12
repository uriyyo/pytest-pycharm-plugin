package com.pytest_support.extensions

import com.intellij.psi.util.PsiTreeUtil
import com.jetbrains.python.psi.PyFunction
import com.jetbrains.python.psi.PyReturnStatement
import com.jetbrains.python.psi.PyYieldExpression
import com.jetbrains.python.psi.types.PyType
import com.jetbrains.python.psi.types.TypeEvalContext
import com.pytest_support.consts.DECARATORS

val PyFunction?.isFixture: Boolean
    get() = this
            ?.decoratorList
            ?.let { decoratorList -> DECARATORS.any { decoratorList.findDecorator(it) !== null } }
            ?: false

fun PyFunction.isFixture(parameterName: String): Boolean =
        isFixture && containsParameter(parameterName)

val PyFunction?.isTest: Boolean
    get() = this
            ?.name
            ?.startsWith("test_")
            ?: false

fun PyFunction.isTest(parameterName: String): Boolean =
        isTest && containsParameter(parameterName)

fun PyFunction.containsParameter(parameterName: String): Boolean =
        parameterList.findParameterByName(parameterName) !== null

val PyFunction.returnType: PyType?
    get() {
        val evalContext = TypeEvalContext.deepCodeInsight(project)

        if (isGenerator) {
            return PsiTreeUtil.collectElementsOfType(function, PyYieldExpression::class.java)
                    .mapNotNull { evalContext.getType(it) }
                    .firstOrNull()
        }

        return PsiTreeUtil.collectElementsOfType(function, PyReturnStatement::class.java)
                .toList()
                .asReversed()
                .mapNotNull { it.expression }
                .mapNotNull { evalContext.getType(it) }
                .firstOrNull()
    }