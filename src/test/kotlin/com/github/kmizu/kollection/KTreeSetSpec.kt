package com.github.kmizu.kollection
import org.jetbrains.spek.api.Spek
import kotlin.test.assertEquals
import com.github.kmizu.kollection.KStream.*
import kotlin.test.assertFailsWith

class KTreeSetSpec(): Spek() {
    init {
        given("A Set which consists of 1, 2, 3, 4, and 5") {
            val set = KTreeSet(KTreeSet.INT_COMPARATOR, 1, 2, 3, 4, 5)
            on("performing get function") {
                it("returns true for 1") {
                    assertEquals(true, set[1])
                }
                it("returns true for 2") {
                    assertEquals(true, set[2])
                }
                it("returns true for 3") {
                    assertEquals(true, set[3])
                }
                it("returns true for 4") {
                    assertEquals(true, set[4])
                }
                it("returns true for 5") {
                    assertEquals(true, set[5])
                }
                it("returns false for 0") {
                    assertEquals(false, set[0])
                }
                it("returns false 6") {
                    assertEquals(false, set[6])
                }
            }
            on("performing plus 6") {
                val set2 = set + 6
                it("returns true for 6") {
                    assertEquals(true, set2[6])
                }
            }
        }
    }
}
