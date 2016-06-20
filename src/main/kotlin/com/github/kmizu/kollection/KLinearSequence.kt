package com.github.kmizu.kollection


/**
 * This interface provides a linear sequence.
 * A linear sequence guarantees constant time access of
 * `head` nad `tail`.
 */
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
