package com.github.kmizu.kollection
import org.jetbrains.spek.api.Spek
import kotlin.test.assertEquals
import com.github.kmizu.kollection.KStream.*
import org.jetbrains.spek.api.dsl.*
import kotlin.test.assertFailsWith

class KStreamSpec(): Spek({
    given("Args which consists of 1, 2, 3, and 4") {
        on("performing KStream function") {
            it("produces (1 cons { 2 cons { 3 cons { 4 cons {KStreamNil}}}})") {
                assertEquals(1 cons { 2 cons { 3 cons { 4 cons { Nil }}}}, KStream(1, 2, 3, 4))
            }
        }
    }
    given("A KStream describing fibonacci number") {
        fun fib(): KStream<Int> = 0 cons { 1 cons { fib() zip fib().tl map {it.first + it.second} } }
        on("performing take(5)") {
            it("produces KStream(0, 1, 1, 2, 3, 5, 8)") {
                assertEquals(KStream(0, 1, 1, 2, 3, 5, 8), fib().take(7))
            }
        }
    }
    given("A KStream describing counter") {
        var i = 0
        fun countUp(): Int = run { i += 1; i }
        val counter: KStream<Int> = KStream.forever { countUp() }
        on("performing take") {
            val result = counter.take(3)
            it("produces KStream(1, 2, 3)") {
                assertEquals(KStream(1, 2, 3), result)
            }
        }
    }
    given("A KStream describing an infinite stream of natural numbers") {
        val nat = KStream.from(0)
        on("performing take and toKList") {
            val result = nat.take(3).toKList()
            it("produces KList(0, 1, 2)") {
                assertEquals(KList(0, 1, 2), result)
            }
        }
        on("performing map") {
            val result = nat.map{x -> x + 1}.take(3)
            it("produces KStream(1, 2, 3, ...)") {
                assertEquals(KStream(1, 2, 3), result)
            }
        }
        on("performing map and drop") {
            val result = nat.map{x -> x + 1}.drop(3).take(3)
            it("produces KStream(4, 5, 6, ...)") {
                assertEquals(KStream(4, 5, 6), result)
            }
        }
        on("performing takeWhile") {
            val result = nat.takeWhile {it < 6}
            it("produces KStream(0, 1, 2, 3, 4, 5)") {
                assertEquals(KStream(0, 1, 2, 3, 4, 5), result)
            }
        }
        on("performing dropWhile") {
            val result = nat.dropWhile {it < 2}.take(4)
            it("produces KStream(2, 3, 4, 5, ...)") {
                assertEquals(KStream(2, 3, 4, 5), result)
            }
        }
        on("calculating sum of first 5 elements using foldLeft") {
            it("returns 10") {
                assertEquals(10, nat.take(5).foldLeft(0){a, b -> a + b})
            }
        }
        on("performing toKList") {
            it("throws StackOverFlowError") {
                assertFailsWith(StackOverflowError::class) {
                    nat.toKList()
                }
            }
        }
    }
    given("Two KStream") {
        val a = KStream.from(1)
        val b = KStream.from(4)
        on("performing flatMap") {
            val result = a.flatMap{x -> b.flatMap {y -> KStream(x, y) }}.take(6)
            it("produces KStream(1, 4, 1, 5, 1, 6, ...)") {
                assertEquals(KStream(1, 4, 1, 5, 1, 6), result)
            }
        }
    }
})
