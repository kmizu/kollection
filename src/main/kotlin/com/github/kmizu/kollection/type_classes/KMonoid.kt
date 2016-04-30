package com.github.kmizu.kollection.type_classes

import com.github.kmizu.kollection.KList
import com.github.kmizu.kollection.KList.*
import com.github.kmizu.kollection.concat

interface KMonoid<T> {
    companion object {
        val BYTE: KMonoid<Byte> = object : KMonoid<Byte> {
            override fun mzero(): Byte = 0
            override fun mplus(a: Byte, b: Byte): Byte = (a + b).toByte()
        }
        val SHORT: KMonoid<Short> = object : KMonoid<Short> {
            override fun mzero(): Short = 0
            override fun mplus(a: Short, b: Short): Short = (a + b).toShort()
        }
        val INT: KMonoid<Int> = object : KMonoid<Int> {
            override fun mzero(): Int = 0
            override fun mplus(a: Int, b: Int): Int = a + b
        }
        val CHAR: KMonoid<Char> = object : KMonoid<Char> {
            override fun mzero(): Char = 0.toChar()
            override fun mplus(a: Char, b: Char): Char = (a + b.toInt()).toChar()
        }
        val LONG: KMonoid<Long> = object : KMonoid<Long> {
            override fun mzero(): Long = 0
            override fun mplus(a: Long, b: Long): Long = a + b
        }
        val FLOAT: KMonoid<Float> = object : KMonoid<Float> {
            override fun mzero(): Float = 0.0f
            override fun mplus(a: Float, b: Float): Float = a + b
        }
        val DOUBLE: KMonoid<Double> = object : KMonoid<Double> {
            override fun mzero(): Double = 0.0
            override fun mplus(a: Double, b: Double): Double = a + b
        }
        val BOOLEAN: KMonoid<Boolean> = object : KMonoid<Boolean> {
            override fun mzero(): Boolean = false
            override fun mplus(a: Boolean, b: Boolean): Boolean = a || b
        }
        val UNIT: KMonoid<Unit> = object : KMonoid<Unit> {
            override fun mzero(): Unit = Unit
            override fun mplus(a: Unit, b: Unit): Unit = Unit
        }

        fun <A, B> PAIR(a: KMonoid<A>, b: KMonoid<B>): KMonoid<Pair<A, B>> = object : KMonoid<Pair<A, B>> {
            override fun mzero(): Pair<A, B> = Pair(a.mzero(), b.mzero())
            override fun mplus(x: Pair<A, B>, y: Pair<A, B>): Pair<A, B> = Pair(a.mplus(x.first, y.first), b.mplus(x.second, y.second))
        }

        fun <A> KLIST(): KMonoid<KList<A>> = object : KMonoid<KList<A>> {
            override fun mzero(): KList<A> = Nil
            override fun mplus(a: KList<A>, b: KList<A>): KList<A> = a concat b
        }
    }
    fun mzero(): T
    fun mplus(a: T, b: T): T
}