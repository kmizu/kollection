package com.github.kmizu.kollection
import org.jetbrains.spek.api.Spek
import org.junit.Test
import kotlin.test.assertEquals
import com.github.kmizu.kollection.Option.*
import kotlin.test.assertFailsWith

class OptionSpec(): Spek() {
    init {
        given("An Option") {
            on("with a String FOO") {
                val x: Option<String> = Some("FOO")
                assertEquals(x.get(), "FOO")
                assertEquals(x.filter{true}, Some("FOO"))
                assertEquals(x.filter{false}, None)
                assertEquals(x.map{it + "BAR"}, Some("FOOBAR"))
                assertEquals(x.flatMap{Some("BAR")}, Some("BAR"))
            }
            on("with None") {
                val x: Option<Any> = None
                assertFailsWith(IllegalArgumentException::class) {
                    x.get()
                }
                assertEquals(x.filter{true}, None)
                assertEquals(x.map{it.toString()}, None)
                assertEquals(x.flatMap{Some(it.toString())}, None)
            }
        }
    }
}