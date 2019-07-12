package com.pytest_support.pytest_hooks

import com.intellij.codeInsight.completion.*
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.util.ProcessingContext
import com.jetbrains.python.psi.PyFunction
import com.pytest_support.consts.functionDefinition
import com.pytest_support.extensions.isInsideTestFile
import com.pytest_support.extensions.moduleFunctions
import icons.PythonIcons
import java.util.regex.Pattern

class PytestHooksCompletionContributor : CompletionContributor() {
    init {
        this.extend(
                CompletionType.BASIC,
                functionDefinition,
                PytestHooksCompletionProvider()
        )
    }

    private class PytestHooksCompletionProvider : CompletionProvider<CompletionParameters>() {

        override fun addCompletions(
                completionParameters: CompletionParameters,
                processingContext: ProcessingContext,
                result: CompletionResultSet
        ) {
            val psiElement = completionParameters.position.originalElement

            if (psiElement?.let { it.isInsideTestFile && functionDefinition.accepts(it) } == true) {
                completionParameters
                        .position
                        .project
                        .moduleFunctions("_pytest.hookspec")
                        .mapNotNull { createFunctionDeclaration(it) }
                        .let { result.addAllElements(it) }

            }
        }

        companion object {
            private val functionDefinitionRegex = Pattern
                    .compile("(?<=def\\s)([\\w_]+(.+))(?=:)", Pattern.MULTILINE)

            fun createFunctionDeclaration(function: PyFunction): LookupElement? {
                val matcher = functionDefinitionRegex.matcher(function.text)
                val code = if (matcher.find()) matcher.group(0) else ""

                return LookupElementBuilder
                        .create("$code:\n    ")
                        .withPresentableText(function.name!!)
                        .withIcon(PythonIcons.Python.Function)
            }
        }
    }
}
