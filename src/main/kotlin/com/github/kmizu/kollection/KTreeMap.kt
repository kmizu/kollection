package com.github.kmizu.kollection

import com.github.kmizu.kollection.RedBlackTree as RB
import com.github.kmizu.kollection.RedBlackTree.Tree
import java.util.*

class KTreeMap<A:Any, B:Any>(val tree: Tree<A, B>?, val comparator: Comparator<A>): KMap<A, B> {
    companion object {
        fun <A:Any, B:Any> empty(comparator: Comparator<A>): KTreeMap<A, B> = KTreeMap(null, comparator)
    }

    private fun elementsOf(tree: Tree<A, B>?): KList<Pair<A, B>> = run {
        if(tree === null) {
            KList.Nil
        } else {
            elementsOf(tree.left) concat KList(Pair(tree.key, tree.value)) concat elementsOf(tree.right)
        }
    }

    private fun newMap(t: RB.Tree<A, B>?): KTreeMap<A, B> = KTreeMap<A, B>(t, comparator)

    override fun iterator(): Iterator<Pair<A, B>> = object: Iterator<Pair<A, B>> {
        private var list: KList<Pair<A, B>> = elementsOf(tree)
        override fun hasNext(): Boolean = list is KList.Cons

        override fun next(): Pair<A, B> = run {
            val v = list.hd
            list = list.tl
            v
        }
    }

    override infix operator fun plus(kvPair: Pair<A, B>): KTreeMap<A, B> = run {
        val (key, value) = kvPair
        newMap(RB.update(tree, key, value, false, comparator))
    }

    override fun remove(key: A): KTreeMap<A, B> = run {
        if (!RB.contains(tree, key, comparator)) this
        else newMap(RB.delete(tree, key, comparator))
    }

    override fun get(key: A): KOption<B> = run {
        val value = RB.get(tree, key, comparator)
        if(value == null)
            KOption.None
        else
            KOption.Some(value)
    }

    override val size: Int
        get() = RB.sizeOf(tree)

    override val isEmpty: Boolean
        get() = RB.isEmpty(tree)
}

