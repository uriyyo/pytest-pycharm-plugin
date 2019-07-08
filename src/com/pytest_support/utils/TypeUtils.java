package com.pytest_support.utils;

import com.google.common.collect.Lists;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiReference;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.util.QualifiedName;
import com.jetbrains.python.codeInsight.imports.AutoImportQuickFix;
import com.jetbrains.python.codeInsight.imports.ImportCandidateHolder;
import com.jetbrains.python.codeInsight.imports.PythonImportUtils;
import com.jetbrains.python.psi.*;
import com.jetbrains.python.psi.types.PyModuleType;
import com.jetbrains.python.psi.types.PyType;
import com.jetbrains.python.psi.types.TypeEvalContext;

import java.util.*;

public class TypeUtils {
    public static PyType getInstanceType(Project project, String module, String component) {
        return GenerateUtils.generateCode(
                project, GenerateUtils.createInstanceCode(module, component), PyFromImportStatement.class, PyType.class
        );
    }

    public static PyModuleType getModuleType(Project project, String module) {
        return GenerateUtils.generateCode(
                project, GenerateUtils.createModuleCode(module), PyImportStatement.class, PyModuleType.class
        );
    }

    public static Collection<PyFunction> getModuleFunctions(Project project, String module) {
        return Optional.ofNullable(getModuleType(project, module))
                .map(PyModuleType::getModule)
                .map(PyFile::getTopLevelFunctions)
                .orElse(Collections.emptyList());
    }

    public static PyType getElementType(PyElement element, String name, String module) {
        PsiReference reference = element.getReference();
        if (reference == null) {
            return null;
        }

        AutoImportQuickFix autoImportQuickFix = PythonImportUtils.proposeImportFix(element, reference);

        if (autoImportQuickFix == null){
            return null;
        }

        return autoImportQuickFix.getCandidates()
                .stream()
                .map(ImportCandidateHolder::getPath)
                .filter(Objects::nonNull)
                .map(QualifiedName::toString)
                .filter(path -> path.contains(module) || path.contains("builtin"))
                .map(path -> getInstanceType(element.getProject(), path, name))
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
    }


    public static PyType getElementType(Project project, String name) {
        PyElementGenerator generator = PyElementGenerator.getInstance(project);
        PyExpressionStatement element = generator.createFromText(
                LanguageLevel.PYTHON36, PyExpressionStatement.class, name
        );

        return getElementType(element.getExpression(), name, "_pytest");
    }


    public static PyType getFunctionReturnType(PyFunction function) {
        TypeEvalContext evalContext = TypeEvalContext.deepCodeInsight(function.getProject());

        if (function.isGenerator()) {
            return PsiTreeUtil.collectElementsOfType(function, PyYieldExpression.class)
                    .stream()
                    .map(evalContext::getType)
                    .filter(Objects::nonNull)
                    .findFirst()
                    .orElse(null);
        }

        ArrayList<PyReturnStatement> pyReturnStatements = new ArrayList<>(
                PsiTreeUtil.collectElementsOfType(function, PyReturnStatement.class)
        );

        return Lists.reverse(pyReturnStatements)
                .stream()
                .map(PyReturnStatement::getExpression)
                .filter(Objects::nonNull)
                .map(evalContext::getType)
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
    }
}

