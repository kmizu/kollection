package com.github.kmizu.kollection
import org.jetbrains.spek.api.Spek
import kotlin.test.assertEquals
import com.github.kmizu.kollection.KStream.*
import kotlin.test.assertFailsWith

class KStreamSpec(): Spek() {
    init {
        given("KStream") {
            on("handling infinite stream") {
                fun fromInt(from: Int): KStream<Int> = KStreamCons(from, { fromInt(from + 1) })
                val nat = fromInt(0)
                it("map() and take()") {
                    assertEquals(KList(1, 2, 3), nat.map { it + 1 }.take(3).toKList())
                }
                it("map() and drop()") {
                    assertEquals(KList(4, 5, 6), nat.map { it + 1 }.drop(3).take(3).toKList())
                }
                it("takeWhile()") {
                    assertEquals(KList(0, 1, 2, 3, 4, 5), nat.takeWhile{it <  6}.toKList())
                }
                it("dropWhile()") {
                    assertEquals(KList(2, 3, 4, 5), nat.dropWhile {it < 2}.take(4).toKList())
                }
                it("fibonacchi stream") {
                    fun fib(): KStream<Int> = KStreamCons(0, { KStreamCons(1, { fib() zip fib().tl map {it.first + it.second} }) })
                    assertEquals(KList(0, 1, 1, 2, 3, 5, 8), fib().take(7).toKList())
                }

                it("toKList() should throw StackOverflowError") {
                    assertFailsWith(StackOverflowError::class) {
                        nat.toKList()
                    }
                }
            }
        }
    }
}
