package com.github.kmizu.kollection

sealed class KEither<L, R> {
    class Left<L, R>(val value: L): KEither<L, R>()
    class Right<L, R>(val value: R): KEither<L, R>()
    fun <U> map(function: (R) -> U): KEither<L, U> = when(this) {
        is Right -> Right(function(this.value))
        is Left -> Left(this.value)
    }
    fun <U> flatMap(function: (R) -> KEither<L, U>): KEither<L, U> = when(this) {
        is Right -> function(this.value)
        is Left -> Left(this.value)
    }
}