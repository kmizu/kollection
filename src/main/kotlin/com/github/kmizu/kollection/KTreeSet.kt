package com.github.kmizu.kollection
import com.github.kmizu.kollection.Tree.*

class KTreeSet<T>(val comparator: (T, T) -> Int, val value: Tree<T> = Tree.Empty) : KImmutableSet<T> {
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
            is Tree.Empty -> false
            is Tree.Node<T> -> {
                if (comparator(e, t.e) < 0) contains(e, t.l)
                else if(comparator(t.e, e) < 0) contains(e, t.r)
                else true
            }
        }
        contains(element, this.value)
    }

    private fun balance(c: Color, l: Tree<T>, e: T, r: Tree<T>): Node<T> = run {
        if(c == Color.BLACK && l is Node<T> && l.c == Color.RED && l.l is Node<T> && l.l.c == Color.RED)
            Node(Color.RED, Node(Color.BLACK, l.l.l, l.l.e, l.l.r), l.e, Node(Color.BLACK, l.r, e, r))
        else if(c == Color.BLACK && l is Node<T> && l.c == Color.RED && l.r is Node<T> && l.r.c == Color.RED)
            Node(Color.RED, Node(Color.BLACK, l.l, l.e, l.r.l), l.r.e, Node(Color.BLACK, l.r.l, e, r))
        else if(c == Color.BLACK && r is Node<T> && r.c == Color.RED && r.l is Node<T> && r.l.c == Color.RED)
            Node(Color.RED, Node(Color.BLACK, l, e, r.l.l), r.l.e, Node(Color.BLACK, r.l.r, r.e, r.r))
        else if(c == Color.BLACK && r is Node<T> && r.c == Color.RED && r.r is Node<T> && r.r.c == Color.RED)
            Node(Color.RED, Node(Color.BLACK, l, e, r.l), r.e, Node(Color.BLACK, r.r.l, r.r.e, r.r.r))
        else
            Node(c, l, e, r)
    }

    override fun plus(element: T): KTreeSet<T> = run {
        fun insert(tree: Tree<T>): Node<T> = when(tree) {
            is Empty -> Node(Color.RED, Empty, element, Empty)
            is Node -> {
                if(comparator(element, tree.e) < 0) balance(tree.c, insert(tree.l), tree.e, tree.r)
                else if(comparator(tree.e, element) < 0) balance(tree.c, tree.l, tree.e, insert(tree.r))
                else tree
            }
        }
        val t = insert(value)
        KTreeSet(comparator, Node(Color.BLACK, t.l, t.e, t.r))
    }

    override val isEmpty: Boolean
        get() = value is Tree.Empty
}
