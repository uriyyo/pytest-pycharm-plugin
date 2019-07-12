package com.pytest_support.pytest_hooks

import com.jetbrains.python.codeInsight.userSkeletons.PyUserSkeletonsTypeProvider
import com.jetbrains.python.psi.PyReferenceExpression
import com.jetbrains.python.psi.types.PyType
import com.jetbrains.python.psi.types.TypeEvalContext
import com.pytest_support.consts.hookTypes
import com.pytest_support.consts.inFunctionElement
import com.pytest_support.extensions.elementType
import com.pytest_support.extensions.function
import com.pytest_support.extensions.isInsideTestFile

class PytestHooksTypeProvider : PyUserSkeletonsTypeProvider() {

    override fun getReferenceExpressionType(
            referenceExpression: PyReferenceExpression,
            context: TypeEvalContext
    ): PyType? =
            referenceExpression
                    .function
                    ?.let {
                        if (
                                inFunctionElement.accepts(referenceExpression) &&
                                referenceExpression.isInsideTestFile
                        )
                            hookTypes[referenceExpression.name]
                                    ?.let { referenceExpression.project.elementType(it) }
                        else null
                    }
}
