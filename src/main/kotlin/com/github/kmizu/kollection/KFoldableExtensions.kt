package com.github.kmizu.kollection

import com.github.kmizu.kollection.type_classes.KMonoid

fun <T> KFoldable<T>.sum(monoid: KMonoid<T>): T = this.foldLeft(monoid.mzero()){ a, e -> monoid.mplus(a, e)}