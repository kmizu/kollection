package com.github.kmizu.kollection

import java.util.*

import com.github.kmizu.kollection.RedBlackTree.Tree.RedTree
import com.github.kmizu.kollection.RedBlackTree.Tree.BlackTree

object RedBlackTree {
    fun isEmpty(tree: Tree<*, *>?): Boolean = tree === null

    fun <A> contains(tree: Tree<A, *>?, x: A, comparator: Comparator<A>): Boolean = lookup(tree, x, comparator) !== null
    fun <A, B> get(tree: Tree<A, B>?, x: A, comparator: Comparator<A>): B? = run {
        if(tree == null) {
            null
        } else {
            val result = lookup(tree, x, comparator)
            if(result == null) {
                null
            } else {
                result.value
            }
        }
    }

    fun <A, B> sizeOf(tree: Tree<A, B>?): Int = run {
        if(tree === null) {
            0
        } else {
            sizeOf(tree.left) + 1 + sizeOf(tree.right)
        }
    }

    tailrec
    fun <A, B> lookup(tree: Tree<A, B>?, x: A, comparator: Comparator<A>): Tree<A, B>? = if (tree === null) null else {
        val cmp = comparator.compare(x, tree.key)
        if (cmp < 0) lookup(tree.left, x, comparator)
        else if (cmp > 0) lookup(tree.right, x, comparator)
        else tree
    }

    fun count(tree: Tree<*, *>?): Int = if (tree === null) 0 else tree.count

    fun <A> countInRange(tree: Tree<A, *>?, from: A?, to: A?, comparator: Comparator<A>) : Int = run {
        if (tree === null)
            0
        else {
            when {
                from === null && to === null ->
                    tree.count
                from !== null && comparator.compare(tree.key, from) < 0 ->
                    countInRange(tree.right, from, to, comparator)
                to !== null && comparator.compare(tree.key, to) >= 0 ->
                    countInRange(tree.left, from, to, comparator)
                else ->
                    1 + countInRange(tree.left, from, null, comparator) + countInRange(tree.right, null, to, comparator)
            }
        }
    }

    fun <A, B> update(tree: Tree<A, B>?, k: A, v: B, overwrite: Boolean, comparator: Comparator<A>): Tree<A, B>? = run {
        blacken(upd(tree, k, v, overwrite, comparator))
    }
    fun <A, B> delete(tree: Tree<A, B>?, k: A, comparator: Comparator<A>): Tree<A, B>? = blacken(del(tree, k, comparator))
    fun <A, B> rangeImpl(tree: Tree<A, B>?, from: A?, until: A?, comparator: Comparator<A>): Tree<A, B>? = when {
        from !== null && until !== null -> this.range(tree, from, until, comparator)
        from !== null && until === null -> this.from(tree, from, comparator)
        from === null && until !== null -> this.until(tree, until, comparator)
        else -> tree
    }
    fun <A, B> range(tree: Tree<A, B>?, from: A, until: A, comparator: Comparator<A>): Tree<A, B>? = blacken(doRange(tree, from, until, comparator))
    fun <A, B> from(tree: Tree<A, B>?, from: A, comparator: Comparator<A>): Tree<A, B>? = blacken(doFrom(tree, from, comparator))
    fun <A, B> to(tree: Tree<A, B>?, to: A, comparator: Comparator<A>): Tree<A, B>? = blacken(doTo(tree, to, comparator))
    fun <A, B> until(tree: Tree<A, B>?, key: A, comparator: Comparator<A>): Tree<A, B>? = blacken(doUntil(tree, key, comparator))

    fun <A, B> drop(tree: Tree<A, B>?, n: Int): Tree<A, B>? = blacken(doDrop(tree, n))
    fun <A, B> take(tree: Tree<A, B>?, n: Int): Tree<A, B>? = blacken(doTake(tree, n))
    fun <A, B> slice(tree: Tree<A, B>?, from: Int, until: Int): Tree<A, B>? = blacken(doSlice(tree, from, until))

