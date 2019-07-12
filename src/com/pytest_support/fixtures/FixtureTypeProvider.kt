package com.pytest_support.fixtures

import com.jetbrains.python.codeInsight.userSkeletons.PyUserSkeletonsTypeProvider
import com.jetbrains.python.psi.PyFile
import com.jetbrains.python.psi.PyReferenceExpression
import com.jetbrains.python.psi.types.PyType
import com.jetbrains.python.psi.types.TypeEvalContext
import com.pytest_support.consts.REQUEST_FIXTURE_NAME
import com.pytest_support.extensions.*

class FixtureTypeProvider : PyUserSkeletonsTypeProvider() {

    override fun getReferenceExpressionType(
            referenceExpression: PyReferenceExpression,
            context: TypeEvalContext
    ): PyType? {
        if (referenceExpression.function === null) {
            return null
        }

        val testFunction = referenceExpression.function!!
        val fixtureName = referenceExpression.name!!

        return if (testFunction.isTest(fixtureName) || testFunction.isFixture(fixtureName)) {
            if (fixtureName == REQUEST_FIXTURE_NAME)
                referenceExpression.project.elementType("FixtureRequest")
            else (testFunction.containingFile as PyFile)
                    .fixtures()
                    .withName(fixtureName)
                    .returnType()
                    .firstOrNull()
        } else null
    }
}
