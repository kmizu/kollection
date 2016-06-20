package com.github.kmizu.kollection
import org.jetbrains.spek.api.Spek
import kotlin.test.assertEquals

class KLazySpec(): Spek({
    given("A KLazy instance which evaluates to 1") {
        val klazy = KLazy{ 1 }
        on("not performing force") {
            it("!isForced") {
                assertEquals(false, klazy.isForced)
            }
        }
        on("performing force twice") {
            it("produces 1 for the first time") {
                assertEquals(1, klazy.force())
            }
            it("also produces 1 for the second time") {
                assertEquals(1, klazy.force())
            }
        }
        on("performing map") {
            val result = klazy.map{it + 2}
            it("!isForced") {
                assertEquals(false, result.isForced)
            }
            it("produces 3") {
                assertEquals(3, result.force())
            }
        }
        on("performing flatMap") {
            val result = klazy.flatMap{x -> KLazy{x + x}}
            it("!isForced") {
                assertEquals(false, result.isForced)
            }
            it("produces 2") {
                assertEquals(2, result.force())
            }
        }
    }
})
