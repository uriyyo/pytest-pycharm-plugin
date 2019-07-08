package com.pytest_support.utils;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.jetbrains.python.psi.PyDecoratorList;
import com.jetbrains.python.psi.PyFile;
import com.jetbrains.python.psi.PyFunction;
import com.jetbrains.python.psi.types.PyType;
import com.pytest_support.consts.PytestConsts;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Stream;

import static com.pytest_support.consts.PytestConsts.functionParameter;

public class FixtureUtils {

    public static boolean isFixtureReference(PsiElement psiElement) {
        return functionParameter.accepts(psiElement);
    }


    public static boolean isFixture(PyFunction function) {
        if (function == null) {
            return false;
        }
        PyDecoratorList decoratorList = function.getDecoratorList();

        return decoratorList != null && PytestConsts.DECARATORS.stream()
                .anyMatch(name -> decoratorList.findDecorator(name) != null);
    }

    public static boolean isFixture(PyFunction function, String parameterName) {
        return isFixture(function) && functionContainsParameter(function, parameterName);
    }

    public static boolean isTestFunction(PyFunction testFunction) {
        if (testFunction == null) {
            return false;
        }

        String name = testFunction.getName();
        return name != null && name.startsWith("test_");
    }


    public static boolean isTestFunction(PyFunction function, String parameterName) {
        return isTestFunction(function) && functionContainsParameter(function, parameterName);
    }

    public static boolean isTestFunctionOrFixture(PyFunction function, String parameterName) {
        return isFixture(function, parameterName) || isTestFunction(function, parameterName);
    }

    public static boolean isTestFunctionOrFixture(PyFunction function) {
        return isFixture(function) || isTestFunction(function);
    }

    public static boolean isTestOrFixtureParameter(PsiElement element) {
        return isTestFunctionOrFixture(getFunctionFromElement(element));
    }

    public static PyFunction getFunctionFromElement(PsiElement element) {
        return PsiTreeUtil.getContextOfType(element, PyFunction.class);
    }

    public static boolean functionContainsParameter(PyFunction function, String parameterName) {
        if (parameterName == null || function == null) {
            return false;
        }

        return function.getParameterList().findParameterByName(parameterName) != null;
    }

    public static boolean isHookFunction(Project project, String functionName) {
        return TypeUtils.getModuleFunctions(project, "_pytest.hookspec")
                .stream()
                .anyMatch(function -> Objects.equals(function.getName(), functionName));
    }

    public static PyType getFixtureRequestType(Project project) {
        return TypeUtils.getElementType(project, "FixtureRequest");
    }

    public static Stream<PyFunction> pyFileTopLevelFunctionsAndClassMethods(PyFile pyFile) {
        return Stream.concat(
                pyFile.getTopLevelFunctions().stream(),
                pyFile.getTopLevelClasses().stream().flatMap(pyClass -> Arrays.stream(pyClass.getMethods()))
        );
    }

    public static Stream<PyFunction> pyFileTopLevelFunctionsAndClassMethods(Project project) {
        return VirtualFilesUtils.allPyFileStream(project)
                .flatMap(FixtureUtils::pyFileTopLevelFunctionsAndClassMethods);
    }

}