    fun <A, B> smallest(tree: Tree<A, B>?): Tree<A, B> = run {
        if (tree === null) throw NoSuchElementException("empty map")

        tailrec
        fun loop(node: Tree<A, B>): Tree<A, B> {
            if(node.left !== null) {
                return loop(node.left)
            } else {
                return node
            }
        }

        loop(tree)
    }
    fun <A, B> greatest(tree: Tree<A, B>?): Tree<A, B> = run {
        if (tree === null) throw NoSuchElementException("empty map")

        tailrec
        fun loop(node: Tree<A, B>): Tree<A, B> {
            if(node.right !== null) {
                return loop(node.right)
            } else {
                return node
            }
        }

        loop(tree)
    }

    tailrec
    fun <A, B> nth(tree: Tree<A, B>?, n: Int): Tree<A, B>? {
        if(tree === null) {
            return null
        } else {
            val count = this.count(tree.left)
            if (n < count) return nth(tree.left, n)
            else if (n > count) return nth(tree.right, n - count - 1)
            else return tree
        }
    }

    fun isBlack(tree: Tree<*, *>?) = (tree === null) || isBlackTree(tree)

    private fun isRedTree(tree: Tree<*, *>) = tree is RedBlackTree.Tree.RedTree<*, *>
    private fun isBlackTree(tree: Tree<*, *>) = tree is RedBlackTree.Tree.BlackTree<*, *>

    private fun <A, B> blacken(t: Tree<A, B>?): Tree<A, B>? = if (t === null) null else t.black

    private fun <A, B> mkTree(isBlack: Boolean, k: A, v: B, l: Tree<A, B>?, r: Tree<A, B>?): Tree<A, B> = run {
        if (isBlack) BlackTree(k, v, l, r) else RedTree(k, v, l, r)
    }

    private fun <A, B> balanceLeft(isBlack: Boolean, z: A, zv: B, l: Tree<A, B>?, d: Tree<A, B>?): Tree<A, B> = run {
        if (l is RedTree<A, B> && l.left is RedTree<A, B>)
            RedTree(l.key, l.value, BlackTree(l.left.key, l.left.value, l.left.left, l.left.right), BlackTree(z, zv, l.right, d))
        else if (l is RedTree<A, B>  && l.right is RedTree<A, B>)
            RedTree(l.right.key, l.right.value, BlackTree(l.key, l.value, l.left, l.right.left), BlackTree(z, zv, l.right.right, d))
        else
            mkTree(isBlack, z, zv, l, d)
    }

    private fun <A, B> balanceRight(isBlack: Boolean, x: A, xv: B, a: Tree<A, B>?, r: Tree<A, B>?): Tree<A, B> = run {
        if (r is RedTree<A, B> && r.left is RedTree<A, B>)
            RedTree(r.left.key, r.left.value, BlackTree(x, xv, a, r.left.left), BlackTree(r.key, r.value, r.left.right, r.right))
        else if (r is RedTree<A, B> && r.right is RedTree<A, B>)
            RedTree(r.key, r.value, BlackTree(x, xv, a, r.left), BlackTree(r.right.key, r.right.value, r.right.left, r.right.right))
        else
            mkTree(isBlack, x, xv, a, r)
    }

    private fun <A, B> upd(tree: Tree<A, B>?, k: A, v: B, overwrite: Boolean, comparator: Comparator<A>): Tree<A, B> = if (tree === null) {
        RedTree(k, v, null, null)
    } else {
        val cmp = comparator.compare(k, tree.key)
        if (cmp < 0) balanceLeft(isBlackTree(tree), tree.key, tree.value, upd(tree.left, k, v, overwrite, comparator), tree.right)
        else if (cmp > 0) balanceRight(isBlackTree(tree), tree.key, tree.value, tree.left, upd(tree.right, k, v, overwrite, comparator))
        else if (overwrite || k != tree.key) mkTree(isBlackTree(tree), k, v, tree.left, tree.right)
        else tree
    }

