package com.github.kmizu.kollection
import com.github.kmizu.kollection.kontrol.block

infix fun <T> T.prepend(other: KList<T>): KList<T> = KList.KCons(this, other)

fun <T:Any> klist(vararg elements: T): KList<T> = block{
    KList.make(*elements)
}

infix fun <T> KList<T>.concat(right: KList<T>): KList<T> = block {
    this.reverse().foldLeft(right){result, e -> e prepend result}
}
