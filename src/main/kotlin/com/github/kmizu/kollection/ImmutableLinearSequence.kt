package com.github.kmizu.kollection

interface ImmutableLinearSequence<out T> {
    val hd: T
    val tl: ImmutableLinearSequence<T>
    val isEmpty: Boolean
}
