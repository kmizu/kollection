package com.github.kmizu.kollection
import org.jetbrains.spek.api.Spek
import org.junit.Test
import kotlin.test.assertEquals
import com.github.kmizu.kollection.KList.*
import kotlin.test.assertFailsWith

class KListSpec(): Spek() {
    init {
        given("A KList") {
            on("which is a KCons") {
                val klist = klist(1, 2, 3, 4, 5)
                it("prepend()") {
                    assertEquals(klist, 1 prepend (2 prepend (3 prepend (4 prepend (5 prepend KNil)))))
                }
                it("reverse()") {
                    assertEquals(klist(5, 4, 3, 2, 1), klist.reverse())
                }
                it("foldLeft()") {
                    assertEquals(15, klist.foldLeft(0){l, r -> l + r})
                }
                it("foldRight()") {
                    assertEquals(-15, klist.foldRight(0){l, r -> r - l})
                }
                it("map()") {
                    assertEquals(klist(2, 3, 4, 5, 6), klist.map {it + 1})
                }
                it("flatMap()") {
                    val list = klist(1, 2, 3)
                    assertEquals(klist(1, 1, 2, 2, 3, 3), list.flatMap{x -> klist(x, x)})
                }
                it("concat") {
                    val list = klist(1, 2, 3)
                    assertEquals(klist(1, 2, 3, 4, 5, 1, 2, 3), klist concat list)
                }
            }
        }
    }
}