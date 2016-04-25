package com.github.kmizu.kollection

sealed class KOption<out T>(): Iterable<T> {
    class Some<T>(val value: T) : KOption<T>() {
        override fun equals(other: Any?): Boolean = when(other){
            is Some<*> -> value == other.value
            else -> false
        }
        override fun hashCode(): Int = value?.hashCode() ?: 0
    }
    object None : KOption<Nothing>() {
        override fun equals(other: Any?): Boolean = super.equals(other)
    }

    /**
     * Extract the value from this Option.
     * Note that this method should not be used as soon as possible since it is dangerous.
     */
    fun get(): T = when(this) {
        is Some<T> -> this.value
        is None -> throw IllegalArgumentException("None")
    }

    fun <U:Any> foldLeft(z: U, function: (U, T) -> U): U = when(this) {
        is Some<T> -> function(z, this.value)
        is None -> z
    }

    fun <U:Any> foldRight(z: U, function: (T, U) -> U): U = when(this) {
        is Some<T> -> function(this.value, z)
        is None -> z
    }

    fun <U:Any> map(function: (T) -> U): KOption<U> = when(this) {
        is Some<T> -> Some(function(this.value))
        is None -> None
    }

    fun <U:Any> flatMap(function: (T) -> KOption<U>): KOption<U> = when(this) {
        is Some<T> -> function(this.value)
        is None -> None
    }

    fun filter(function: (T) -> Boolean): KOption<T> = when(this) {
        is Some<T> -> if(function(this.value)) this else None
        is None -> None
    }

    fun isEmpty(): Boolean = when(this) {
        is Some<T> -> false
        is None -> true
    }

    override fun toString(): String = when(this){
        is Some<*> -> "Some(${value})"
        is None -> "None"
    }

    override fun iterator(): Iterator<T> = object : Iterator<T> {
        private var value: KOption<T> = this@KOption
        override fun next(): T = when(value) {
            is Some<T> ->
                run {
                    val result = value.get()
                    value = None
                    result
                }
            is None -> throw IllegalArgumentException("None")
        }

        override fun hasNext(): Boolean = when(value){
            is Some<T> -> true
            is None -> false
        }
    }
}