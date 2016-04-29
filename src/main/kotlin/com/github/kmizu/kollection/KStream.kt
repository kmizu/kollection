package com.github.kmizu.kollection

sealed abstract class KStream<out T> {
    abstract val hd: T
    abstract val tl: KStream<T>
    class KStreamCons<out T>(internal val head: T, internal val tail: () -> KStream<T>) : KStream<T>() {
        private var tlVal: KStream<T>? = null
        private var tlGen: (() -> KStream<T>)? = tail
        private fun tailDefined(): Boolean = tlGen == null
        override val hd: T
            get() = head
        override val tl: KStream<T>
            get() = run {
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
            is KStreamCons<*> -> this.hd == other.hd && this.tl == other.tl
            else -> false
        }
        override fun hashCode(): Int = tl.hashCode() + (hd?.hashCode() ?: 0)
        override fun toString(): String = if(tailDefined()) {
            hd.toString() + "  :% " + tl.toString()
        } else {
            hd.toString() + "  :% ?"
        }
    }
    object KStreamNil : KStream<Nothing>() {
        override val hd: Nothing
            get() = throw IllegalArgumentException("KStreamNil")
        override val tl: Nothing
            get() = throw IllegalArgumentException("KStreamNil")

        override fun equals(other: Any?): Boolean = when (other) {
            is KStreamNil -> true
            else -> false
        }
        override fun toString(): String = "KStreamNil"
    }
    infix fun <U> map(function: (T) -> U): KStream<U> = when(this) {
        is KStreamCons<T> -> KStreamCons(function(this.hd), { this.tl.map(function) })
        is KStreamNil -> KStreamNil
    }
    infix fun take(n: Int): KStream<T> = run {
        if (n <= 0 || this == KStreamNil) KStreamNil
        else if (n == 1) KStreamCons(this.hd, { KStreamNil})
        else KStreamCons(this.hd, { this.tl take (n - 1) })
    }
}