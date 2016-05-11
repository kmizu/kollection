package com.github.kmizu.kollection

fun <K, V> KListMap(vararg elements: Pair<K, V>): KListMap<K, V> = run {
    var list: KList<Pair<K, V>> = KList.Nil
    for(e in elements) { list = e cons list }
    KListMap(list.reverse())
}
