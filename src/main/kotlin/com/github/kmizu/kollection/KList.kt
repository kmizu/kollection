package com.github.kmizu.kollection
import com.github.kmizu.kollection.kontrol.block

sealed class KList<out T>() : Iterable<T> {
    companion object {
        fun <T> make(vararg elements: T): KList<T> = block{
            var result: KList<T> = KNil
            for(e in elements.reversed()) {
                result = e.cons(result)
            }
            result
        }
    }
    class KCons<out T>(val head: T, val tail: KList<T>) : KList<T>() {
        override fun equals(other: Any?): Boolean = when (other) {
            is KCons<*> -> this.head == other.head && this.tail == other.tail
            else -> false
        }
        override fun hashCode(): Int = tail.hashCode() + (head?.hashCode() ?: 0)
        override fun toString(): String = head.toString() + " :: " + tail
    }
    object KNil : KList<Nothing>() {
        override fun equals(other: Any?): Boolean = when (other) {
            is KNil -> true
            else -> false
        }
        override fun toString(): String = "KNil"
    }
    fun hd(): T = when(this) {
        is KCons<T> -> this.head
        is KNil -> throw IllegalArgumentException("KNil")
    }
    fun tl(): KList<T> = when(this) {
        is KCons<T> -> this.tail
        is KNil -> throw IllegalArgumentException("KNil")
    }
    fun reverse(): KList<T> = block {
        tailrec fun loop(accumlator: KList<T>, rest: KList<T>): KList<T> = when(rest) {
            is KNil -> accumlator
            is KCons<T> -> loop(rest.head.cons(accumlator), rest.tail)
        }
        loop(KNil, this)
    }
    fun <U> foldLeft(z: U, function: (U, T) -> U): U  = block {
        tailrec fun loop(list: KList<T>, accumulator: U): U = when(list) {
            is KCons<T> -> loop(list.tail, function(accumulator, list.head))
            is KNil -> accumulator
        }
        loop(this, z)
    }
    fun <U> foldRight(z: U, function: (T, U) -> U): U = block {
        tailrec fun loop(list: KList<T>, accumulator: U): U = when(list) {
            is KCons<T> -> loop(list.tail, function(list.head, accumulator))
            is KNil -> accumulator
        }
        loop(this.reverse(), z)
    }
    fun <U> map(function: (T) -> U): KList<U>  = block {
        tailrec fun loop(list: KList<T>, result: KList<U>): KList<U> = when(list) {
            is KCons<T> -> loop(list.tail, function(list.head) cons result)
            is KNil -> result
        }
        loop(this, KNil).reverse()
    }
    fun <U> flatMap(function: (T) -> KList<U>): KList<U> = block {
        var result: KList<U> = KNil
        var rest: KList<T> = this
        while(rest != KNil) {
            var rest2: KList<U> = function(rest.hd())
            rest = rest.tl()
            while(rest2 != KNil) {
                result = rest2.hd() cons result
                rest2 = rest2.tl()
            }
        }
        result.reverse()
    }
    fun isEmpty(): Boolean = when(this) {
        is KCons<T> -> false
        is KNil -> true
    }
    fun forAll(predicate: (T) -> Boolean): Boolean = this.all(predicate)
    fun exists(predicate: (T) -> Boolean): Boolean = this.any(predicate)

    override fun iterator(): Iterator<T> = object : Iterator<T> {
        private var elements: KList<T> = this@KList
        override fun hasNext(): Boolean = !elements.isEmpty()
        override fun next(): T = block {
            val value = elements.hd()
            elements = elements.tl()
            value
        }
    }
}

