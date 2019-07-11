package com.pytest_support.fixtures

import com.jetbrains.python.codeInsight.userSkeletons.PyUserSkeletonsTypeProvider
import com.jetbrains.python.psi.PyFile
import com.jetbrains.python.psi.PyReferenceExpression
import com.jetbrains.python.psi.types.PyType
import com.jetbrains.python.psi.types.TypeEvalContext
import com.pytest_support.consts.PytestConsts
import com.pytest_support.extensions.fixtures
import com.pytest_support.extensions.returnType
import com.pytest_support.extensions.withName
import com.pytest_support.utils.FixtureUtils

class FixtureTypeProvider : PyUserSkeletonsTypeProvider() {

    override fun getReferenceExpressionType(
            referenceExpression: PyReferenceExpression,
            context: TypeEvalContext
    ): PyType? {
        val testFunction = FixtureUtils.getFunctionFromElement(referenceExpression)
        val fixtureName = referenceExpression.name

        return if (FixtureUtils.isTestFunctionOrFixture(testFunction, fixtureName)) {

            if (fixtureName == PytestConsts.REQUEST_FIXTURE_NAME)
                FixtureUtils.getFixtureRequestType(referenceExpression.project)
            else (testFunction.containingFile as PyFile)
                    .fixtures()
                    .withName(fixtureName)
                    .returnType()
                    .first()

        } else null
    }
}
