package com.github.kmizu.kollection
import org.jetbrains.spek.api.Spek
import kotlin.test.assertEquals
import com.github.kmizu.kollection.KStream.*
import kotlin.test.assertFailsWith

class KStreamSpec(): Spek() {
    init {
        given("KStream") {
            on("an infinite stream describing natural numbers") {
                fun fromInt(from: Int): KStream<Int> = from cons { fromInt(from + 1) }
                val nat = fromInt(0)
                it("map{x -> x + 1}.take(3) produces KStream of (1, 2, 3)") {
                    assertEquals(KList(1, 2, 3), nat.map { it + 1 }.take(3).toKList())
                }
                it("map{x -> x + 1}.drop(3) produces KStream of (4, 5, 6)") {
                    assertEquals(KList(4, 5, 6), nat.map { it + 1 }.drop(3).take(3).toKList())
                }
                it("takeWhile{x -> x < 6} produces KStream of (0, 1, 2, 3, 4, 5)") {
                    assertEquals(KList(0, 1, 2, 3, 4, 5), nat.takeWhile{it <  6}.toKList())
                }
                it("dropWhile{x -> x < 2} produces KStream of (2, 3, 4, 5, ...)") {
                    assertEquals(KList(2, 3, 4, 5), nat.dropWhile {it < 2}.take(4).toKList())
                }
                it("KStream describing fibonacci can be defined") {
                    fun fib(): KStream<Int> = 0 cons { 1 cons { fib() zip fib().tl map {it.first + it.second} } }
                    assertEquals(KList(0, 1, 1, 2, 3, 5, 8), fib().take(7).toKList())
                }
                it("KStream describing counter") {
                    var i = 0
                    fun countUp(): Int = run{ i += 1; i }
                    assertEquals(KList(1, 2, 3), KStream.forever{ countUp() }.take(3).toKList())
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
