package com.pytest_support.utils

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import com.jetbrains.python.psi.LanguageLevel
import com.jetbrains.python.psi.PyElementGenerator
import com.jetbrains.python.psi.PyFunction
import com.jetbrains.python.psi.types.PyType
import com.pytest_support.extensions.returnType

object GenerateUtils {
    fun createInstanceCode(module: String, component: String): String =
            "from $module import $component\ndef _():return $component()"

    fun createModuleCode(module: String): String = "import $module\ndef _():return $module"

    fun <T : PsiElement, R : PyType> generateCode(
            project: Project, code: String, generateType: Class<T>, returnType: Class<R>
    ): R? =
            PyElementGenerator
                    .getInstance(project)
                    .createFromText(LanguageLevel.PYTHON36, generateType, code)
                    .let { PsiTreeUtil.getChildOfType(it.parent, PyFunction::class.java) }
                    ?.returnType as? R
}
