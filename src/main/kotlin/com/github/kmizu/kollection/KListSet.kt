package com.github.kmizu.kollection

class KListSet<T>(initialElements: KList<T> = KList.Nil): KSet<T> {
    private var elements: KList<T> = initialElements

    override fun iterator(): Iterator<T> = object: Iterator<T> {
        private var list: KList<T> = elements.reverse()
        override fun hasNext(): Boolean = list is KList.Cons

        override fun next(): T = run {
            val v = list.hd
            list = list.tl
            v
        }
    }

    override fun get(element: T): Boolean = elements.contains(element)

    override fun plus(element: T): KListSet<T> = run {
        if(elements.exists{it == element}) this else KListSet(element cons elements)
    }

    override fun remove(element: T): KListSet<T> = KListSet<T>(elements.filter {it != element})

    override val isEmpty: Boolean
        get() = elements == KList.Nil
}