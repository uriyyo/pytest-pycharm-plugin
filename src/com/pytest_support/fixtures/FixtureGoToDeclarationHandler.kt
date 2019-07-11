package com.pytest_support.fixtures

import com.intellij.codeInsight.navigation.actions.GotoDeclarationHandlerBase
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiElement
import com.jetbrains.python.psi.PyFile
import com.pytest_support.extensions.fixtures
import com.pytest_support.extensions.isFixtureReference
import com.pytest_support.utils.FixtureUtils
import com.pytest_support.utils.StreamUtils
import kotlin.streams.asSequence

class FixtureGoToDeclarationHandler : GotoDeclarationHandlerBase() {

    override fun getGotoDeclarationTarget(psiElement: PsiElement?, editor: Editor): PsiElement? =
            if (psiElement?.isFixtureReference() == true)
                (psiElement.containingFile as PyFile)
                        .fixtures()
                        .firstOrNull { it.name == psiElement.text }
            else null
}
