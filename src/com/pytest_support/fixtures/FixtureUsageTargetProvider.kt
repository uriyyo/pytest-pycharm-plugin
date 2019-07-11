package com.pytest_support.fixtures

import com.intellij.find.findUsages.FindUsagesHandler
import com.intellij.find.findUsages.FindUsagesHandlerFactory
import com.intellij.psi.PsiElement
import com.jetbrains.python.psi.PyFunction
import com.pytest_support.extensions.allFixtureAndTests
import com.pytest_support.extensions.toParameter
import com.pytest_support.extensions.toPsiArray
import com.pytest_support.utils.FixtureUtils

class FixtureUsageTargetProvider : FindUsagesHandlerFactory() {
    override fun canFindUsages(psiElement: PsiElement): Boolean =
            if (psiElement is PyFunction) FixtureUtils.isFixture(psiElement) else false

    override fun createFindUsagesHandler(psiElement: PsiElement, b: Boolean): FindUsagesHandler? {
        return object : FindUsagesHandler(psiElement) {
            override fun getPrimaryElements(): Array<PsiElement> {
                val fixtureName = (getPsiElement() as PyFunction).name ?: return emptyArray()

                return project
                        .allFixtureAndTests()
                        .toParameter(fixtureName)
                        .distinct()
                        .toPsiArray()
            }
        }
    }


}
