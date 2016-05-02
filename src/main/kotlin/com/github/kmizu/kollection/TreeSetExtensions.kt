package com.github.kmizu.kollection

enum class Color {RED, BLACK}
sealed class Tree<out T> {
    object Empty: Tree<Nothing>()
    class Node<T>(val color: Color, val leftChild: Tree<T>, val element: T, val rightChild: Tree<T>): Tree<T>()
}
