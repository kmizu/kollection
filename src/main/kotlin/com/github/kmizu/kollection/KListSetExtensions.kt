package com.github.kmizu.kollection

fun <T> KListSet(vararg elements: T): KListSet<T> = run {
    var list: KList<T> = KList.Nil
    for(e in elements) { list = e cons list }
    KListSet(list.reverse())
}
