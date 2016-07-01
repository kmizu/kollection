package com.github.kmizu.kollection

interface KQueue<T> {
    val isEmpty: Boolean

    infix fun enqueue(newElement: T): KQueue<T>

    fun dequeue(): KQueue<T>

    fun peek(): T

    fun toList(): KList<T>
}
