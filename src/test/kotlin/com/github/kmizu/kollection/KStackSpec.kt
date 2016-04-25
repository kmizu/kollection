package com.github.kmizu.kollection
import org.jetbrains.spek.api.Spek
import kotlin.test.assertEquals

class KStackSpec(): Spek() {
    init {
        given("A KStack") {
            on("elements are 1, 2, 3, 4, 5") {
                val kstack = KStack<Int>().push(1).push(2).push(3).push(4).push(5)
                it("push()") {
                    assertEquals(KStack<Int>(KList(6, 5, 4, 3, 2, 1)), kstack.push(6))
                }
                it("top()") {
                    assertEquals(5, kstack.top())
                }
                it("pop()") {
                    assertEquals(KStack<Int>(KList(4, 3, 2, 1)), kstack.pop())
                }
                it("isEmpty()") {
                    assertEquals(false, kstack.isEmpty())
                }
            }
        }
    }
}
