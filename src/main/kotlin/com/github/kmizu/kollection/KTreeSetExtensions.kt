package com.github.kmizu.kollection

import java.util.Comparator

fun <A:Comparable<A>> KTreeSet(vararg elements: A): KTreeSet<A> = run {
    val newComparator: Comparator<A> = object : Comparator<A> {
        override fun compare(a: A, b: A): Int = a.compareTo(b)
    }
    var set: KTreeSet<A> = KTreeSet.empty(newComparator)
    for(element in elements) {
        set += element
    }
    set
}
