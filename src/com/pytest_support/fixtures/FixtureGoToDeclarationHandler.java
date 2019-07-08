package com.pytest_support.fixtures;

import com.intellij.codeInsight.navigation.actions.GotoDeclarationHandlerBase;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.PsiElement;
import com.jetbrains.python.psi.PyFile;
import com.pytest_support.utils.FixtureUtils;
import com.pytest_support.utils.StreamUtils;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class FixtureGoToDeclarationHandler extends GotoDeclarationHandlerBase {

    @Nullable
    @Override
    public PsiElement getGotoDeclarationTarget(@Nullable PsiElement psiElement, Editor editor) {
        if (psiElement != null && FixtureUtils.isFixtureReference(psiElement)) {
            String elementText = psiElement.getText();
            return StreamUtils.pyFixturesStream((PyFile) psiElement.getContainingFile())
                    .filter(function -> Objects.equals(function.getName(), elementText))
                    .findFirst()
                    .orElse(null);
        }

        return null;
    }
}
