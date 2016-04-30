package com.github.kmizu.kollection
import org.jetbrains.spek.api.Spek
import kotlin.test.assertEquals
import com.github.kmizu.kollection.KOption.*
import kotlin.test.assertFailsWith

class KOptionSpec(): Spek() {
    init {
        given("An Option") {
            on("which is a Some(\"FOO\")") {
                val x: KOption<String> = Some("FOO")
                it("get()") {
                    assertEquals(x.get(), "FOO")
                }
                it("filter()") {
                    assertEquals(x.filter{true}, Some("FOO"))
                    assertEquals(x.filter{false}, None)
                }
                it("map()"){
                    assertEquals(x.map{it + "BAR"}, Some("FOOBAR"))
                }
                it("flatMap()"){
                    assertEquals(x.flatMap{Some("BAR")}, Some("BAR"))
                }
                it("foldLeft()") {
                    assertEquals(x.foldLeft("BAR"){a, r -> r + a}, "FOOBAR")
                }
                it("foldRight()") {
                    assertEquals(x.foldRight("BAR"){l, a -> l + a}, "FOOBAR")
                }
                it("orElse()") {
                    assertEquals(Some("FOO"), x orElse Some("BAR"))
                }
            }
            on("which is None") {
                val x: KOption<String> = None
                it("get()") {
                    assertFailsWith(IllegalArgumentException::class) {
                        x.get()
                    }
                }
                it("filter()") {
                    assertEquals(x.filter{true}, None)
                }
                it("map()") {
                    assertEquals(x.map{it.toString()}, None)
                }
                it("flatMap()"){
                    assertEquals(x.flatMap{Some(it.toString())}, None)
                }
                it("isEmpty()") {
                    assertEquals(x.isEmpty, true)
                }
                it("foldLeft()") {
                    assertEquals(x.foldLeft("EMPTY"){x, y -> x + y}, "EMPTY")
                }
                it("foldRight") {
                    assertEquals(x.foldRight("EMPTY"){x, y -> x + y}, "EMPTY")
                }
                it("orElse()") {
                    assertEquals(Some("BAR"), x orElse Some("BAR"))
                }
            }
        }
    }
}