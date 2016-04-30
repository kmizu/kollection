package com.github.kmizu.kollection

sealed class KStream<out T> {
    companion object {
        fun <T> forever(action: () -> T): KStream<T> = action() cons { forever(action) }
        fun from(number: Int): KStream<Int> = number cons { from(number + 1 ) }
    }
    abstract val hd: T
    abstract val tl: KStream<T>
    abstract val isTailDefined: Boolean
    val isEmpty: Boolean
        get() = when(this) {
            is KStreamCons<T> -> false
            is KStreamNil -> true
        }
    class KStreamCons<out T>(head: T, tail: () -> KStream<T>) : KStream<T>() {
        private var tlVal: KStream<T>? = null
        private var tlGen: (() -> KStream<T>)? = tail
        override val isTailDefined: Boolean
            get() = tlGen == null
        override val hd: T = head
        override val tl: KStream<T>
            get() = run {
                if (!isTailDefined) {
                    synchronized(this) {
                        if (!isTailDefined) {
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
    }
    object KStreamNil : KStream<Nothing>() {
        override val hd: Nothing
            get() = throw IllegalArgumentException("KStreamNil")
        override val tl: Nothing
            get() = throw IllegalArgumentException("KStreamNil")
        override val isTailDefined: Boolean
            get() = throw IllegalArgumentException("KStreamNil")
        override fun equals(other: Any?): Boolean = when (other) {
            is KStreamNil -> true
            else -> false
        }
        override fun toString(): String = "KStreamNil"
    }
    override fun toString(): String = run {
        val buffer = StringBuilder()
        fun loop(stream: KStream<T>): Unit = when(stream){
            is KStreamCons<T> -> run {
                buffer.append(", ${stream.hd}")
                if(stream.isTailDefined) {
                    loop(stream.tl)
                } else {
                    buffer.append(", ?)")
                }
                Unit
            }
            is KStreamNil -> run {
                buffer.append(")")
                Unit
            }
        }
        buffer.append("KStream(")
        when(this) {
            is KStreamCons<T> -> run {
                buffer.append("${this.hd}")
                loop(this.tl)
            }
            is KStreamNil -> run {
                buffer.append(")")
            }
        }
        String(buffer)
    }
    infix fun <U> map(function: (T) -> U): KStream<U> = when(this) {
        is KStreamCons<T> -> function(this.hd) cons { this.tl.map(function) }
        is KStreamNil -> KStreamNil
    }
    infix fun take(n: Int): KStream<T> = run {
        if (n <= 0 || isEmpty) KStreamNil
        else if (n == 1) this.hd cons { KStreamNil}
        else this.hd cons { this.tl take (n - 1) }
    }
    infix fun drop(n: Int): KStream<T> = run {
        if (n <= 0 || isEmpty) this
        else this.tl drop (n - 1)
    }
    fun <U> foldLeft(z: U, function: (U, T) -> U): U  = run {
        tailrec fun loop(stream: KStream<T>, accumulator: U): U = when(stream) {
            is KStreamCons<T> -> loop(stream.tl, function(accumulator, stream.hd))
            is KStreamNil -> accumulator
        }
        loop(this, z)
    }
    infix fun takeWhile(predicate: (T) -> Boolean): KStream<T> = run {
        if(isEmpty) KStreamNil
        else if(predicate(this.hd)) this.hd cons { this.tl.takeWhile(predicate) }
        else KStreamNil
    }
    infix fun dropWhile(predicate: (T) -> Boolean): KStream<T> = run {
        if(isEmpty || !predicate(this.hd)) this
        else this.tl dropWhile (predicate)
    }
    infix fun <U> zip(another: KStream<U>): KStream<Pair<T, U>> = run {
        if(this.isEmpty || another.isEmpty) KStreamNil
        else Pair(this.hd, another.hd) cons { this.tl zip another.tl }
    }
    fun toKList(): KList<T> = run {
        fun loop(rest: KStream<T>, result: KList<T>): KList<T> = when(rest) {
            is KStreamCons<T> -> loop(rest.tl, rest.hd cons result)
            is KStreamNil -> result.reverse()
        }
        loop(this, KList.KNil)
    }
}