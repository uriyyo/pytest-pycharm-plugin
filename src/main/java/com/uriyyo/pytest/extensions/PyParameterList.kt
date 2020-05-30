package com.uriyyo.pytest.extensions

import com.jetbrains.python.psi.PyParameterList

operator fun PyParameterList.contains(parameter: String): Boolean =
        this.parameters.any { it.name == parameter }
