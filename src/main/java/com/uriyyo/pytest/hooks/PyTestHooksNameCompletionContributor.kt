package com.uriyyo.pytest.hooks

import com.intellij.codeInsight.completion.*
import com.intellij.icons.AllIcons
import com.intellij.patterns.PlatformPatterns
import com.intellij.util.ProcessingContext
import com.jetbrains.python.codeInsight.completion.PythonLookupElement
import com.jetbrains.python.psi.PyFunction
import com.uriyyo.pytest.extensions.findDeepModule

class PyTestHooksNameCompletionContributor : CompletionContributor() {
    init {
        extend(
                CompletionType.BASIC,
                PlatformPatterns.psiElement().withParent(PyFunction::class.java),
                PyTestHooksNameCompletion()
        )
    }
}

class PyTestHooksNameCompletion : CompletionProvider<CompletionParameters>() {
    override fun addCompletions(
        parameters: CompletionParameters,
        context: ProcessingContext,
        result: CompletionResultSet
    ) {
        if (parameters.position.text.startsWith("pytest_"))
            parameters.position.project.findDeepModule("_pytest.hookspec")
                    .flatMap { it.topLevelFunctions }
                    .mapNotNull { it.name }
                    .forEach { result.addElement(PythonLookupElement(it, false, AllIcons.Nodes.Function)) }
    }
}
