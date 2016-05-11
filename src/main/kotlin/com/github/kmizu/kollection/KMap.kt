package com.github.kmizu.kollection

interface KMap<K, V>: Iterable<Pair<K, V>> {
    operator fun get(key: K): KOption<V>
    operator fun plus(kvPair: Pair<K, V>): KMap<K, V>
    fun remove(key: K): KMap<K, V>
    val isEmpty: Boolean
    val size: Int
}