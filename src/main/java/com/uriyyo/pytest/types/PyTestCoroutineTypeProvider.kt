package com.uriyyo.pytest.types

import com.intellij.openapi.util.Ref
import com.jetbrains.python.psi.PyFunction
import com.jetbrains.python.psi.PyNamedParameter
import com.jetbrains.python.psi.types.PyCollectionType
import com.jetbrains.python.psi.types.PyType
import com.jetbrains.python.psi.types.PyTypeProviderBase
import com.jetbrains.python.psi.types.TypeEvalContext
import com.jetbrains.python.testing.pyTestFixtures.PyTextFixtureTypeProvider


class PyTestCoroutineTypeProvider : PyTypeProviderBase() {
    override fun getParameterType(param: PyNamedParameter, func: PyFunction, context: TypeEvalContext): Ref<PyType>? {
        return PyTextFixtureTypeProvider.getParameterType(param, func, context)
                ?.get()
                ?.let {
                    if (it is PyCollectionType && it.name == "Coroutine")
                        Ref(it.elementTypes.last())
                    else
                        null
                }
    }
}
