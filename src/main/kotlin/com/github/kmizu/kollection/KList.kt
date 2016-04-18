package com.github.kmizu.kollection

sealed class KList<out T:Any>() {
    class KCons<out T:Any>(val head: T, val tail: KList<T>) : KList<T>() {
        override fun equals(other: Any?): Boolean = when (other) {
            is KCons<*> -> this.head == other.head && this.tail == other.tail
            else -> false
        }
        override fun hashCode(): Int = head.hashCode() + tail.hashCode()
        override fun toString(): String = head.toString() + " :: " + tail
    }
    object KNil : KList<Nothing>() {
        override fun equals(other: Any?): Boolean = when (other) {
            is KNil -> true
            else -> false
        }
        override fun toString(): String = "KNil"
    }
    fun reverse(): KList<T> {
        tailrec fun loop(accumlator: KList<T>, rest: KList<T>): KList<T> = when(rest) {
            is KNil -> accumlator
            is KCons<T> -> loop(rest.head.prepend(accumlator), rest.tail)
        }
        return loop(KNil, this)
    }
    fun <U:Any> foldLeft(z: U, function: (U, T) -> U): U {
        tailrec fun loop(list: KList<T>, accumulator: U): U = when(list) {
            is KCons<T> -> loop(list.tail, function(accumulator, list.head))
            is KNil -> accumulator
        }
        return loop(this, z)
    }
    fun <U:Any> map(function: (T) -> U): KList<U> {
        tailrec fun loop(list: KList<T>, result: KList<U>): KList<U> = when(list) {
            is KCons<T> -> loop(list.tail, function(list.head) prepend result)
            is KNil -> result
        }
        return loop(this, KNil).reverse()
    }
}


