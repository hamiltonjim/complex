/*
 * Copyright © 2025 Jim Hamilton.
 * All rights reserved.
 */

package xyz.jimh.complex

import kotlin.math.pow
import kotlin.math.round

fun Double.round(decimals: Int): Double {
    val multiplier = 10.0.pow(decimals)
    return round(this * multiplier) / multiplier
}

fun Complex.round(decimals: Int): Complex {
    val multiplier = 10.0.pow(decimals)
    return Complex(
        round(re * multiplier) / multiplier,
        round(im * multiplier) / multiplier
    )
}