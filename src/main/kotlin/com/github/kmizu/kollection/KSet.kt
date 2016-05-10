package com.github.kmizu.kollection

interface KSet<T>: Iterable<T> {
    operator fun get(element: T): Boolean
    infix operator fun plus(element: T): KSet<T>
    fun remove(element: T): KSet<T>
    val isEmpty: Boolean
}