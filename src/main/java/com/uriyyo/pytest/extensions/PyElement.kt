package com.uriyyo.pytest.extensions

import com.jetbrains.python.psi.PyElement
import com.jetbrains.python.psi.types.PyType
import com.jetbrains.python.psi.types.TypeEvalContext
import com.uriyyo.pytest.INTERNAL_TYPES


val INTERNAL_MODULES = arrayOf("_pytest", "py")

private fun isInternalType(qname: String?): Boolean =
        qname?.let { name -> INTERNAL_MODULES.any { name.startsWith(it) } } ?: false

fun PyElement.getPyTestType(context: TypeEvalContext): PyType? =
        INTERNAL_TYPES[name]
                ?.let { project.findClass(it) }
                ?.mapNotNull { it.getType(context) }
                ?.firstOrNull { isInternalType(it.classQName) }
                ?.getReturnType(context)