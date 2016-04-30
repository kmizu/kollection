package com.github.kmizu.kollection
import com.github.kmizu.kollection.KStream.*

infix fun <T> T.cons(other: () -> KStream<T>): KStream<T> = KStreamCons(this, other)
infix fun <T> KStream<T>.concat(other: KStream<T>): KStream<T> = run {
    if(isEmpty) other else this.hd cons { this.tl concat other }
}
