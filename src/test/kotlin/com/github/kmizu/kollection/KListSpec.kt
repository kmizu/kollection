package com.github.kmizu.kollection
import org.jetbrains.spek.api.Spek
import kotlin.test.assertEquals
import com.github.kmizu.kollection.KList.*
import com.github.kmizu.kollection.type_classes.KMonoid
import org.jetbrains.spek.api.dsl.*

class KListSpec(): Spek({
    given("A nested KList") {
        val a = KList(KList(1, 2), KList(3, 4), KList(5, 6))
        on("performing sum") {
            val result = a.sum(KMonoid.KLIST())
            it("returns KList(1, 2, 3, 4, 5, 6)") {
                assertEquals(KList(1, 2, 3, 4, 5, 6), result)
            }
        }
    }
    given("A KList of 1, 1, 2, 3, 3, 4, 5") {
        val klist = KList(1, 1, 2, 3, 3, 4, 5)
        on("performing distinct on the list") {
            val result = klist.distinct()
            it("produces KList(1, 2, 3, 4, 5)") {
                assertEquals(KList(1, 2, 3, 4, 5), result)
            }
        }
    }
    given("A KList of 1, 2, 3, 4, 5") {
        val klist = KList(1, 2, 3, 4, 5)
        on("performing cons to each element") {
            it("produces the same KList") {
                val result = 1 cons (2 cons (3 cons (4 cons (5 cons Nil))))
                assertEquals(klist, result)
            }
        }
        on("performing contains") {
            it("returns true on 5"){
                assertEquals(true, klist.contains(5))
            }
            it("returns false on 6") {
                assertEquals(false, klist.contains(6))
            }
        }
        on("performing reverse") {
            val result = klist.reverse()
            it("produces KList(5, 4, 3, 2, 1)") {
                assertEquals(KList(5, 4, 3, 2, 1), result)
            }
        }
        on("calculating sum of the list using foldLeft") {
            val result = klist.foldLeft(0){l, r -> l + r}
            it("returns 15") {
                assertEquals(15, result)
            }
        }
        on("performing map") {
            val result = klist.map{it + 1}
            it("produces KList(2, 3, 4, 5)") {
                assertEquals(KList(2, 3, 4, 5, 6), result)
            }
        }
        on("calculating -sum using foldRight") {
            val result = klist.foldRight(0){l, r -> r - l}
            it("returns -15") {
                assertEquals(-15, result)
            }
        }
        on("performing flatMap") {
            val result = klist.flatMap { x -> KList(x, x) }
            it("produces KList(1, 1, 2, 2, 3, 3, 4, 4, 5, 5)") {
                assertEquals(KList(1, 1, 2, 2, 3, 3, 4, 4, 5, 5), result)
            }
        }
        on("performing zip to itself") {
            val result = klist zip klist
            it("produces KList(1 to 1, 2 to 2, 3 to 3, 4 to 4, 5 to 5)") {
                assertEquals(KList(1 to 1, 2 to 2, 3 to 3, 4 to 4, 5 to 5), result)
            }
        }
        on("performing zip to KList of 1, 2, 3") {
            val result = klist zip KList(1, 2, 3)
            it("produces KList(1 to 1, 2 to 2, 3 to 3)") {
                assertEquals(KList(1 to 1, 2 to 2, 3 to 3), result)
            }
        }
        on("performing length") {
            val result = klist.length
            it("returns 5") {
                assertEquals(5, result)
            }
        }
        on("performing isEmpty") {
            val result = klist.isEmpty
            it("returns false") {
                assertEquals(false, result)
            }
        }
        on("performing concat to KList of 1, 2, 3") {
            val result = klist concat KList(1, 2, 3)
            it("produces KList(1, 2, 3, 4, 5, 1, 2, 3") {
                assertEquals(KList(1, 2, 3, 4, 5, 1, 2, 3), result)
            }
        }
        on("performing unzip") {
            val result = (klist zip KList(2, 3, 4, 5, 6)).unzip()
            it("produces Pair(KList(1, 2, 3, 4, 5), KList(2, 3, 4, 5, 6") {
                assertEquals(Pair(KList(1, 2, 3, 4, 5), KList(2, 3, 4, 5, 6)), result)
            }
        }
        on("performing forAll [1]") {
            val result = klist.forAll { it < 6}
            it("returns true") {
                assertEquals(true, result)
            }
        }
        on("performing forAll [2]") {
            val result = klist.forAll { it % 2 == 0}
            it("returns false") {
                assertEquals(false, result)
            }
        }
        on("performing exists [1]") {
            val result = klist.exists { it % 2 == 0}
            it("returns true") {
                assertEquals(true, result)
            }
        }
        on("performing exists [2]") {
            val result = klist.exists { it > 5}
            it("returns false") {
                assertEquals(false, result)
            }
        }
        on("performing get which arg is 4") {
            val result = klist[4]
            it("returns 5") {
                assertEquals(5, result)
            }
        }
    }
    given("A KList of KList of 1, 2 and KList of 3, 4 and KList of 5, 6") {
        on("performing flatten") {
            val result = KList(KList(1, 2), KList(3, 4), KList(5, 6)).flatten()
            it("produces KList(1, 2, 3, 4, 5, 6") {
                assertEquals(KList(1, 2, 3, 4, 5, 6), result)
            }
        }
    }
    given("A Nil") {
        val knil: KList<Int> = Nil
        on("performing reverse") {
            it("produces Nil") {
                assertEquals(Nil, knil.reverse())
            }
        }
        on("performing foldLeft") {
            it("produces 0") {
                assertEquals(0, knil.foldRight(0){x, y -> x + y})
            }
        }
        on("performing foldRight") {
            it("produces 0") {
                assertEquals(0, knil.foldRight(0){x, y -> x + y})
            }
        }
        on("performing map") {
            it("produces Nil") {
                assertEquals(Nil, knil.map {it + 1})
            }
        }
        on("performing flatMap") {
            it("produces Nil") {
                assertEquals(Nil, knil.flatMap{ x -> KList(x, x)})
            }
        }
        on("performing concat to KList of 1, 2, 3") {
            val list = KList(1, 2, 3)
            it("produces KList(1, 2, 3)") {
                assertEquals(KList(1, 2, 3), knil concat list)
            }
        }
        on("performing isEmpty") {
            it("returns true") {
                assertEquals(true, knil.isEmpty)
            }
        }
        on("performing length") {
            it("returns 0") {
                assertEquals(0, knil.length)
            }
        }
        on("performing zip") {
            it("returns Nil") {
                assertEquals(Nil, knil zip KList(1))
            }
        }
    }
})
