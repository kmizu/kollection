package com.github.kmizu.kollection

enum class Color {RED, BLACK}
sealed class Tree<out T> {
    object Empty: Tree<Nothing>()
    class Node<T>(val c: Color, val l: Tree<T>, val e: T, val r: Tree<T>): Tree<T>()
}
fun <T:Comparable<T>> KTreeSet(vararg elements: T): KTreeSet<T> = run {
    val comparator = {a: T, b: T -> a.compareTo(b)}
    KTreeSet(comparator, *elements)
}
fun <T> KTreeSet(comparator: (T, T) -> Int, vararg elements: T): KTreeSet<T> = run {
    var set: KTreeSet<T> = KTreeSet(comparator, Tree.Empty)
    for(e in elements) {
        set = set + e
    }
    set
}