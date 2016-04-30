package com.github.kmizu.kollection.type_classes

interface Monoid<T> {
    companion object {
        val BYTE: Monoid<Byte> = object: Monoid<Byte> {
            override fun mzero(): Byte = 0
            override fun mplus(a: Byte, b: Byte): Byte = (a + b).toByte()
        }
        val SHORT: Monoid<Short> = object: Monoid<Short> {
            override fun mzero(): Short = 0
            override fun mplus(a: Short, b: Short): Short = (a + b).toShort()
        }
        val INT: Monoid<Int> = object: Monoid<Int> {
            override fun mzero(): Int = 0
            override fun mplus(a: Int, b: Int): Int = a + b
        }
        val CHAR: Monoid<Char> = object: Monoid<Char> {
            override fun mzero(): Char = 0.toChar()
            override fun mplus(a: Char, b: Char): Char = (a + b.toInt()).toChar()
        }
        val LONG: Monoid<Long> = object: Monoid<Long> {
            override fun mzero(): Long = 0
            override fun mplus(a: Long , b: Long): Long = a + b
        }
        val FLOAT: Monoid<Float> = object: Monoid<Float> {
            override fun mzero(): Float = 0.0f
            override fun mplus(a: Float, b: Float): Float = a + b
        }
        val DOUBLE: Monoid<Double> = object: Monoid<Double> {
            override fun mzero(): Double = 0.0
            override fun mplus(a: Double, b: Double): Double = a + b
        }
        val BOOLEAN: Monoid<Boolean> = object: Monoid<Boolean> {
            override fun mzero(): Boolean = false
            override fun mplus(a: Boolean, b: Boolean): Boolean = a || b
        }
        val UNIT: Monoid<Unit> = object: Monoid<Unit> {
            override fun mzero(): Unit = Unit
            override fun mplus(a: Unit, b: Unit): Unit = Unit
        }
    }
    fun mzero(): T
    fun mplus(a: T, b: T): T
}