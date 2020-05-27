package com.uriyyo.pytest.extensions

import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil

inline fun <reified T : PsiElement> PsiElement.resolve(resolver: (PsiElement) -> T?): T? =
        when (this) {
            is T -> this
            else -> resolver(this)
        }

inline fun <reified T : PsiElement> PsiElement.getParentOrSelf(): T? =
        resolve<T> { PsiTreeUtil.getParentOfType(it, T::class.java) }

inline fun <reified T : PsiElement> PsiElement.getContextOrSelf() =
        resolve<T> { PsiTreeUtil.getContextOfType(it, T::class.java) }

inline fun <reified T : PsiElement> PsiElement.getChild() =
        resolve<T> { PsiTreeUtil.getChildOfType(it, T::class.java) }