package com.github.kmizu.kollection

fun <T> KStack<T>.push(element: T): KStack<T> = KStack(element cons this.elements)
fun <T> KStack(vararg elements: T): KStack<T> = run {
    var stack: KStack<T> = KStack()
    for(e in elements) {
        stack = stack.push(e)
    }
    stack
}
