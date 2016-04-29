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
                it("toKList() should throw StackOverflowError") {
                    assertFailsWith(StackOverflowError::class) {
                        nat.toKList()
                    }
                }
            }
        }
    }
}
