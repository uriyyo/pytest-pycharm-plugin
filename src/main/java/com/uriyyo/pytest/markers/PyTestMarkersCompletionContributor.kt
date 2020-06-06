package com.uriyyo.pytest.markers

import com.intellij.codeInsight.completion.*
import com.intellij.icons.AllIcons
import com.intellij.patterns.PlatformPatterns
import com.intellij.util.ProcessingContext
import com.jetbrains.python.codeInsight.completion.PythonLookupElement
import com.jetbrains.python.psi.PyDecorator
import com.uriyyo.pytest.collectors.collectMarkers
import com.uriyyo.pytest.extensions.getContextOrSelf

class PyTestMarkersCompletionContributor : CompletionContributor() {
    init {
        extend(
                CompletionType.BASIC,
                PlatformPatterns.psiElement().inside(PyDecorator::class.java),
                PyTestMarkersCompletionProvider()
        )
    }
}

class PyTestMarkersCompletionProvider : CompletionProvider<CompletionParameters>() {
    override fun addCompletions(parameters: CompletionParameters, context: ProcessingContext, result: CompletionResultSet) {
        parameters
                .position
                .getContextOrSelf<PyDecorator>()
                ?.takeIf { it.qualifiedName?.components?.contains("mark") == true }
                ?.project
                ?.let { collectMarkers(it) }
                ?.forEach { result.addElement(PythonLookupElement(it, false, AllIcons.Nodes.Method)) }
    }
}