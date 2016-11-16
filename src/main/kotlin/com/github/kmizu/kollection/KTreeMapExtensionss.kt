package com.github.kmizu.kollection

import java.util.Comparator

fun <A:Comparable<A>, B:Any> KTreeMap(vararg elements: Pair<A, B>): KTreeMap<A, B> = run {
    val newComparator: Comparator<A> = object : Comparator<A> {
        override fun compare(a: A, b: A): Int = a.compareTo(b)
    }
    var map: KTreeMap<A, B> = KTreeMap.empty(newComparator)
    for((key, value) in elements) {
        map = map + (key to value)
    }
    map
}
