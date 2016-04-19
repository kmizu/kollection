package com.github.kmizu.kollection
import com.github.kmizu.kollection.KList.*
import com.github.kmizu.kollection.kontrol.block

infix fun <T:Any> T.prepend(other: KList<T>): KList<T> = KList.KCons(this, other)

fun <T:Any> klist(vararg elements: T): KList<T> = block{
    KList.make(*elements)
}
