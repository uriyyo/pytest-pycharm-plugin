package com.uriyyo.pytest

val INTERNAL_TYPES = mapOf(
        "request" to "SubRequest",

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
