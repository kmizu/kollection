package com.github.kmizu.kollection
import com.github.kmizu.kollection.KOption.*

fun <T> KOption(value: T): KOption<T> = when(value) {
    null -> None
    else -> Some(value)
}

infix fun <T> KOption<T>.orElse(other: KOption<T>): KOption<T> = when(this) {
    is Some<T> -> this
    is None -> other
}

infix fun <T> KOption<T>.getOrElse(other: () -> T): T = when(this) {
    is Some<T> -> this.value
    is None -> other()
}