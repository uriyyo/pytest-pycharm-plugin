package com.pytest_support.extensions

import com.intellij.psi.PsiElement
import com.pytest_support.utils.FixtureUtils

fun PsiElement.isFixtureReference(): Boolean = FixtureUtils.isFixtureReference(this)

fun PsiElement.isTestOrFixtureParameter(): Boolean = FixtureUtils.isTestOrFixtureParameter(this)