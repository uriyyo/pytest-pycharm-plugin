package com.pytest_support.fixtures

import com.intellij.codeInsight.completion.*
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.util.ProcessingContext
import com.jetbrains.python.psi.PyFile
import com.pytest_support.consts.functionParameter
import com.pytest_support.extensions.fixtures
import com.pytest_support.extensions.isTestOrFixtureParameter
import com.pytest_support.extensions.names
import icons.PythonIcons


val REQUEST_ELEMENT: LookupElementBuilder = LookupElementBuilder
        .create("request")
        .withIcon(PythonIcons.Python.Function)


class FixtureNamesCompletionContributor : CompletionContributor() {
    init {
        this.extend(
                CompletionType.BASIC,
                functionParameter,
                FixtureCompletionProvider()
        )
    }

    private class FixtureCompletionProvider : CompletionProvider<CompletionParameters>() {

        override fun addCompletions(
                completionParameters: CompletionParameters,
                processingContext: ProcessingContext,
                result: CompletionResultSet
        ) {
            if (completionParameters.position.isTestOrFixtureParameter()) {
                result.addElement(REQUEST_ELEMENT)

                (completionParameters.originalFile as PyFile)
                        .fixtures()
                        .names()
                        .map {
                            LookupElementBuilder
                                    .create(it)
                                    .withIcon(PythonIcons.Python.Function)
                        }
                        .forEach { result.addElement(it) }

                result.stopHere()
            }
        }
    }
}
