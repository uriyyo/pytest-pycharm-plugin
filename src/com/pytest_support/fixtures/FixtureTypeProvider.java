package com.pytest_support.fixtures;

import com.jetbrains.python.codeInsight.userSkeletons.PyUserSkeletonsTypeProvider;
import com.jetbrains.python.psi.PyFile;
import com.jetbrains.python.psi.PyFunction;
import com.jetbrains.python.psi.PyReferenceExpression;
import com.jetbrains.python.psi.types.PyType;
import com.jetbrains.python.psi.types.TypeEvalContext;
import com.pytest_support.consts.PytestConsts;
import com.pytest_support.utils.FixtureUtils;
import com.pytest_support.utils.StreamUtils;
import com.pytest_support.utils.TypeUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class FixtureTypeProvider extends PyUserSkeletonsTypeProvider {

    @Nullable
    @Override
    public PyType getReferenceExpressionType(
            @NotNull PyReferenceExpression referenceExpression,
            @NotNull TypeEvalContext context
    ) {
        PyFunction testFunction = FixtureUtils.getFunctionFromElement(referenceExpression);
        String fixtureName = referenceExpression.getName();

        if (FixtureUtils.isTestFunctionOrFixture(testFunction, fixtureName)) {
            if (Objects.equals(fixtureName, PytestConsts.REQUEST_FIXTURE_NAME)) {
                return FixtureUtils.getFixtureRequestType(referenceExpression.getProject());
            }

            return StreamUtils.pyFixturesStream((PyFile) testFunction.getContainingFile())
                    .filter(function -> Objects.equals(function.getName(), fixtureName))
                    .filter(FixtureUtils::isFixture)
                    .map(TypeUtils::getFunctionReturnType)
                    .filter(Objects::nonNull)
                    .findFirst()
                    .orElse(null);
        }
        return null;
    }
}
