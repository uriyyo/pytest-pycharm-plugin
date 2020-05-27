package com.uriyyo.pytest.extensions

import com.jetbrains.python.psi.PyDecoratorList
import com.jetbrains.python.psi.PyFunction
import com.jetbrains.python.psi.stubs.PyFunctionNameIndex
import com.jetbrains.python.testing.pyTestFixtures.isCustomFixture


fun PyDecoratorList.hasDecorator(vararg names: String) =
        names.any { findDecorator(it) != null }

fun PyFunction.isFixture(): Boolean =
        decoratorList
                ?.hasDecorator("pytest.fixture", "fixture")
                ?: false
                || isCustomFixture()

fun PyFunction.isHook(): Boolean = this.getHook() !== null

fun PyFunction.getHook(): PyFunction? =
        PyFunctionNameIndex.find(this.name, this.project)
                .firstOrNull {
                    it.qualifiedName?.startsWith("_pytest.hookspec") ?: false
                }

val PyFunction.parameterNames
    get(): Collection<String> = this.parameterList.parameters.mapNotNull { it.name }