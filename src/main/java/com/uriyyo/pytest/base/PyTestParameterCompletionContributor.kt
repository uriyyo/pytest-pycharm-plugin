package com.uriyyo.pytest.base

import com.intellij.codeInsight.completion.*
import com.intellij.icons.AllIcons
import com.intellij.patterns.PlatformPatterns
import com.intellij.util.ProcessingContext
import com.jetbrains.python.codeInsight.completion.PythonLookupElement
import com.jetbrains.python.psi.PyFunction
import com.jetbrains.python.psi.PyParameterList
import com.uriyyo.pytest.extensions.contains
import com.uriyyo.pytest.extensions.getParentOrSelf

abstract class PyTestParameterCompletionContributor : CompletionContributor() {
    abstract fun getCompletion(): PyTestArgumentCompletion

    init {
        extend(
                CompletionType.BASIC,
                PlatformPatterns.psiElement().inside(PyParameterList::class.java),
                getCompletion()
        )
    }
}

abstract class PyTestArgumentCompletion : CompletionProvider<CompletionParameters>() {
    override fun addCompletions(
            parameters: CompletionParameters,
            context: ProcessingContext,
            result: CompletionResultSet
    ) {
        parameters
                .position
                .getParentOrSelf<PyFunction>()
                ?.let { pyFunction ->
                    getCompletions(pyFunction)
                            ?.filter { it !in pyFunction.parameterList }
                            ?.forEach {
                                result.addElement(PythonLookupElement(it, false, AllIcons.Nodes.Parameter))
                            }
                }
    }

    abstract fun getCompletions(pyFunction: PyFunction): Sequence<String>?
}