package com.pytest_support.pytest_hooks;

import com.jetbrains.python.codeInsight.userSkeletons.PyUserSkeletonsTypeProvider;
import com.jetbrains.python.psi.PyFunction;
import com.jetbrains.python.psi.PyReferenceExpression;
import com.jetbrains.python.psi.types.PyType;
import com.jetbrains.python.psi.types.TypeEvalContext;
import com.pytest_support.consts.PytestConstsKt;
import com.pytest_support.utils.FixtureUtils;
import com.pytest_support.utils.PytestUtils;
import com.pytest_support.utils.TypeUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class PytestHooksTypeProvider extends PyUserSkeletonsTypeProvider {

    @Nullable
    @Override
    public PyType getReferenceExpressionType(
            @NotNull PyReferenceExpression referenceExpression,
            @NotNull TypeEvalContext context
    ) {
        PyFunction testFunction = FixtureUtils.getFunctionFromElement(referenceExpression);

        if (testFunction != null && PytestConstsKt.getInFunctionElement().accepts(referenceExpression)
                && PytestUtils.isTestOrPytestFile(referenceExpression)) {
            String name = referenceExpression.getName();

            return Optional.ofNullable(PytestConstsKt.getHookTypes().getOrDefault(name, null))
                    .map(function -> TypeUtils.getElementType(referenceExpression.getProject(), function))
                    .orElse(null);
        }
        return null;
    }
}
