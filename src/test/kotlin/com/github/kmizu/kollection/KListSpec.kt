package com.github.kmizu.kollection
import org.jetbrains.spek.api.Spek
import kotlin.test.assertEquals
import com.github.kmizu.kollection.KList.*
import com.github.kmizu.kollection.type_classes.KMonoid

class KListSpec(): Spek() {
    init {
        given("A nested KList") {
            val a = KList(KList(1, 2), KList(3, 4), KList(5, 6))
            on("performing sum") {
                val result = a.sum(KMonoid.KLIST())
                it("returns KList(1, 2, 3, 4, 5, 6)") {
                    assertEquals(KList(1, 2, 3, 4, 5, 6), result)
                }
            }
        }
        given("A KList") {
            on("which is a KCons") {
                val klist = KList(1, 2, 3, 4, 5)
                it("cons()") {
                    assertEquals(klist, 1 cons (2 cons (3 cons (4 cons (5 cons Nil)))))
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
                    assertEquals(false, klist.isEmpty)
                }
                it("length()") {
                    assertEquals(5, klist.length)
                }
                it("zip() -- 1") {
                    assertEquals(KList(1 to 0, 2 to 1, 3 to 2, 4 to 3, 5 to 4), klist zip KList(0, 1, 2, 3, 4))
                }
                it("zip() -- 2") {
                    assertEquals(KList(1 to 0, 2 to 1, 3 to 2, 4 to 3), klist zip KList(0, 1, 2, 3))
                }
                it("unzip()") {
                    assertEquals(Pair(KList(1, 2, 3, 4, 5), KList(2, 3, 4, 5, 6)), (klist zip KList(2, 3, 4, 5, 6)).unzip())
                }
                it("forAll() -- 1") {
                    assertEquals(true, klist.forAll { it < 6 })
                }
                it("forAll() -- 2") {
                    assertEquals(false, klist.forAll { it % 2 == 0 })
                }
                it("exists() -- 1") {
                    assertEquals(true, klist.exists { it % 2 == 0 })
                }
                it("exists() -- 2") {
                    assertEquals(false, klist.exists { it > 5 })
                }
                it("get()") {
                    assertEquals(5, klist[4])
                }
                it("flatten()") {
                    assertEquals(KList(1, 2, 3, 4, 5, 6), KList(KList(1, 2), KList(3, 4), KList(5, 6)).flatten())
                }
            }

            on("which is KNil") {
                val knil: KList<Int> = Nil
                it("reverse()") {
                    assertEquals(Nil, knil.reverse())
                }
                it("foldLeft()") {
                    assertEquals(0, knil.foldRight(0){x, y -> x + y})
                }
                it("foldRight()") {
                    assertEquals(0, knil.foldRight(0){x, y -> x + y})
                }
                it("map()") {
                    assertEquals(Nil, knil.map {it + 1})
                }
                it("flatMap()") {
                    val list = KList(1, 2, 3)
                    assertEquals(Nil, knil.flatMap{ x -> KList(x, x)})
                }
                it("concat()") {
                    val list = KList(1, 2, 3)
                    assertEquals(Nil, knil concat knil)
                }
                it("isEmpty") {
                    assertEquals(true, knil.isEmpty)
                }
                it("length()") {
                    assertEquals(0, knil.length)
                }
                it("zip()") {
                    assertEquals(Nil, knil zip KList(1))
                }
            }
        }
    }
}
