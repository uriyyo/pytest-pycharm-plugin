package com.uriyyo.pytest.hooks

import com.jetbrains.python.psi.PyFunction
import com.uriyyo.pytest.base.PyTestArgumentCompletion
import com.uriyyo.pytest.base.PyTestParameterCompletionContributor
import com.uriyyo.pytest.extensions.getHook
import com.uriyyo.pytest.extensions.parameterNames

class PyTestHooksParameterCompletionContributor : PyTestParameterCompletionContributor() {

    override fun getCompletion() = object : PyTestArgumentCompletion() {
        override fun getCompletions(pyFunction: PyFunction): Sequence<String>? =
                pyFunction
                        .getHook()
                        ?.parameterNames
                        ?.asSequence()
    }
}
