package com.github.kmizu.kollection
import com.github.kmizu.kollection.KList.*

data class KStack<out T>(internal val elements: KList<T>) {
    constructor(): this(Nil)
    val isEmpty: Boolean
        get() = elements.isEmpty
    val top: T
        get() = elements.hd
    fun pop(): KStack<T> = KStack(elements.tl)
}