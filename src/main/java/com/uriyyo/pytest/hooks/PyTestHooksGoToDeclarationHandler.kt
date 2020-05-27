package com.uriyyo.pytest.hooks

import com.intellij.codeInsight.navigation.actions.GotoDeclarationHandlerBase
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiElement
import com.jetbrains.python.psi.PyFunction
import com.uriyyo.pytest.extensions.getHook
import com.uriyyo.pytest.extensions.getParentOrSelf

class PyTestHooksGoToDeclarationHandler : GotoDeclarationHandlerBase() {

    override fun getGotoDeclarationTarget(psiElement: PsiElement?, editor: Editor): PsiElement? =
            psiElement
                    ?.getParentOrSelf<PyFunction>()
                    ?.getHook()

}
