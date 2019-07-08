package com.pytest_support.pytest_hooks;

import com.intellij.codeInsight.navigation.actions.GotoDeclarationHandlerBase;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.PsiElement;
import com.pytest_support.utils.PytestUtils;
import com.pytest_support.utils.TypeUtils;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class PytestHooksGoToDeclarationHandler extends GotoDeclarationHandlerBase {
    @Nullable
    @Override
    public PsiElement getGotoDeclarationTarget(@Nullable PsiElement psiElement, Editor editor) {
        if (psiElement != null && PytestUtils.isTestOrPytestFile(psiElement)) {
            psiElement = psiElement.getParent();
            String text = psiElement.getText();

            return TypeUtils.getModuleFunctions(psiElement.getProject(), "_pytest.hookspec")
                    .stream()
                    .filter(function -> Objects.equals(function.getName(), text))
                    .findFirst()
                    .orElse(null);
        }
        return null;
    }
}
