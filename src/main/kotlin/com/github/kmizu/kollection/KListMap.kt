package com.github.kmizu.kollection

class KListMap<K, V>(initialElements: KList<Pair<K, V>> = KList.Nil): KMap<K, V> {
    private var elements: KList<Pair<K, V>> = initialElements

    override fun iterator(): Iterator<Pair<K, V>> = object: Iterator<Pair<K, V>> {
        private var list: KList<Pair<K, V>> = elements.reverse()
        override fun hasNext(): Boolean = list is KList.Cons

        override fun next(): Pair<K, V> = run {
            val v = list.hd
            list = list.tl
            v
        }
    }

    override fun remove(key: K): KListMap<K, V> = run {
        KListMap(elements.filter {it.first != key})
    }

    override fun get(key: K): KOption<V> = run {
        elements.find {it.first == key}.map{it.second}
    }

    override fun plus(kvPair: Pair<K, V>): KListMap<K, V> = run {
        if(elements.exists {it.first == kvPair.first})
            KListMap(elements.map{if(it.first == kvPair.first) kvPair else it})
        else
            KListMap(kvPair cons elements)
    }

    override val isEmpty: Boolean
        get() = elements == KList.Nil

    override val size: Int
        get() = elements.length
}