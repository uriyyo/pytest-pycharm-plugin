package com.pytest_support.consts;

import com.intellij.patterns.ElementPattern;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.psi.PsiElement;
import com.jetbrains.python.PyTokenTypes;
import com.jetbrains.python.PythonLanguage;
import com.jetbrains.python.psi.PyFunction;
import com.jetbrains.python.psi.PyParameterList;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PytestConsts {
    public static final String CONFTEST = "conftest.py";
    public static final String PLUGIN = "plugin.py";
    public static final String PYTEST_PLUGIN = "pytest_plugin.py";

    public static final String REQUEST_FIXTURE_NAME = "request";

    public static final List<String> PYTEST_FILES = Arrays.asList(CONFTEST, PLUGIN, PYTEST_PLUGIN);

    public static final List<String> DECARATORS = Arrays.asList(
            "fixture", "pytest.fixture", "pytest.yield_fixture", "yield_fixture"
    );

    public static final ElementPattern<PsiElement> functionParameter = PlatformPatterns
            .psiElement()
            .withLanguage(PythonLanguage.getInstance())
            .and(PlatformPatterns.psiElement().inside(PyParameterList.class));

    public static final ElementPattern<PsiElement> functionDefinition = PlatformPatterns
            .psiElement()
            .withLanguage(PythonLanguage.getInstance())
            .afterLeaf(PlatformPatterns.psiElement(PyTokenTypes.DEF_KEYWORD));

    public static final ElementPattern<PsiElement> inFunctionElement = PlatformPatterns
            .psiElement()
            .withLanguage(PythonLanguage.getInstance())
            .inside(PyFunction.class);

    public static final Map<String, String> hookTypes = new HashMap<>();

    static {
        hookTypes.put("pluginmanager", "PytestPluginManager");
        hookTypes.put("plugin", "PytestPluginManager");
        hookTypes.put("manager", "PytestPluginManager");

        hookTypes.put("config", "Config");
        hookTypes.put("early_config", "Config");

        hookTypes.put("session", "Session");
        hookTypes.put("parent", "Session");

        hookTypes.put("pyfuncitem", "Function");
        hookTypes.put("nextitem", "Function");
        hookTypes.put("item", "Function");

        hookTypes.put("startdir", "LocalPath");
        hookTypes.put("path", "LocalPath");
        hookTypes.put("fslocation", "LocalPath");

        hookTypes.put("terminalreporter", "TerminalReporter");
        hookTypes.put("collector", "FSCollector");
        hookTypes.put("excinfo", "ExceptionInfo");
        hookTypes.put("report", "BaseReport");
        hookTypes.put("metafunc", "Metafunc");
        hookTypes.put("location", "tuple");
        hookTypes.put("exitstatus", "int");
        hookTypes.put("call", "CallInfo");
        hookTypes.put("parser", "Parser");
        hookTypes.put("excrepr", "str");
        hookTypes.put("nodeid", "str");
        hookTypes.put("node", "Node");
        hookTypes.put("args", "list");
        hookTypes.put("code", "Code");
        hookTypes.put("name", "str");
    }
}
