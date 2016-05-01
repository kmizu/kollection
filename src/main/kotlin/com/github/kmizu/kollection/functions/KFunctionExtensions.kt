package com.github.kmizu.kollection.functions

fun <A, B, R> ((A, B) -> R).curried(): (A) -> (B) -> R = {a -> {b -> this(a, b)}}
