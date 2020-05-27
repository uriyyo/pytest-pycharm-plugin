package com.uriyyo.pytest.fixtures


import com.jetbrains.python.psi.PyFunction
import com.uriyyo.pytest.base.PyTestArgumentCompletion
import com.uriyyo.pytest.base.PyTestParameterCompletionContributor
import com.uriyyo.pytest.extensions.isFixture

class PyTestFixtureRequestCompletionContributor : PyTestParameterCompletionContributor() {

    override fun getCompletion() = object : PyTestArgumentCompletion() {
        override fun getCompletions(pyFunction: PyFunction): Sequence<String>? =
                if (pyFunction.isFixture())
                    sequenceOf("request")
                else
                    emptySequence()
    }

}