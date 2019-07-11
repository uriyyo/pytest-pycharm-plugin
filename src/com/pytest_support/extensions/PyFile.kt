package com.pytest_support.extensions

import com.jetbrains.python.psi.PyFile
import com.pytest_support.utils.StreamUtils
import kotlin.streams.asSequence

fun PyFile.fixtures() = StreamUtils
        .pyFixturesStream(this)
        .asSequence()
        .fixtures()