    private fun <A, B> updNth(tree: Tree<A, B>?, idx: Int, k: A, v: B, overwrite: Boolean): Tree<A, B> = if (tree === null) {
        RedTree(k, v, null, null)
    } else {
        val rank = count(tree.left) + 1
        if (idx < rank) balanceLeft(isBlackTree(tree), tree.key, tree.value, updNth(tree.left, idx, k, v, overwrite), tree.right)
        else if (idx > rank) balanceRight(isBlackTree(tree), tree.key, tree.value, tree.left, updNth(tree.right, idx - rank, k, v, overwrite))
        else if (overwrite) mkTree(isBlackTree(tree), k, v, tree.left, tree.right)
        else tree
    }

    /* Based on Stefan Kahrs' Haskell version of Okasaki's Red&Black Trees
     * Constructing Red-Black Trees, Ralf Hinze: http://www.cs.ox.ac.uk/ralf.hinze/publications/WAAAPL99b.ps.gz
     * Red-Black Trees in a Functional Setting, Chris Okasaki: https://wiki.rice.edu/confluence/download/attachments/2761212/Okasaki-Red-Black.pdf */
    private fun <A, B> del(tree: Tree<A, B>?, k: A, comparator: Comparator<A>): Tree<A, B>? = if (tree === null) null else {
        fun balance(x: A, xv: B, tl: Tree<A, B>?, tr: Tree<A, B>?) = if (tl is RedTree<A, B>) {
            if (tr is RedTree<A, B>) {
                RedTree(x, xv, tl.black, tr.black)
            } else if (tl.left is RedTree<A, B>) {
                RedTree(tl.key, tl.value, tl.left.black, BlackTree(x, xv, tl.right, tr))
            } else if (tl.right is RedTree<A, B>) {
                RedTree(tl.right.key, tl.right.value, BlackTree(tl.key, tl.value, tl.left, tl.right.left), BlackTree(x, xv, tl.right.right, tr))
            } else {
                BlackTree(x, xv, tl, tr)
            }
        } else if (tr is RedTree<A, B>) {
            if (tr.right is RedTree<A, B>) {
                RedTree(tr.key, tr.value, BlackTree(x, xv, tl, tr.left), tr.right.black)
            } else if (tr.left is RedTree<A, B>) {
                RedTree(tr.left.key, tr.left.value, BlackTree(x, xv, tl, tr.left.left), BlackTree(tr.key, tr.value, tr.left.right, tr.right))
            } else {
                BlackTree(x, xv, tl, tr)
            }
        } else {
            BlackTree(x, xv, tl, tr)
        }
        fun subl(t: Tree<A, B>?): Tree<A, B> = run {
            if (t is BlackTree<A, B>) t.red
            else throw Exception("Defect: invariance violation; expected black, got "+t)
        }

        fun balLeft(x: A, xv: B, tl: Tree<A, B>?, tr: Tree<A, B>?) = if (tl is RedTree<A, B>) {
            RedTree(x, xv, tl.black, tr)
        } else if (tr is BlackTree<A, B>) {
            balance(x, xv, tl, tr.red)
        } else if (tr is RedTree<A, B> && tr.left is BlackTree<A, B>) {
            RedTree(tr.left.key, tr.left.value, BlackTree(x, xv, tl, tr.left.left), balance(tr.key, tr.value, tr.left.right, subl(tr.right)))
        } else {
            throw Exception("Defect: invariance violation")
        }
        fun balRight(x: A, xv: B, tl: Tree<A, B>?, tr: Tree<A, B>?) = if (tr is RedTree<A, B>) {
            RedTree(x, xv, tl, tr.black)
        } else if (tl is BlackTree<A, B>) {
            balance(x, xv, tl.red, tr)
        } else if (tl is RedTree<A, B> && tl.right is BlackTree<A, B>) {
            RedTree(tl.right.key, tl.right.value, balance(tl.key, tl.value, subl(tl.left), tl.right.left), BlackTree(x, xv, tl.right.right, tr))
        } else {
            throw Exception("Defect: invariance violation")
        }
        fun delLeft() = run {
            if (tree.left is BlackTree<A, B>) balLeft(tree.key, tree.value, del(tree.left, k, comparator), tree.right) else RedTree(tree.key, tree.value, del(tree.left, k, comparator), tree.right)
        }
        fun delRight() = run {
            if (tree.right is BlackTree<A, B>) balRight(tree.key, tree.value, tree.left, del(tree.right, k, comparator)) else RedTree(tree.key, tree.value, tree.left, del(tree.right, k, comparator))
        }
        fun append(tl: Tree<A, B>?, tr: Tree<A, B>?): Tree<A, B>? = if (tl === null) {
            tr
        } else if (tr === null) {
            tl
        } else if (tl is RedTree<A, B> && tr is RedTree<A, B>) {
            val bc = append(tl.right, tr.left)
            if (bc is RedTree<A, B>) {
                RedTree(bc.key, bc.value, RedTree(tl.key, tl.value, tl.left, bc.left), RedTree(tr.key, tr.value, bc.right, tr.right))
            } else {
                RedTree(tl.key, tl.value, tl.left, RedTree(tr.key, tr.value, bc, tr.right))
            }
        } else if (isBlackTree(tl) && isBlackTree(tr)) {
            val bc = append(tl.right, tr.left)
            if (bc is RedTree<A, B>) {
                RedTree(bc.key, bc.value, BlackTree(tl.key, tl.value, tl.left, bc.left), BlackTree(tr.key, tr.value, bc.right, tr.right))
            } else {
                balLeft(tl.key, tl.value, tl.left, BlackTree(tr.key, tr.value, bc, tr.right))
            }
        } else if (tr is RedTree<A, B>) {
            RedTree(tr.key, tr.value, append(tl, tr.left), tr.right)
        } else if (tl is RedTree<A, B>) {
            RedTree(tl.key, tl.value, tl.left, append(tl.right, tr))
        } else {
            throw Exception("unmatched tree on append: " + tl + ", " + tr)
        }

        val cmp = comparator.compare(k, tree.key)
        if (cmp < 0) delLeft()
        else if (cmp > 0) delRight()
        else append(tree.left, tree.right)
    }

