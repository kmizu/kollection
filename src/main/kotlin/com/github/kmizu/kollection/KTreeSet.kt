package com.github.kmizu.kollection
import com.github.kmizu.kollection.Tree.*

class KTreeSet<T>(val comparator: (T, T) -> Int, val value: Tree<T> = Tree.Empty) : KImmutableSet<T> {
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

    private fun balance(color: Color, l: Tree<T>, e: T, r: Tree<T>): Node<T> = TODO()

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
