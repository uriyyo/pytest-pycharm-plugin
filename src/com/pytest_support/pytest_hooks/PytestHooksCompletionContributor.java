package com.pytest_support.pytest_hooks;

import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.psi.PsiElement;
import com.intellij.util.ProcessingContext;
import com.jetbrains.python.psi.PyFunction;
import com.pytest_support.consts.PytestConsts;
import com.pytest_support.utils.PytestUtils;
import com.pytest_support.utils.TypeUtils;
import icons.PythonIcons;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class PytestHooksCompletionContributor extends CompletionContributor {
    public PytestHooksCompletionContributor() {
        this.extend(
                CompletionType.BASIC,
                PytestConsts.functionDefinition,
                new PytestHooksCompletionProvider()
        );
    }

    private static class PytestHooksCompletionProvider extends CompletionProvider<CompletionParameters> {
        private static Pattern functionDefinition = Pattern.compile(
                "(?<=def\\s)([\\w_]+(.+))(?=:)", Pattern.MULTILINE
        );

        public static LookupElement createFunctionDeclaration(PyFunction function) {
            String name = function.getName();
            if (name == null) {
                return null;
            }

            Matcher matcher = functionDefinition.matcher(function.getText());
            String code = matcher.find() ? matcher.group(0) : "";

            return LookupElementBuilder
                    .create(code + ":\n    ")
                    .withPresentableText(function.getName())
                    .withIcon(PythonIcons.Python.Function);
        }

        @Override
        protected void addCompletions(
                @NotNull CompletionParameters completionParameters,
                ProcessingContext processingContext,
                @NotNull CompletionResultSet result
        ) {
            PsiElement psiElement = completionParameters.getPosition().getOriginalElement();
            if (psiElement != null && PytestConsts.functionDefinition.accepts(psiElement)
                    && PytestUtils.isTestOrPytestFile(psiElement)) {
                Collection<PyFunction> moduleFunctions = TypeUtils.getModuleFunctions(
                        completionParameters.getPosition().getProject(), "_pytest.hookspec"
                );

                result.addAllElements(
                        moduleFunctions.stream()
                                .map(PytestHooksCompletionProvider::createFunctionDeclaration)
                                .filter(Objects::nonNull)
                                .collect(Collectors.toList())
                );
            }
        }
    }
}
