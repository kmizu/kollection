package com.github.kmizu.kollection
import org.jetbrains.spek.api.Spek
import kotlin.test.assertEquals

class KStackSpec(): Spek() {
    init {
        given("A KStack which elements are 1, 2, 3, 4, 5") {
            val kstack = KStack(1, 2, 3, 4, 5)
            on("performing push to each element") {
                val result = KStack<Int>().push(1).push(2).push(3).push(4).push(5)
                it("produce the same stack") {
                    assertEquals(kstack, result)
                }
            }
            on("performing top") {
                val result = kstack.top()
                it("returns 5") {
                    assertEquals(5, result)
                }
            }
            on("performing pop") {
                val result = kstack.pop()
                it("returns KStack(1, 2, 3, 4)") {
                    assertEquals(KStack(1, 2, 3, 4), result)
                }
            }
            on("performing isEmpty") {
                val result = kstack.isEmpty()
                it("returns false") {
                    assertEquals(false, result)
                }
            }
        }
    }
}
