package com.github.kmizu.kollection

sealed abstract class KStream<out T> {
    abstract val hd: T
    abstract val tl: KStream<T>
    val isEmpty: Boolean
        get() = when(this) {
            is KStreamCons<T> -> false
            is KStreamNil -> true
        }
    class KStreamCons<out T>(head: T, tail: () -> KStream<T>) : KStream<T>() {
        private var tlVal: KStream<T>? = null
        private var tlGen: (() -> KStream<T>)? = tail
        private fun tailDefined(): Boolean = tlGen == null
        override val hd: T = head
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
        if (n <= 0 || isEmpty) KStreamNil
        else if (n == 1) KStreamCons(this.hd, { KStreamNil})
        else KStreamCons(this.hd, { this.tl take (n - 1) })
    }
    infix fun drop(n: Int): KStream<T> = run {
        if (n <= 0 || isEmpty) this
        else this.tl drop (n - 1)
    }
    infix fun takeWhile(predicate: (T) -> Boolean): KStream<T> = run {
        if(isEmpty) KStreamNil
        else if(predicate(this.hd)) KStreamCons(this.hd, { this.tl.takeWhile(predicate) })
        else KStreamNil
    }
    infix fun dropWhile(predicate: (T) -> Boolean): KStream<T> = run {
        if(isEmpty || !predicate(this.hd)) this
        else this.tl dropWhile (predicate)
    }
    fun toKList(): KList<T> = run {
        fun loop(rest: KStream<T>, result: KList<T>): KList<T> = when(rest) {
            is KStreamCons<T> -> loop(rest.tl, rest.hd cons result)
            is KStreamNil -> result.reverse()
        }
        loop(this, KList.KNil)
    }
}