package com.github.kmizu.kollection

fun <T> KStack<T>.push(element: T): KStack<T> = KStack(element cons this.elements)
