package com.github.kmizu.kollection

sealed abstract class KStream<out T> {
    abstract open fun hd(): T
    abstract open fun tl(): KStream<T>
    class KStreamCons<out T>(internal val head: T, internal val tail: () -> KStream<T>) : KStream<T>() {
        private var tlVal: KStream<T>? = null
        private var tlGen: (() -> KStream<T>)? = tail
        private fun tailDefined(): Boolean = tlGen == null
        override fun hd(): T = head
        override fun tl(): KStream<T> = run {
            if (!tailDefined()) {
                synchronized(this) {
                    if (!tailDefined()) {
                        tlVal = (tlGen!!)()
                        tlGen = null
                    }
                }
            }
            tlVal!!
        }
        override fun equals(other: Any?): Boolean = when (other) {
            is KStreamCons<*> -> this.hd() == other.hd() && this.tl() == other.tl()
            else -> false
        }
        override fun hashCode(): Int = tl().hashCode() + (hd()?.hashCode() ?: 0)
        override fun toString(): String = if(tailDefined()) {
            hd().toString() + "  :% " + tail.toString()
        } else {
            hd().toString() + "  :% ?"
        }
    }
    object KStreamNil : KStream<Nothing>() {
        override fun hd(): Nothing = throw IllegalArgumentException("KStreamNil")
        override fun tl(): Nothing = throw IllegalArgumentException("KStreamNil")

        override fun equals(other: Any?): Boolean = when (other) {
            is KStreamNil -> true
            else -> false
        }
        override fun toString(): String = "KStreamNil"
    }
    fun <U> map(function: (T) -> U): KStream<U> = when(this) {
        is KStreamCons<T> -> KStreamCons(function(this.hd()), { this.tl().map(function) })
        is KStreamNil -> KStreamNil
    }
}