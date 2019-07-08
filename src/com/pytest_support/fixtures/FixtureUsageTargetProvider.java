package com.pytest_support.fixtures;

import com.intellij.find.findUsages.FindUsagesHandler;
import com.intellij.find.findUsages.FindUsagesHandlerFactory;
import com.intellij.psi.PsiElement;
import com.jetbrains.python.psi.PyFunction;
import com.pytest_support.utils.FixtureUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class FixtureUsageTargetProvider extends FindUsagesHandlerFactory {
    @Override
    public boolean canFindUsages(@NotNull PsiElement psiElement) {
        if (psiElement instanceof PyFunction) {
            return FixtureUtils.isFixture((PyFunction) psiElement);
        }
        return false;
    }


    @Nullable
    @Override
    public FindUsagesHandler createFindUsagesHandler(@NotNull PsiElement psiElement, boolean b) {
        return new FindUsagesHandler(psiElement) {
            @NotNull
            @Override
            public PsiElement[] getPrimaryElements() {
                String fixtureName = ((PyFunction) getPsiElement()).getName();
                if (fixtureName == null) {
                    return new PsiElement[]{};
                }

                return FixtureUtils
                        .pyFileTopLevelFunctionsAndClassMethods(getProject())
                        .filter(FixtureUtils::isTestFunctionOrFixture)
                        .map(function -> function.getParameterList().findParameterByName(fixtureName))
                        .filter(Objects::nonNull)
                        .distinct()
                        .toArray(PsiElement[]::new);
            }
        };
    }


}
