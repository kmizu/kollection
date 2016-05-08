package com.github.kmizu.kollection

interface KImmutableSet<T>: Iterable<T> {
    operator fun get(element: T): Boolean
    infix operator fun plus(element: T): KImmutableSet<T>
    fun remove(element: T): KImmutableSet<T>
    val isEmpty: Boolean
}