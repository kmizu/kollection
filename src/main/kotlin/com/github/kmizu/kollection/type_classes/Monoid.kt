package com.github.kmizu.kollection.type_classes

interface Monoid<T> {
    fun mzero(): T
    fun mplus(a: T, b: T): T
}