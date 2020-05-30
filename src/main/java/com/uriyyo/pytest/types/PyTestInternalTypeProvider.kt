package com.uriyyo.pytest.types

import com.jetbrains.python.psi.PyElement
import com.jetbrains.python.psi.PyFunction
import com.jetbrains.python.psi.types.PyType
import com.jetbrains.python.psi.types.TypeEvalContext
import com.uriyyo.pytest.base.PyTestTypeProvider
import com.uriyyo.pytest.extensions.*

class PyTestInternalTypeProvider : PyTestTypeProvider() {

    override fun getType(element: PyElement, context: TypeEvalContext): PyType? =
            element.getContextOrSelf<PyFunction>()
                    ?.takeIf { it.isFixture || it.isHook || it.isTest }
                    ?.let { element.getPyTestType(context) }
}
