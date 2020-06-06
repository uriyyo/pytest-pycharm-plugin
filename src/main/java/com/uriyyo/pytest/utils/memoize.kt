package com.uriyyo.pytest.utils

import java.util.*

internal fun currentTime(): Long = Calendar.getInstance().time.time

data class Cell<R>(val resetAt: Long, val value: R)

fun <T, R> ((T) -> R).memoize(timeout: Int = 10_000): (T) -> R {
    val cache: MutableMap<T, Cell<R>> = mutableMapOf()

    fun memoized(it: T): R {
        cache[it]
                ?.takeIf { currentTime() < it.resetAt }
                ?.let { return@memoized it.value }

        val value = this(it)
        cache[it] = Cell(currentTime() + timeout, value)

        return value
    }

    return ::memoized
}