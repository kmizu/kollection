package com.github.kmizu.kollection
import com.github.kmizu.kollection.Tree.*

class KTreeSet<T>(val comparator: (T, T) -> Int, val value: Tree<T> = Tree.E) : KSet<T> {
    companion object {
        val BYTE_COMPARATOR: (Byte, Byte) -> Int = {a, b -> if(a < b) -1 else if(a > b) 1 else 0}
        val SHORT_COMPARATOR: (Short, Short) -> Int = {a, b -> if(a < b) -1 else if(a > b) 1 else 0}
        val INT_COMPARATOR: (Int, Int) -> Int = {a, b -> if(a < b) -1 else if(a > b) 1 else 0}
        val LONG_COMPARATOR: (Long, Long) -> Int = {a, b -> if(a < b) -1 else if(a > b) 1 else 0}
        val FLOAT_COMPARATOR: (Float, Float) -> Int = {a, b -> if(a < b) -1 else if(a > b) 1 else 0}
        val DOUBLE_COMPARATOR: (Double, Double) -> Int = {a, b -> if(a < b) -1 else if(a > b) 1 else 0}
        val BOOLEAN_COMPARATOR: (Boolean, Boolean) -> Int = {a, b -> if(!a && b) -1 else if(a && !b) 1 else 0}
    }
    override fun get(element: T): Boolean = run {
        fun contains(e: T, t: Tree<T>): Boolean = when(t) {
            is Tree.E -> false
            is Tree.N<T> -> {
                if (comparator(e, t.e) < 0) contains(e, t.l)
                else if(comparator(t.e, e) < 0) contains(e, t.r)
                else true
            }
        }
        contains(element, this.value)
    }

    private fun balance(c: Color, l: Tree<T>, e: T, r: Tree<T>): N<T> = run {
        if(c == Color.BLACK && l is N<T> && l.c == Color.RED && l.l is N<T> && l.l.c == Color.RED)
            N(Color.RED, N(Color.BLACK, l.l.l, l.l.e, l.l.r), l.e, N(Color.BLACK, l.r, e, r))
        else if(c == Color.BLACK && l is N<T> && l.c == Color.RED && l.r is N<T> && l.r.c == Color.RED)
            N(Color.RED, N(Color.BLACK, l.l, l.e, l.r.l), l.r.e, N(Color.BLACK, l.r.l, e, r))
        else if(c == Color.BLACK && r is N<T> && r.c == Color.RED && r.l is N<T> && r.l.c == Color.RED)
            N(Color.RED, N(Color.BLACK, l, e, r.l.l), r.l.e, N(Color.BLACK, r.l.r, r.e, r.r))
        else if(c == Color.BLACK && r is N<T> && r.c == Color.RED && r.r is N<T> && r.r.c == Color.RED)
            N(Color.RED, N(Color.BLACK, l, e, r.l), r.e, N(Color.BLACK, r.r.l, r.r.e, r.r.r))
        else
            N(c, l, e, r)
    }

    override operator fun plus(element: T): KTreeSet<T> = run {
        fun insert(tree: Tree<T>): N<T> = when(tree) {
            is E -> N(Color.RED, E, element, E)
            is N -> {
                if(comparator(element, tree.e) < 0) balance(tree.c, insert(tree.l), tree.e, tree.r)
                else if(comparator(tree.e, element) < 0) balance(tree.c, tree.l, tree.e, insert(tree.r))
                else tree
            }
        }
        val t = insert(value)
        KTreeSet(comparator, N(Color.BLACK, t.l, t.e, t.r))
    }

    override val isEmpty: Boolean
        get() = value is Tree.E

    override fun iterator(): Iterator<T> = object: Iterator<T> {
        private var list: KList<T> = listFrom(value)
        fun listFrom(tree: Tree<T>): KList<T> = when(tree) {
            is E -> KList.Nil
            is N -> listFrom(tree.l) concat KList(tree.e) concat listFrom(tree.r)
        }
        override fun hasNext(): Boolean = list is KList.Cons

        override fun next(): T = run {
            val v = list.hd
            list = list.tl
            v
        }
    }

    override fun remove(element: T): KTreeSet<T> = run {

        fun findTarget(e: T, tree: Tree<T>): N<T>? = when(tree) {
            is E -> null
            is N<T> -> {
                if(comparator(e, tree.e) < 0) findTarget(e, tree.l)
                else if(comparator(tree.e, e) < 0) findTarget(e, tree.r)
                else tree
            }
        }
        val target = findTarget(element, value)
        when(target) {
            null -> KTreeSet(comparator, E)
            else -> {
                TODO()
            }
        }
    }
}
