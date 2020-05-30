package com.uriyyo.pytest.extensions

import com.jetbrains.python.psi.PyDecoratorList
import com.jetbrains.python.psi.PyFunction
import com.jetbrains.python.psi.stubs.PyFunctionNameIndex
import com.jetbrains.python.testing.pyTestFixtures.isCustomFixture

fun PyDecoratorList.hasDecorator(vararg names: String) =
        names.any { findDecorator(it) != null }

val PyFunction.isTest
    get(): Boolean =
        name?.let { it.startsWith("test") || it.endsWith("test") } ?: false

val PyFunction.isFixture
    get(): Boolean =
        decoratorList
                ?.hasDecorator("pytest.fixture", "fixture", "pytest.yield_fixture", "yield_fixture")
                ?: false ||
                isCustomFixture()

val PyFunction.isHook
    get(): Boolean = getHook() !== null

fun PyFunction.getHook(): PyFunction? =
        PyFunctionNameIndex.find(name, project)
                .firstOrNull {
                    it.qualifiedName?.startsWith("_pytest.hookspec") ?: false
                }

val PyFunction.parameterNames
    get(): Collection<String> = parameterList.parameters.mapNotNull { it.name }
