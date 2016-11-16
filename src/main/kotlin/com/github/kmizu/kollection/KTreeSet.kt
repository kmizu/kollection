package com.github.kmizu.kollection

import com.github.kmizu.kollection.RedBlackTree as RB
import com.github.kmizu.kollection.RedBlackTree.Tree
import com.github.kmizu.kollection.RedBlackTree.Tree.RedTree
import com.github.kmizu.kollection.RedBlackTree.Tree.BlackTree
import java.util.*

class KTreeSet<A:Any>(val tree: Tree<A, Unit>?, val comparator: Comparator<A>): KSet<A> {
    companion object {
        fun <A:Any> empty(comparator: Comparator<A>): KTreeSet<A> = KTreeSet(null, comparator)
    }

    private fun elementsOf(tree: Tree<A, Unit>?): KList<A> = run {
        if(tree === null) {
            KList.Nil
        } else {
            elementsOf(tree.left) concat KList(tree.key) concat elementsOf(tree.right)
        }
    }

    private fun newSet(t: RB.Tree<A, Unit>?): KTreeSet<A> = KTreeSet<A>(t, comparator)

    override fun iterator(): Iterator<A> = object: Iterator<A> {
        private var list: KList<A> = elementsOf(tree)
        override fun hasNext(): Boolean = list is KList.Cons

        override fun next(): A = run {
            val v = list.hd
            list = list.tl
            v
        }
    }

    override infix operator fun plus(element: A): KTreeSet<A> = run {
        newSet(RB.update(tree, element, Unit, false, comparator))
    }

    override fun remove(element:A): KTreeSet<A> = run {
        if (!RB.contains(tree, element, comparator)) this
        else newSet(RB.delete(tree, element, comparator))
    }

    override fun get(element: A): Boolean = RB.contains(tree, element, comparator)

    override val isEmpty: Boolean
        get() = RB.isEmpty(tree)
}

