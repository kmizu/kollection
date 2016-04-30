package com.github.kmizu.kollection

sealed class KList<out T>() : KFoldable<T>, KImmutableLinearSequence<T> {
    companion object {
        fun <T> make(vararg elements: T): KList<T> = run {
            var result: KList<T> = Nil
            for(e in elements.reversed()) {
                result = e.cons(result)
            }
            result
        }
    }
    class Cons<out T>(head: T, tail: KList<T>) : KList<T>() {
        override fun equals(other: Any?): Boolean = when (other) {
            is Cons<*> -> this.hd == other.hd && this.tl == other.tl
            else -> false
        }
        override val hd: T
        override val tl: KList<T>
        init {
            hd = head
            tl = tail
        }

        override fun hashCode(): Int = tl.hashCode() + (hd?.hashCode() ?: 0)
    }
    object Nil : KList<Nothing>() {
        override fun equals(other: Any?): Boolean = when (other) {
            is Nil -> true
            else -> false
        }
        override val hd: Nothing
            get() = throw IllegalArgumentException("KList.Nil")
        override val tl: Nothing
            get() = throw IllegalArgumentException("KList.Nil")
    }
    abstract override val hd: T
    abstract override val tl: KList<T>
    override fun toString(): String = run {
        val buffer = StringBuilder()
        fun loop(list: KList<T>): Unit = when(list){
            is Cons<T> -> run {
                buffer.append(", ${list.hd}")
                loop(list.tl)
                Unit
            }
            is Nil -> run {
                buffer.append(")")
                Unit
            }
        }
        buffer.append("KList(")
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
    fun reverse(): KList<T> = run {
        tailrec fun loop(accumlator: KList<T>, rest: KList<T>): KList<T> = when(rest) {
            is Nil -> accumlator
            is Cons<T> -> loop(rest.hd.cons(accumlator), rest.tl)
        }
        loop(Nil, this)
    }
    override fun <U> foldLeft(z: U, function: (U, T) -> U): U  = run {
        tailrec fun loop(list: KList<T>, accumulator: U): U = when(list) {
            is Cons<T> -> loop(list.tl, function(accumulator, list.hd))
            is Nil -> accumulator
        }
        loop(this, z)
    }
    override fun <U> foldRight(z: U, function: (T, U) -> U): U = run {
        tailrec fun loop(list: KList<T>, accumulator: U): U = when(list) {
            is Cons<T> -> loop(list.tl, function(list.hd, accumulator))
            is Nil -> accumulator
        }
        loop(this.reverse(), z)
    }
    fun <U> map(function: (T) -> U): KList<U>  = run {
        tailrec fun loop(list: KList<T>, result: KList<U>): KList<U> = when(list) {
            is Cons<T> -> loop(list.tl, function(list.hd) cons result)
            is Nil -> result
        }
        loop(this, Nil).reverse()
    }
    fun <U> flatMap(function: (T) -> KList<U>): KList<U> = run {
        var result: KList<U> = Nil
        var rest: KList<T> = this
        while(rest != Nil) {
            var rest2: KList<U> = function(rest.hd)
            rest = rest.tl
            while(rest2 != Nil) {
                result = rest2.hd cons result
                rest2 = rest2.tl
            }
        }
        result.reverse()
    }
    override val isEmpty: Boolean
        get() = when(this) {
            is Cons<T> -> false
            is Nil -> true
        }
    val length: Int
        get() = run {
            tailrec fun loop(rest: KList<T>, count: Int): Int = when(rest) {
                is Cons<T> -> loop(rest.tl, count + 1)
                is Nil -> count
            }
            loop(this, 0)
        }
    infix fun <U> zip(another: KList<U>): KList<Pair<T, U>> = run {
        tailrec fun loop(a: KList<T>, b: KList<U>, result: KList<Pair<T, U>>): KList<Pair<T, U>> =
            if(!a.isEmpty && !b.isEmpty){
                loop(a.tl, b.tl, Pair(a.hd, b.hd) cons result)
            } else {
                result.reverse()
            }
        loop(this, another, Nil)
    }
    operator fun get(index: Int): T = run {
        tailrec fun loop(a: KList<T>, i: Int): T = when(a) {
            is Cons<T> -> if(i == 0) a.hd else loop(a.tl, i - 1)
            is Nil -> throw IllegalArgumentException("KList.Nil")
        }
        if(index < 0) {
            throw IllegalArgumentException("negative index")
        } else {
            loop(this, index)
        }
    }
    fun forAll(predicate: (T) -> Boolean): Boolean = this.all(predicate)
    fun exists(predicate: (T) -> Boolean): Boolean = this.any(predicate)

}

