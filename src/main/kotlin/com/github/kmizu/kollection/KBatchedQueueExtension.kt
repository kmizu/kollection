package com.github.kmizu.kollection

fun <T> KBatchedQueue(vararg elements: T): KBatchedQueue<T> = run {
    var queue: KBatchedQueue<T> = KBatchedQueue<T>()
    for(e in elements) {
        queue = queue.enqueue(e)
    }
    queue
}
