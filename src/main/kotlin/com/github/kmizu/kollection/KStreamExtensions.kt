package com.github.kmizu.kollection
import com.github.kmizu.kollection.KStream.*

fun <T> KStream(vararg elements: T): KStream<T> = run {
    elements.reversed().fold(KStreamNil as KStream<T>){a, e -> e cons {a} }
}
infix fun <T> T.cons(other: () -> KStream<T>): KStream<T> = KStreamCons(this, other)
infix fun <T> KStream<T>.concat(other: KStream<T>): KStream<T> = run {
    if(isEmpty) other else this.hd cons { this.tl concat other }
}
