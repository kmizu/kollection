package com.github.kmizu.kollection
import org.jetbrains.spek.api.Spek
import kotlin.test.assertEquals
import com.github.kmizu.kollection.*
import com.github.kmizu.kollection.KEither.*
import com.github.kmizu.kollection.type_classes.KMonoid

class KEitherSpec(): Spek({
    given("A Left 1") {
        val a = Left<Int, Int>(1)
        on("performing map") {
            val result = a.map {it * 2}
            it("returns Left(1)") {
                assertEquals(Left<Int, Int>(1), result)
            }
        }
    }
    given("A Right 1") {
        val a = Right<Int, Int>(1)
        on("performing map") {
            val result = a.map {it * 2}
            it("returns Right(2)") {
                assertEquals(Right<Int, Int>(2), result)
            }
        }
    }
})
