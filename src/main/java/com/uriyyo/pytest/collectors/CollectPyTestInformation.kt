package com.uriyyo.pytest.collectors

import com.intellij.openapi.project.Project
import com.uriyyo.pytest.extensions.execPythonCommand
import com.uriyyo.pytest.utils.memoize

val BUILTIN_MARKERS = arrayOf(
        "skipif",
        "skip",
        "xfail",
        "parametrize",
        "usefixtures",
        "tryfirst",
        "trylast",
        "hookwrapper"
)

internal fun execAndParseOutput(project: Project, pattern: String, vararg commands: String): Sequence<String> {
    val output = project.execPythonCommand(*commands)?.stdout ?: return emptySequence()

    return Regex(pattern)
            .findAll(output)
            .map { it.value }
            .toSet()
            .asSequence()
}

val collectAddOptions = { project: Project ->
    execAndParseOutput(project, "(?<= )-[-\\w]+", "-m", "pytest", "--help")
            .toList()
}.memoize()

val collectMarkers = { project: Project ->
    execAndParseOutput(project, "(?<=pytest\\.mark\\.)(\\w+)", "-m", "pytest", "--markers")
            .filter { it !in BUILTIN_MARKERS }
            .toList()
}.memoize()