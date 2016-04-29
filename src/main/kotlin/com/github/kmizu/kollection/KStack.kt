package com.github.kmizu.kollection
import com.github.kmizu.kollection.KList.*

data class KStack<out T>(internal val elements: KList<T>) {
    constructor(): this(KNil)
    fun isEmpty(): Boolean = elements.isEmpty
    fun top(): T = elements.hd
    fun pop(): KStack<T> = KStack(elements.tl)
}