package com.pytest_support.utils;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.jetbrains.python.psi.LanguageLevel;
import com.jetbrains.python.psi.PyElementGenerator;
import com.jetbrains.python.psi.PyFunction;
import com.jetbrains.python.psi.types.PyType;
import com.jetbrains.python.psi.types.TypeEvalContext;

import java.text.MessageFormat;

public class GenerateUtils {
    static public final MessageFormat instanceCode = new MessageFormat("from {0} import {1}\ndef _():return {1}()");
    static public final MessageFormat moduleCode = new MessageFormat("import {0}\ndef _():return {0}");

    static public String createInstanceCode(String module, String component) {
        return instanceCode.format(new String[]{module, component});
    }

    static public String createModuleCode(String module) {
        return moduleCode.format(new String[]{module});
    }

    static public <T extends PsiElement, R extends PyType> R generateCode(
            Project project, String code, Class<T> generateType, Class<R> returnType
    ) {
        PyElementGenerator generator = PyElementGenerator.getInstance(project);
        T generatedCode = generator.createFromText(LanguageLevel.PYTHON36, generateType, code);
        PyFunction function = PsiTreeUtil.getChildOfType(generatedCode.getParent(), PyFunction.class);

        return function != null ? (R) function.getReturnStatementType(TypeEvalContext.deepCodeInsight(project)) : null;
    }

}
