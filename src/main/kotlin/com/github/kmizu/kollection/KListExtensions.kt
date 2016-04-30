package com.github.kmizu.kollection

import com.github.kmizu.kollection.type_classes.KMonoid

infix fun <T> T.cons(other: KList<T>): KList<T> = KList.Cons(this, other)

fun <T> KList(vararg elements: T): KList<T> = run {
    KList.make(*elements)
}

infix fun <T> KList<T>.concat(right: KList<T>): KList<T> = run {
    this.reverse().foldLeft(right){result, e -> e cons result}
}

fun <T> KList<KList<T>>.flatten(): KList<T> = run {
    this.flatMap {x -> x}
}

fun <T, U> KList<Pair<T, U>>.unzip(): Pair<KList<T>, KList<U>> = run {
    tailrec fun loop(rest: KList<Pair<T, U>>, a: KList<T>, b: KList<U>): Pair<KList<T>, KList<U>> = when(rest) {
        is KList.Nil -> Pair(a.reverse(), b.reverse())
        is KList.Cons<Pair<T, U>> -> loop(rest.tl, rest.hd.first cons a, rest.hd.second cons b)
    }
    loop(this, KList.Nil, KList.Nil)
}

fun <T> KList<T>.sum(KMonoid: KMonoid<T>): T = this.foldLeft(KMonoid.mzero()){ a, e -> KMonoid.mplus(a, e)}