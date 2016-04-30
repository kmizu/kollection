package com.github.kmizu.kollection

interface KImmutableLinearSequence<out T> {
    val hd: T
    val tl: KImmutableLinearSequence<T>
    val isEmpty: Boolean
}
