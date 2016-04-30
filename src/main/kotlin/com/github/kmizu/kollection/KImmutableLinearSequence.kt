package com.github.kmizu.kollection

interface KImmutableLinearSequence<out T> : Iterable<T> {
    val hd: T
    val tl: KImmutableLinearSequence<T>
    val isEmpty: Boolean
    override fun iterator(): Iterator<T> = object : Iterator<T> {
        private var elements: KImmutableLinearSequence<T> = this@KImmutableLinearSequence
        override fun hasNext(): Boolean = !elements.isEmpty
        override fun next(): T = run {
            val value = elements.hd
            elements = elements.tl
            value
        }
    }
}
