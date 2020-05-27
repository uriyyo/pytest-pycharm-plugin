package com.uriyyo.pytest.base

import com.jetbrains.python.psi.PyElement
import com.jetbrains.python.psi.PyReferenceExpression
import com.jetbrains.python.psi.types.PyType
import com.jetbrains.python.psi.types.PyTypeProviderBase
import com.jetbrains.python.psi.types.TypeEvalContext

abstract class PyTestTypeProvider : PyTypeProviderBase() {

    override fun getReferenceExpressionType(
            reference: PyReferenceExpression,
            context: TypeEvalContext
    ): PyType? =
            when (val element = reference.originalElement) {
                is PyElement -> getType(element, context)
                else -> null
            }

    abstract fun getType(element: PyElement, context: TypeEvalContext): PyType?
}