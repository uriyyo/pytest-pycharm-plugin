package com.pytest_support.extensions

import com.intellij.openapi.project.Project
import com.pytest_support.utils.FixtureUtils
import kotlin.streams.asSequence

fun Project.allFixtureAndTests() = FixtureUtils
        .pyFileTopLevelFunctionsAndClassMethods(this)
        .asSequence()
        .fixturesOrTests()