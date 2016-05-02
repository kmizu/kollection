package com.github.kmizu.kollection
import org.jetbrains.spek.api.Spek
import kotlin.test.assertEquals
import com.github.kmizu.kollection.KOption.*
import kotlin.test.assertFailsWith

class KOptionSpec(): Spek() {
    init {
        given("An Option which is a Some") {
            val x: KOption<String> = Some("FOO")
            on("performing get") {
                val result = x.get()
                it("""returns "FOO"""") {
                    assertEquals("FOO", result)
                }
            }
            on("performing filter") {
                it("""produces Some("FOO")""") {
                    assertEquals(x.filter { true }, Some("FOO"))
                }
                it("""produces None""") {
                    assertEquals(x.filter { false }, None)
                }
            }
            on("performing map") {
                it("""produces Some("FOOBAR")""") {
                    assertEquals(x.map { it + "BAR" }, Some("FOOBAR"))
                }
            }
            on("performing flatMap") {
                it("""produces Some("BAR")""") {
                    assertEquals(x.flatMap { Some("BAR") }, Some("BAR"))
                }
            }
            on("calculating the result of concatenation using foldLeft") {
                it("""returns Some("FOOBAR")""") {
                    assertEquals(x.foldLeft("BAR") { a, r -> r + a }, "FOOBAR")
                }
            }
            on("calculating the result of concatenation using foldRight") {
                it("""returns Some("FOOBAR")""") {
                    assertEquals(x.foldRight("BAR") { l, a -> l + a }, "FOOBAR")
                }
            }
            on("performing orElse") {
                it("""returns Some("FOO")""") {
                    assertEquals(Some("FOO"), x orElse Some("BAR"))
                }
            }
            on("performing getOrElse") {
                it("""returns "FOO"""") {
                    assertEquals("FOO", x getOrElse { "BAR" })
                }
            }
        }
        given("An Option which is None"){
            val x: KOption<String> = None
            on("performing get") {
                it("throws Exception") {
                    assertFailsWith(IllegalArgumentException::class) {
                        x.get()
                    }
                }
            }
            on("performing filter") {
                it("produces None") {
                    assertEquals(None, x.filter { true })
                }
            }
            on("performing map") {
                it("produces None") {
                    assertEquals(None, x.map { it.toString() })
                }
            }
            on("performing flatMap") {
                it("produces None") {
                    assertEquals(None, x.flatMap { Some(it.toString()) })
                }
            }
            on("performing isEmpty") {
                it("returns true") {
                    assertEquals(true, x.isEmpty)
                }
            }
            on("performing foldLeft") {
                it("""returns "EMPTY"""") {
                    assertEquals("EMPTY", x.foldLeft("EMPTY") { x, y -> x + y })
                }
            }
            on("performing foldRight") {
                it("""returns "EMPTY"""") {
                    assertEquals("EMPTY", x.foldRight("EMPTY") { x, y -> x + y })
                }
            }
            on("performing orElse") {
                it("""produces Some("BAR")""") {
                    assertEquals(Some("BAR"), x orElse Some("BAR"))
                }
            }
            on("performing getOrElse") {
                it("""produces "BAR"""") {
                    assertEquals("BAR", x getOrElse { "BAR" })
                }
            }
        }
        given("A value which is null") {
            val value: Int? = null
            on("performing KOption") {
                it("returns None") {
                    assertEquals(None, KOption(value))
                }
            }
        }
        given("A Int value ") {
            val value: Int? = 1
            on("performing KOption") {
                it("returns Some(1)") {
                    assertEquals(Some(1), KOption(value))
                }
            }
        }
    }
}