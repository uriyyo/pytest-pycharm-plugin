package com.pytest_support.consts

import com.intellij.patterns.ElementPattern
import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.PsiElement
import com.jetbrains.python.PyTokenTypes
import com.jetbrains.python.PythonLanguage
import com.jetbrains.python.psi.PyFunction
import com.jetbrains.python.psi.PyParameterList

const val CONFTEST = "conftest.py"
const val PLUGIN = "plugin.py"
const val PYTEST_PLUGIN = "pytest_plugin.py"

const val REQUEST_FIXTURE_NAME = "request"

val PYTEST_FILES = listOf(CONFTEST, PLUGIN, PYTEST_PLUGIN)

val DECARATORS = listOf(
        "fixture", "pytest.fixture", "pytest.yield_fixture", "yield_fixture"
)

val functionParameter: ElementPattern<PsiElement> = PlatformPatterns
        .psiElement()
        .withLanguage(PythonLanguage.getInstance())
        .and(PlatformPatterns.psiElement().inside(PyParameterList::class.java))

val functionDefinition: ElementPattern<PsiElement> = PlatformPatterns
        .psiElement()
        .withLanguage(PythonLanguage.getInstance())
        .afterLeaf(PlatformPatterns.psiElement(PyTokenTypes.DEF_KEYWORD))

val inFunctionElement: ElementPattern<PsiElement> = PlatformPatterns
        .psiElement()
        .withLanguage(PythonLanguage.getInstance())
        .inside(PyFunction::class.java)

val hookTypes: Map<String, String> = mapOf(
        "pluginmanager" to "PytestPluginManager",
        "plugin" to "PytestPluginManager",
        "manager" to "PytestPluginManager",

        "config" to "Config",
        "early_config" to "Config",

        "session" to "Session",
        "parent" to "Session",

        "pyfuncitem" to "Function",
        "nextitem" to "Function",
        "item" to "Function",

        "startdir" to "LocalPath",
        "path" to "LocalPath",
        "fslocation" to "LocalPath",

        "terminalreporter" to "TerminalReporter",
        "collector" to "FSCollector",
        "excinfo" to "ExceptionInfo",
        "report" to "BaseReport",
        "metafunc" to "Metafunc",
        "location" to "tuple",
        "exitstatus" to "int",
        "call" to "CallInfo",
        "parser" to "Parser",
        "excrepr" to "str",
        "nodeid" to "str",
        "node" to "Node",
        "args" to "list",
        "code" to "Code",
        "name" to "str"
)

