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
    return Complex(re.round(decimals), im.round(decimals))
}