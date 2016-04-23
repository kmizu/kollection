package com.github.kmizu.kollection
import org.jetbrains.spek.api.Spek
import org.junit.Test
import kotlin.test.assertEquals
import com.github.kmizu.kollection.KList.*

class KListSpec(): Spek() {
    init {
        given("A KList") {
            on("which is a KCons") {
                val klist = KList(1, 2, 3, 4, 5)
                it("cons()") {
                    assertEquals(klist, 1 cons (2 cons (3 cons (4 cons (5 cons KNil)))))
                }
                it("reverse()") {
                    assertEquals(KList(5, 4, 3, 2, 1), klist.reverse())
                }
                it("foldLeft()") {
                    assertEquals(15, klist.foldLeft(0){l, r -> l + r})
                }
                it("foldRight()") {
                    assertEquals(-15, klist.foldRight(0){l, r -> r - l})
                }
                it("map()") {
                    assertEquals(KList(2, 3, 4, 5, 6), klist.map {it + 1})
                }
                it("flatMap()") {
                    val list = KList(1, 2, 3)
                    assertEquals(KList(1, 1, 2, 2, 3, 3), list.flatMap{ x -> KList(x, x)})
                }
                it("concat()") {
                    val list = KList(1, 2, 3)
                    assertEquals(KList(1, 2, 3, 4, 5, 1, 2, 3), klist concat list)
                }
                it("isEmpty()") {
                    assertEquals(false, klist.isEmpty())
                }
            }
            on("which is KNil") {
                val knil: KList<Int> = KNil
                it("reverse()") {
                    assertEquals(KNil, knil.reverse())
                }
                it("foldLeft()") {
                    assertEquals(0, knil.foldRight(0){x, y -> x + y})
                }
                it("foldRight()") {
                    assertEquals(0, knil.foldRight(0){x, y -> x + y})
                }
                it("map()") {
                    assertEquals(KNil, knil.map {it + 1})
                }
                it("flatMap()") {
                    val list = KList(1, 2, 3)
                    assertEquals(KNil, knil.flatMap{x -> KList(x, x)})
                }
                it("concat()") {
                    val list = KList(1, 2, 3)
                    assertEquals(KNil, knil concat knil)
                }
                it("isEmpty()") {
                    assertEquals(true, knil.isEmpty())
                }
            }
        }
    }
}
