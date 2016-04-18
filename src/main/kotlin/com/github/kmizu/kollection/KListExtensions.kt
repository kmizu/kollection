package com.github.kmizu.kollection
import com.github.kmizu.kollection.KList.*

infix fun <T:Any> T.prepend(other: KList<T>): KList<T> = KList.KCons(this, other)

fun <T:Any> klistOf(vararg elements: T): KList<T> {
    var result: KList<T> = KNil
    for(e in elements.reversed()) {
        result = e.prepend(result)
    }
    return result
}
