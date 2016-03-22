package com.github.kmizu.kollection

sealed class Option<T:Any>() {
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

    override fun toString(): String = when(this){
        is Some<*> -> "Some(${value})"
        is None -> "None"
    }
}