    private fun <A, B> doFrom(tree: Tree<A, B>?, from: A, comparator: Comparator<A>): Tree<A, B>? = run {
        if (tree == null) return null
        if (comparator.compare(tree.key, from) < 0) return doFrom(tree.right, from, comparator)
        val newLeft = doFrom(tree.left, from, comparator)
        if (newLeft === tree.left) tree
        else if (newLeft === null) upd(tree.right, tree.key, tree.value, false, comparator)
        else rebalance(tree, newLeft, tree.right)
    }
    private fun <A, B> doTo(tree: Tree<A, B>?, to: A, comparator: Comparator<A>): Tree<A, B>? = run {
        if (tree === null) return null
        if (comparator.compare(to, tree.key) < 0) return doTo(tree.left, to, comparator)
        val newRight = doTo(tree.right, to, comparator)
        if (newRight === tree.right) tree
        else if (newRight === null) upd(tree.left, tree.key, tree.value, false, comparator)
        else rebalance(tree, tree.left, newRight)
    }
    private fun <A, B> doUntil(tree: Tree<A, B>?, until: A, comparator: Comparator<A>): Tree<A, B>? = run {
        if (tree === null) return null
        if (comparator.compare(until, tree.key) <= 0) return doUntil(tree.left, until, comparator)
        val newRight = doUntil(tree.right, until, comparator)
        if (newRight === tree.right) tree
        else if (newRight === null) upd(tree.left, tree.key, tree.value, false, comparator)
        else rebalance(tree, tree.left, newRight)
    }
    private fun <A, B> doRange(tree: Tree<A, B>?, from: A, until: A, comparator: Comparator<A>): Tree<A, B>? = run {
        if (tree === null) return null
        if (comparator.compare(tree.key ,from) < 0) return doRange(tree.right, from, until, comparator)
        if (comparator.compare(until, tree.key) <= 0) return doRange(tree.left, from, until, comparator)
        val newLeft = doFrom(tree.left, from, comparator)
        val newRight = doUntil(tree.right, until, comparator)
        if ((newLeft === tree.left) && (newRight === tree.right)) tree
        else if (newLeft === null) upd(newRight, tree.key, tree.value, false, comparator)
        else if (newRight === null) upd(newLeft, tree.key, tree.value, false, comparator)
        else rebalance(tree, newLeft, newRight)
    }
    private fun <A, B> doDrop(tree: Tree<A, B>?, n: Int): Tree<A, B>? = run {
        if (n <= 0) return tree
        if (n >= this.count(tree)) return null
        if(tree === null) return tree
        val count = this.count(tree.left)
        if (n > count) return doDrop(tree.right, n - count - 1)
        val newLeft = doDrop(tree.left, n)
        if (newLeft === tree.left) tree
        else if (newLeft === null) updNth(tree.right, n - count - 1, tree.key, tree.value, overwrite = false)
        else rebalance(tree, newLeft, tree.right)
    }
    private fun <A, B> doTake(tree: Tree<A, B>?, n: Int): Tree<A, B>? = run {
        if (n <= 0) return null
        if (n >= this.count(tree)) return tree
        if(tree === null) return tree
        val count = this.count(tree.left)
        if (n <= count) return doTake(tree.left, n)
        val newRight = doTake(tree.right, n - count - 1)
        if (newRight === tree.right) tree
        else if (newRight === null) updNth(tree.left, n, tree.key, tree.value, overwrite = false)
        else rebalance(tree, tree.left, newRight)
    }
    private fun <A, B> doSlice(tree: Tree<A, B>?, from: Int, until: Int): Tree<A, B>? = run {
        if (tree === null) return null
        val count = this.count(tree.left)
        if (from > count) return doSlice(tree.right, from - count - 1, until - count - 1)
        if (until <= count) return doSlice(tree.left, from, until)
        val newLeft = doDrop(tree.left, from)
        val newRight = doTake(tree.right, until - count - 1)
        if ((newLeft === tree.left) && (newRight === tree.right)) tree
        else if (newLeft === null) updNth(newRight, from - count - 1, tree.key, tree.value, overwrite = false)
        else if (newRight === null) updNth(newLeft, until, tree.key, tree.value, overwrite = false)
        else rebalance(tree, newLeft, newRight)
    }

