package com.github.kmizu.kollection
import com.github.kmizu.kollection.KList.KCons
import com.github.kmizu.kollection.KList.KNil

sealed class KList<out T:Any> {
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
}

infix fun <T:Any> T.prepend(other: KList<T>): KList<T> = KCons(this, other)

fun <T:Any> klistOf(vararg elements: T): KList<T> {
    var result: KList<T> = KNil
    for(e in elements.reversed()) {
        result = e.prepend(result)
    }
    return result
}