package com.github.kmizu.kollection

interface KLinearSequence<out T> : Iterable<T> {
    val hd: T
    val tl: KLinearSequence<T>
    val isEmpty: Boolean
    override fun iterator(): Iterator<T> = object : Iterator<T> {
        private var elements: KLinearSequence<T> = this@KLinearSequence
        override fun hasNext(): Boolean = !elements.isEmpty
        override fun next(): T = run {
            val value = elements.hd
            elements = elements.tl
            value
        }
    }
}