    data class Result<A, B>(val _1: KList<Tree<A, B>>, val _2: Boolean, val _3: Boolean, val _4: Int)

    private fun <A, B> compareDepth(left: Tree<A, B>?, right: Tree<A, B>?): Result<A, B> = run {
         fun unzip(zipper: KList<Tree<A, B>>, leftMost: Boolean): KList<Tree<A, B>> = run {
            val next = if (leftMost) zipper.hd.left else zipper.hd.right
            if (next === null) zipper
            else unzip(next cons zipper, leftMost)
        }

        fun unzipBoth(
            left: Tree<A, B>?,
            right: Tree<A, B>?,
            leftZipper: KList<Tree<A, B>>,
            rightZipper: KList<Tree<A, B>>,
            smallerDepth: Int
        ): Result<A, B> = run {
            if (left is BlackTree<A, B> && right is BlackTree<A, B>) {
                unzipBoth(left.right, right.left, left cons leftZipper, right cons rightZipper, smallerDepth + 1)
            } else if (left is RedTree<A, B> && right is RedTree<A, B>) {
                unzipBoth(left.right, right.left, left cons leftZipper, right cons rightZipper, smallerDepth)
            } else if (right is RedTree<A, B>) {
                unzipBoth(left, right.left, leftZipper, right cons rightZipper, smallerDepth)
            } else if (left is RedTree<A, B>) {
                unzipBoth(left.right, right, left cons leftZipper, rightZipper, smallerDepth)
            } else if (left === null && right === null) {
                Result(KList.Nil, true, false, smallerDepth)
            } else if (left === null && right is BlackTree<A, B>) {
                val leftMost = true
                Result(unzip(right cons rightZipper, leftMost), false, leftMost, smallerDepth)
            } else if (left is BlackTree<A, B> && right === null) {
                val leftMost = false
                Result(unzip(left cons leftZipper, leftMost), false, leftMost, smallerDepth)
            } else {
                throw Exception("unmatched trees in unzip: $left, $right")
            }
        }
        unzipBoth(left, right, KList.Nil, KList.Nil, 0)
    }

