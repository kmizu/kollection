package com.github.kmizu.kollection
import com.github.kmizu.kollection.KOption.*

class KLazy<T>(private val thunk: () -> T) {
    private var value: KOption<T> = None
    fun force(): T = synchronized(this){
        when(value) {
            is Some<T> -> value.get()
            is None -> {
                value = Some(thunk())
                value.get()
            }
        }
    }

    val isForced: Boolean
        get() = value != None

    override fun equals(other: Any?): Boolean = when(other){
        is KLazy<*> -> value == other.value
        else -> false
    }

    override fun hashCode(): Int = value.hashCode()

    override fun toString(): String = when(value){
        is Some<T> -> "KLazy(${value.get()})"
        is None -> "KLazy(?)"
    }
}