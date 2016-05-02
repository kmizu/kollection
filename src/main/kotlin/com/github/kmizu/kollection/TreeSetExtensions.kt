package com.github.kmizu.kollection

enum class Color {RED, BLACK}
sealed class Tree<out T> {
    object Empty: Tree<Nothing>()
    class Node<T>(val c: Color, val l: Tree<T>, val e: T, val r: Tree<T>): Tree<T>()
}
