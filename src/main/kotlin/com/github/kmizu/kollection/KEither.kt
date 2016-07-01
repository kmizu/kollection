package com.github.kmizu.kollection

sealed class KEither<L, R> {
    class Left<L, R>(val value: L): KEither<L, R>() {
        override fun equals(other: Any?): Boolean = run {
            when(other) {
                is Left<*, *> -> value == other.value
                else -> false
            }
        }

        override fun hashCode(): Int = run {
            value?.hashCode() ?: 0
        }

        override fun toString(): String = run {
            "Left(${value})"
        }
    }
    class Right<L, R>(val value: R): KEither<L, R>() {
        override fun equals(other: Any?): Boolean = run {
            when(other) {
                is Right<*, *> -> value == other.value
                else -> false
            }
        }

        override fun hashCode(): Int = run {
            value?.hashCode() ?: 0
        }

        override fun toString(): String = run {
            "Right(${value})"
        }
    }
    fun <U> map(function: (R) -> U): KEither<L, U> = when(this) {
        is Right -> Right(function(this.value))
        is Left -> Left(this.value)
    }
    fun <U> flatMap(function: (R) -> KEither<L, U>): KEither<L, U> = when(this) {
        is Right -> function(this.value)
        is Left -> Left(this.value)
    }
    fun <U> fold(fl: (L) -> U, fr: (R) -> U): U = when(this) {
        is Right -> fr(this.value)
        is Left -> fl(this.value)
    }
}