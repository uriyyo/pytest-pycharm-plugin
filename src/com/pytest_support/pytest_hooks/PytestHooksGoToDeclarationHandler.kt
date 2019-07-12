package com.pytest_support.pytest_hooks

import com.intellij.codeInsight.navigation.actions.GotoDeclarationHandlerBase
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiElement
import com.pytest_support.extensions.isInsideTestFile
import com.pytest_support.extensions.moduleFunctions

class PytestHooksGoToDeclarationHandler : GotoDeclarationHandlerBase() {
    override fun getGotoDeclarationTarget(psiElement: PsiElement?, editor: Editor): PsiElement? {
        if (psiElement?.isInsideTestFile == true) {
            val text = psiElement.parent!!.text

            return psiElement
                    .project
                    .moduleFunctions("_pytest.hookspec")
                    .firstOrNull { it.name == text }
        }

        return null
    }
}
