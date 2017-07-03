package com.github.kmizu.kollection
import org.jetbrains.spek.api.Spek
import com.github.kmizu.kollection.*
import com.github.kmizu.kollection.KEither.*
import com.github.kmizu.kollection.type_classes.KMonoid
import kotlin.test.assertEquals
import org.jetbrains.spek.api.dsl.*

object KBatchedQueueSpek: Spek({
    given("A BatchedQueue consists of 1, 2, 3, 4, 5") {
        val q = KBatchedQueue(1, 2, 3, 4, 5)
        on("performing enqueue 6") {
            val result = q enqueue 6
            it("returns KBatchedQueue(1, 2, 3, 4, 5, 6)") {
                assertEquals(KBatchedQueue(1, 2, 3, 4, 5, 6).toList(), result.toList())
            }
        }
        on("performing dequeue") {
            val result = q.dequeue()
            it("returns KBatchedQueue(2, 3, 4, 5)") {
                assertEquals(KBatchedQueue(2, 3, 4, 5).toList(), result.toList())
            }
        }
    }
})
