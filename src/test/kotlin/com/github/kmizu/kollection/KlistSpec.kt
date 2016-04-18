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
                val klist = klistOf(1, 2, 3, 4, 5)
                it("prepend()") {
                    assertEquals(klist, 1 prepend (2 prepend (3 prepend (4 prepend (5 prepend KNil)))))
                }
                it("reverse()") {
                    assertEquals(klist.reverse(), klistOf(5, 4, 3, 2, 1))
                }
            }
        }
    }
}