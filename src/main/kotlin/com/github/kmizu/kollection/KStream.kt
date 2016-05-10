package com.github.kmizu.kollection

sealed class KStream<out T> : KFoldable<T>, KLinearSequence<T> {
    companion object {
        fun <T> forever(action: () -> T): KStream<T> = action() cons { forever(action) }
        fun from(number: Int): KStream<Int> = number cons { from(number + 1 ) }
    }
    abstract override val hd: T
    abstract override val tl: KStream<T>
    abstract val isTailDefined: Boolean
    override val isEmpty: Boolean
        get() = when(this) {
            is Cons<T> -> false
            is Nil -> true
        }
    class Cons<out T>(head: T, tail: () -> KStream<T>) : KStream<T>() {
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
            is Cons<*> -> this.hd == other.hd && this.tl == other.tl
            else -> false
        }
        override fun hashCode(): Int = tl.hashCode() + (hd?.hashCode() ?: 0)
    }
    object Nil : KStream<Nothing>() {
        override val hd: Nothing
            get() = throw IllegalArgumentException("KStream.Nil")
        override val tl: Nothing
            get() = throw IllegalArgumentException("KStream.Nil")
        override val isTailDefined: Boolean
            get() = throw IllegalArgumentException("KStream.Nil")
        override fun equals(other: Any?): Boolean = when (other) {
            is Nil -> true
            else -> false
        }
    }
    override fun toString(): String = run {
        val buffer = StringBuilder()
        fun loop(stream: KStream<T>): Unit = when(stream){
            is Cons<T> -> run {
                buffer.append(", ${stream.hd}")
                if(stream.isTailDefined) {
                    loop(stream.tl)
                } else {
                    buffer.append(", ?)")
                }
                Unit
            }
            is Nil -> run {
                buffer.append(")")
                Unit
            }
        }
        buffer.append("KStream(")
        when(this) {
            is Cons<T> -> run {
                buffer.append("${this.hd}")
                loop(this.tl)
            }
            is Nil -> run {
                buffer.append(")")
            }
        }
        String(buffer)
    }
    infix fun <U> map(function: (T) -> U): KStream<U> = when(this) {
        is Cons<T> -> function(this.hd) cons { this.tl.map(function) }
        is Nil -> Nil
    }
    infix fun <U> flatMap(function: (T) -> KStream<U>): KStream<U> = when(this) {
        is Cons<T> -> function(this.hd) concat { this.tl.flatMap(function) }
        is Nil -> Nil
    }
    infix fun take(n: Int): KStream<T> = run {
        if (n <= 0 || isEmpty) Nil
        else if (n == 1) this.hd cons { Nil }
        else this.hd cons { this.tl take (n - 1) }
    }
    infix fun drop(n: Int): KStream<T> = run {
        if (n <= 0 || isEmpty) this
        else this.tl drop (n - 1)
    }
    override fun <U> foldLeft(z: U, function: (U, T) -> U): U  = run {
        tailrec fun loop(stream: KStream<T>, accumulator: U): U = when(stream) {
            is Cons<T> -> loop(stream.tl, function(accumulator, stream.hd))
            is Nil -> accumulator
        }
        loop(this, z)
    }
    override fun <U> foldRight(z: U, function: (T, U) -> U): U = run {
        tailrec fun loop(stream: KStream<T>, accumulator: U): U = when(stream) {
            is Cons<T> -> loop(stream.tl, function(stream.hd, accumulator))
            is Nil -> accumulator
        }
        loop(this.reverse(), z)
    }
    fun reverse(): KStream<T> = this.foldLeft(Nil as KStream<T>){ a, e -> e cons {a}}
    infix fun takeWhile(predicate: (T) -> Boolean): KStream<T> = run {
        if(isEmpty) Nil
        else if(predicate(this.hd)) this.hd cons { this.tl.takeWhile(predicate) }
        else Nil
    }
    infix fun dropWhile(predicate: (T) -> Boolean): KStream<T> = run {
        if(isEmpty || !predicate(this.hd)) this
        else this.tl dropWhile (predicate)
    }
    infix fun <U> zip(another: KStream<U>): KStream<Pair<T, U>> = run {
        if(this.isEmpty || another.isEmpty) Nil
        else Pair(this.hd, another.hd) cons { this.tl zip another.tl }
    }
    fun toKList(): KList<T> = run {
        fun loop(rest: KStream<T>, result: KList<T>): KList<T> = when(rest) {
            is Cons<T> -> loop(rest.tl, rest.hd cons result)
            is Nil -> result.reverse()
        }
        loop(this, KList.Nil)
    }
}