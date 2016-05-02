package com.github.kmizu.kollection
import com.github.kmizu.kollection.Tree.*

class KTreeSet<T>(val comparator: (T, T) -> Int, val value: Tree<T> = Tree.Empty) : KImmutableSet<T> {
    override fun get(element: T): Boolean = run {
        fun contains(e: T, t: Tree<T>): Boolean = when(t) {
            is Tree.Empty -> false
            is Tree.Node<T> -> {
                if (comparator(e, t.element) < 0) contains(e, t.leftChild)
                else if(comparator(t.element, e) < 0) contains(e, t.rightChild)
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
                if(comparator(element, tree.element) < 0) balance(tree.color, insert(tree.leftChild), tree.element, tree.rightChild)
                else if(comparator(tree.element, element) < 0) balance(tree.color, tree.leftChild, tree.element, insert(tree.rightChild))
                else tree
            }
        }
        val t = insert(value)
        KTreeSet(comparator, Node(Color.BLACK, t.leftChild, t.element, t.rightChild))
    }

    override val isEmpty: Boolean
        get() = value is Tree.Empty
}
