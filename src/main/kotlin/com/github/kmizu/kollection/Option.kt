package com.github.kmizu.kollection

sealed class Option<out T:Any>() {
    class Some<T:Any>(val value: T) : Option<T>() {
        override fun equals(other: Any?): Boolean = when(other){
            is Some<*> -> value == other.value
            else -> false
        }
        override fun hashCode(): Int = value.hashCode()
    }
    object None : Option<Nothing>() {
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

    fun <U:Any> map(function: (T) -> U): Option<U> = when(this) {
        is Some<T> -> Some(function(this.value))
        is None -> None
    }

    fun <U:Any> flatMap(function: (T) -> Option<U>): Option<U> = when(this) {
        is Some<T> -> function(this.value)
        is None -> None
    }

    fun filter(function: (T) -> Boolean): Option<T> = when(this) {
        is Some<T> -> if(function(this.value)) this else None
        is None -> None
    }

    override fun toString(): String = when(this){
        is Some<*> -> "Some(${value})"
        is None -> "None"
    }
}