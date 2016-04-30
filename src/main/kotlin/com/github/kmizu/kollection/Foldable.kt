package com.github.kmizu.kollection

interface Foldable<out T> {
    fun <U> foldLeft(z: U, function: (U, T) -> U): U
    fun <U> foldRight(z: U, function: (T, U) -> U): U
}