package com.uriyyo.pytest.types

import com.jetbrains.python.psi.PyElement
import com.jetbrains.python.psi.PyFunction
import com.jetbrains.python.psi.types.PyType
import com.jetbrains.python.psi.types.TypeEvalContext
import com.uriyyo.pytest.base.PyTestTypeProvider
import com.uriyyo.pytest.extensions.getContextOrSelf
import com.uriyyo.pytest.extensions.getPyTestType
import com.uriyyo.pytest.extensions.isFixture
import com.uriyyo.pytest.extensions.isHook

class PyTestInternalTypeProvider : PyTestTypeProvider() {

    override fun getType(element: PyElement, context: TypeEvalContext): PyType? =
            element.getContextOrSelf<PyFunction>()
                    ?.let {
                        if (it.isFixture() || it.isHook())
                            element.getPyTestType(context)
                        else
                            null
                    }

}