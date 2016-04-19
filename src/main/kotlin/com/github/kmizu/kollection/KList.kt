package com.github.kmizu.kollection
import com.github.kmizu.kollection.kontrol.block

sealed class KList<out T:Any>() {
    companion object {
        fun <T:Any> make(vararg elements: T): KList<T> = block{
            var result: KList<T> = KNil
            for(e in elements.reversed()) {
                result = e.prepend(result)
            }
            result
        }
    }
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
    fun reverse(): KList<T> = block {
        tailrec fun loop(accumlator: KList<T>, rest: KList<T>): KList<T> = when(rest) {
            is KNil -> accumlator
            is KCons<T> -> loop(rest.head.prepend(accumlator), rest.tail)
        }
        loop(KNil, this)
    }
    fun <U:Any> foldLeft(z: U, function: (U, T) -> U): U  = block {
        tailrec fun loop(list: KList<T>, accumulator: U): U = when(list) {
            is KCons<T> -> loop(list.tail, function(accumulator, list.head))
            is KNil -> accumulator
        }
        loop(this, z)
    }
    fun <U:Any> foldRight(z: U, function: (T, U) -> U): U = block {
        tailrec fun loop(list: KList<T>, accumulator: U): U = when(list) {
            is KCons<T> -> loop(list.tail, function(list.head, accumulator))
            is KNil -> accumulator
        }
        loop(this.reverse(), z)
    }
    fun <U:Any> map(function: (T) -> U): KList<U>  = block {
        tailrec fun loop(list: KList<T>, result: KList<U>): KList<U> = when(list) {
            is KCons<T> -> loop(list.tail, function(list.head) prepend result)
            is KNil -> result
        }
        loop(this, KNil).reverse()
    }
}


