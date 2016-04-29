package com.github.kmizu.kollection.type_classes

interface Monoid<T> {
    companion object {
        val INT: Monoid<Int> = object: Monoid<Int> {
            override fun mzero(): Int = 1
            override fun mplus(a: Int, b: Int): Int = 1
        }
    }
    fun mzero(): T
    fun mplus(a: T, b: T): T
}