package com.github.kmizu.kollection
import org.jetbrains.spek.api.Spek
import kotlin.test.assertEquals
import com.github.kmizu.kollection.KOption.*

class KListMapSpec(): Spek() {
    init {
        given("A Map which consists of 1 to 2, 3 to 4, and 5 to 6") {
            val map = KListMap(1 to 2, 3 to 4, 5 to 6)
            on("performing get function") {
                it("returns Some(2) for 1") {
                    assertEquals(Some(2), map[1])
                }
                it("returns Some(4) for 3") {
                    assertEquals(Some(4), map[3])
                }
                it("returns Some(6) for 5") {
                    assertEquals(Some(6), map[5])
                }
                it("returns None otherwise") {
                    assertEquals(None, map[0])
                    assertEquals(None, map[6])
                    assertEquals(None, map[7])
                }
            }
            on("performing plus 6 to 8") {
                val map2 = map + (6 to 8)
                it("returns Some(8) for 6") {
                    assertEquals(Some(8), map2[6])
                }
            }
            on("performing remove 1") {
                val map3 = map.remove(1)
                it("returns None for 1") {
                    assertEquals(None, map3[1])
                }
            }
            on("removing all elements") {
                val empty = map.remove(1).remove(3).remove(5)
                it("returns empty map") {
                    assertEquals(true, empty.isEmpty)
                }
            }
        }
    }
}
