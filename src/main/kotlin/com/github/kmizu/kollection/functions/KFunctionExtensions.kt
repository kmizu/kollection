package com.github.kmizu.kollection.functions

fun <A, B, R> ((A, B) -> R).curried(): (A) -> (B) -> R = {a -> {b -> this(a, b)}}
fun <A, B, C, R> ((A, B, C) -> R).curried(): (A) -> (B) -> (C) -> R = {a -> {b -> {c -> this(a, b, c)}}}
fun <A, B, C, D, R> ((A, B, C, D) -> R).curried(): (A) -> (B) -> (C) -> (D) -> R = {a -> {b -> {c -> {d -> this(a, b, c, d)}}}}
fun <A, B, C, D, E, R> ((A, B, C, D, E) -> R).curried(): (A) -> (B) -> (C) -> (D) -> (E) -> R = {a -> {b -> {c -> {d -> {e -> this(a, b, c, d, e)}}}}}
fun <A, B, C, D, E, F, R> ((A, B, C, D, E, F) -> R).curried(): (A) -> (B) -> (C) -> (D) -> (E) -> (F) -> R = {a -> {b -> {c -> {d -> {e -> {f -> this(a, b, c, d, e, f)}}}}}}
fun <A, B, C, D, E, F, G, R> ((A, B, C, D, E, F, G) -> R).curried(): (A) -> (B) -> (C) -> (D) -> (E) -> (F) ->(G) -> R = {a -> {b -> {c -> {d -> {e -> {f -> {g -> this(a, b, c, d, e, f, g)}}}}}}}
fun <A, B, C, D, E, F, G, H, R> ((A, B, C, D, E, F, G, H) -> R).curried(): (A) -> (B) -> (C) -> (D) -> (E) -> (F) -> (G) ->(H) -> R = {a -> {b -> {c -> {d -> {e -> {f -> {g -> {h -> this(a, b, c, d, e, f, g, h)}}}}}}}}
fun <A, B, C, D, E, F, G, H, I, R> ((A, B, C, D, E, F, G, H, I) -> R).curried(): (A) -> (B) -> (C) -> (D) -> (E) -> (F) -> (G) -> (H) -> (I) -> R = {a -> {b -> {c -> {d -> {e -> {f -> {g -> {h -> {i -> this(a, b, c, d, e, f, g, h, i)}}}}}}}}}
fun <A, B, C, D, E, F, G, H, I, J, R> ((A, B, C, D, E, F, G, H, I, J) -> R).curried(): (A) -> (B) -> (C) -> (D) -> (E) -> (F) -> (G) -> (H) -> (I) -> (J) -> R = {a -> {b -> {c -> {d -> {e -> {f -> {g -> {h -> {i -> {j -> this(a, b, c, d, e, f, g, h, i, j)}}}}}}}}}}
