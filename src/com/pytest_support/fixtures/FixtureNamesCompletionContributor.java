package com.pytest_support.fixtures;

import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.util.ProcessingContext;
import com.jetbrains.python.psi.PyFile;
import com.jetbrains.python.psi.PyFunction;
import com.pytest_support.consts.PytestConsts;
import com.pytest_support.utils.FixtureUtils;
import com.pytest_support.utils.StreamUtils;
import icons.PythonIcons;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class FixtureNamesCompletionContributor extends CompletionContributor {
    public FixtureNamesCompletionContributor() {
        this.extend(
                CompletionType.BASIC,
                PytestConsts.functionParameter,
                new FixtureCompletionProvider()
        );
    }

    private static class FixtureCompletionProvider extends CompletionProvider<CompletionParameters> {

        @Override
        protected void addCompletions(
                @NotNull CompletionParameters completionParameters,
                ProcessingContext processingContext,
                @NotNull CompletionResultSet result) {

            if (FixtureUtils.isTestOrFixtureParameter(completionParameters.getPosition())) {
                result.addElement(LookupElementBuilder.create("request").withIcon(PythonIcons.Python.Function));
                StreamUtils.pyFixturesStream((PyFile) completionParameters.getOriginalFile())
                        .filter(FixtureUtils::isFixture)
                        .map(PyFunction::getName)
                        .filter(Objects::nonNull)
                        .forEach(name -> result.addElement(
                                LookupElementBuilder.create(name).withIcon(PythonIcons.Python.Function)
                        ));
                result.stopHere();
            }
        }
    }
}
