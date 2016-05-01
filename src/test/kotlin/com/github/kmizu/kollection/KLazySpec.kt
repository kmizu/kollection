package com.github.kmizu.kollection
import org.jetbrains.spek.api.Spek
import kotlin.test.assertEquals

class KLazySpec(): Spek() {
    init {
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
                it("also procuces 1 for the seoncd time") {
                    assertEquals(1, klazy.force())
                }
            }
        }
    }
}
