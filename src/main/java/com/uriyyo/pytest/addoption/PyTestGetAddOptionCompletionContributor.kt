package com.uriyyo.pytest.addoption

import com.intellij.codeInsight.completion.*
import com.intellij.icons.AllIcons
import com.intellij.patterns.PlatformPatterns
import com.intellij.util.ProcessingContext
import com.jetbrains.python.codeInsight.completion.PythonLookupElement
import com.jetbrains.python.psi.PyCallExpression
import com.jetbrains.python.psi.PyStringLiteralExpression
import com.uriyyo.pytest.collectors.collectAddOptions
import com.uriyyo.pytest.extensions.getContextOrSelf

class PyTestGetAddOptionCompletionContributor : CompletionContributor() {
    init {
        extend(
                CompletionType.BASIC,
                PlatformPatterns.psiElement()
                        .inside(PyCallExpression::class.java)
                        .and(PlatformPatterns.psiElement().inside(PyStringLiteralExpression::class.java)),
                PyTestGetAddOptionCompletionProvider()
        )
    }
}

class PyTestGetAddOptionCompletionProvider : CompletionProvider<CompletionParameters>() {
    override fun addCompletions(parameters: CompletionParameters, context: ProcessingContext, result: CompletionResultSet) {
        parameters
                .position
                .getContextOrSelf<PyCallExpression>()
                .takeIf { it?.callee?.name == "getoption" } // TODO: should be filtered using PlatformPatterns
                ?.project
                ?.let { collectAddOptions(it) }
                ?.forEach { result.addElement(PythonLookupElement(it, false, AllIcons.Nodes.Parameter)) }
    }
}