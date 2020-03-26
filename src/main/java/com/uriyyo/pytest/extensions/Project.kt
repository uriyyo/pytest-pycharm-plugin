package com.uriyyo.pytest.extensions

import com.intellij.openapi.project.Project
import com.jetbrains.python.psi.PyClass
import com.jetbrains.python.psi.stubs.PyClassNameIndex

fun Project.findClass(name: String, includeNonProjectItems: Boolean = true): Collection<PyClass> =
        PyClassNameIndex.find(name, this, includeNonProjectItems)