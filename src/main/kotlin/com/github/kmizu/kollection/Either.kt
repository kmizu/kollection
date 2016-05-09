package com.github.kmizu.kollection

sealed class Either<L, R> {
    class Left<L, R>(val value: L): Either<L, R>()
    class Right<L, R>(val value: R): Either<L, R>()
    fun <U> map(function: (R) -> U): Either<L, U> = when(this) {
        is Right -> Right(function(this.value))
        is Left -> Left(this.value)
    }
    fun <U> flatMap(function: (R) -> Either<L, U>): Either<L, U> = when(this) {
        is Right -> function(this.value)
        is Left -> Left(this.value)
    }
}