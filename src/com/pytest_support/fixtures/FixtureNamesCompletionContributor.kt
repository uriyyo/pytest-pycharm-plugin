package com.pytest_support.fixtures

import com.intellij.codeInsight.completion.*
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.util.ProcessingContext
import com.jetbrains.python.psi.PyFile
import com.pytest_support.consts.PytestConsts
import com.pytest_support.utils.FixtureUtils
import com.pytest_support.utils.StreamUtils
import com.pytest_support.extensions.fixtures
import com.pytest_support.extensions.isTestOrFixtureParameter
import com.pytest_support.extensions.names
import icons.PythonIcons
import kotlin.streams.asSequence

class FixtureNamesCompletionContributor : CompletionContributor() {
    init {
        this.extend(
                CompletionType.BASIC,
                PytestConsts.functionParameter,
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
                result.addElement(LookupElementBuilder.create("request").withIcon(PythonIcons.Python.Function))

                (completionParameters.originalFile as PyFile)
                        .fixtures()
                        .names()
                        .forEach { name ->
                            result.addElement(
                                    LookupElementBuilder
                                            .create(name)
                                            .withIcon(PythonIcons.Python.Function)
                            )
                        }
                result.stopHere()
            }
        }
    }
}
