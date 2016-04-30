package com.github.kmizu.kollection
import com.github.kmizu.kollection.KStream.*

infix fun <T> T.cons(other: () -> KStream<T>): KStream<T> = KStreamCons(this, other)
