package com.github.kmizu.kollection

infix fun <T> T.cons(other: KList<T>): KList<T> = KList.KCons(this, other)

fun <T> KList(vararg elements: T): KList<T> = run {
    KList.make(*elements)
}

infix fun <T> KList<T>.concat(right: KList<T>): KList<T> = run {
    this.reverse().foldLeft(right){result, e -> e cons result}
}

fun <T, U> KList<Pair<T, U>>.unzip(): Pair<KList<T>, KList<U>> = run {
    tailrec fun loop(rest: KList<Pair<T, U>>, a: KList<T>, b: KList<U>): Pair<KList<T>, KList<U>> = when(rest) {
        is KList.KNil -> Pair(a.reverse(), b.reverse())
        is KList.KCons<Pair<T, U>> -> loop(rest.tail, rest.head.first cons a, rest.head.second cons b)
    }
    loop(this, KList.KNil, KList.KNil)
}
