package com.github.kmizu.kollection

interface KImmutableSet<T> {
    operator fun get(element: T): Boolean
    infix operator fun plus(element: T): KImmutableSet<T>
    val isEmpty: Boolean
}