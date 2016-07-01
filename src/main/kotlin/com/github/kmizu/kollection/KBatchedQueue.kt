package com.github.kmizu.kollection

data class KBatchedQueue<T>(private val front: KList<T>, private val rear: KList<T>) : KQueue<T> {
    constructor() : this(KList.Nil, KList.Nil)
    private fun ensureFront(f: KList<T>, r: KList<T>): KBatchedQueue<T> = when {
        f.isEmpty -> KBatchedQueue(r.reverse(), KList.Nil)
        else -> KBatchedQueue(f, r)
    }

    override val isEmpty: Boolean
        get() = front.isEmpty

    override fun enqueue(newElement: T): KBatchedQueue<T> = ensureFront(front, newElement cons rear)

    override fun dequeue(): KBatchedQueue<T> = when {
        front.isEmpty -> throw IllegalArgumentException("com.github.kmizu.kollection.KBatchedQueue.isEmpty")
        else -> ensureFront(front.tl, rear)
    }

    override fun peek(): T = when {
        front.isEmpty -> throw IllegalArgumentException("com.github.kmizu.kollection.KBatchedQueue.isEmpty")
        else -> front.hd
    }

    override fun toList(): KList<T> = front concat rear.reverse()
}