    private fun <A, B> rebalance(tree: Tree<A, B>, newLeft: Tree<A, B>?, newRight: Tree<A, B>?): Tree<A, B>? = run {
        tailrec
        fun  findDepth(zipper: KList<Tree<A, B>>, depth: Int): KList<Tree<A, B>> =
        if (zipper === KList.Nil) {
            throw Exception("Defect: unexpected empty zipper while computing range")
        } else if (zipper.hd is BlackTree<A, B>) {
            if (depth == 1) zipper else findDepth(zipper.tl, depth - 1)
        } else {
            findDepth(zipper.tl, depth)
        }

        // Blackening the smaller tree avoids balancing problems on union;
        // this can't be done later, though, or it would change the result of compareDepth
        val blkNewLeft = blacken(newLeft)
        val blkNewRight = blacken(newRight)
        val (zipper, levelled, leftMost, smallerDepth) = compareDepth(blkNewLeft, blkNewRight)

        if (levelled) {
            BlackTree(tree.key, tree.value, blkNewLeft, blkNewRight)
        } else {
            val zipFrom = findDepth(zipper, smallerDepth)
            val union: Tree<A, B> = if (leftMost) {
                RedTree(tree.key, tree.value, blkNewLeft, zipFrom.hd)
            } else {
                RedTree(tree.key, tree.value, zipFrom.hd, blkNewRight)
            }
            val zippedTree = zipFrom.tl.foldLeft(union) { tree, node ->
                if (leftMost)
                    balanceLeft(isBlackTree(node), node.key, node.value, tree, node.right)
                else
                    balanceRight(isBlackTree(node), node.key, node.value, node.left, tree)
            }
            zippedTree
        }
    }

    /*
     * Forcing direct fields access using the @inline annotation helps speed up
     * various operations (especially smallest/greatest and update/delete).
     *
     * Unfortunately the direct field access is not guaranteed to work (but
     * works on the current implementation of the Scala compiler).
     *
     * An alternative is to implement the these classes using plain old Java code...
     */
    sealed class Tree<A, out B>(
      val key: A,
      val value: B,
      val left: Tree<A, B>?,
      val right: Tree<A, B>?
    ) {
        val count: Int = 1 + RedBlackTree.count(left) + RedBlackTree.count(right)
        abstract val black: Tree<A, B>
        abstract val red: Tree<A, B>

        class RedTree<A, out B>(
                key: A,
                value: B,
                left: Tree<A, B>?,
                right: Tree<A, B>?
        ) : Tree<A, B>(key, value, left, right) {
            companion object {
                fun <A, B> invoke(key: A, value: B, left: Tree<A, B>?, right: Tree<A, B>?) = RedTree(key, value, left, right)
            }
            override val black: Tree<A, B>
                    get() = BlackTree(key, value, left, right)

            override val red: Tree<A, B>
                    get() = this
            override fun toString(): String = "RedTree($key, $value, $left, $right)"
        }
        class BlackTree<A, out B>(
                key: A,
                value: B,
                left: Tree<A, B>?,
                right: Tree<A, B>?
        ) : Tree<A, B>(key, value, left, right) {
            companion object {
                fun <A, B> invoke(key: A, value: B, left: Tree<A, B>?, right: Tree<A, B>?) = BlackTree(key, value, left, right)
            }
            override val black: Tree<A, B>
                    get() = this
            override val red: Tree<A, B>
                    get() = RedTree(key, value, left, right)
            override fun toString(): String = "BlackTree($key, $value, $left, $right)"
        }
    }
}