package xyz.jimh.complex

import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

data class Complex(val re: Double, val im: Double) {
    constructor(re: Double): this(re, 0.0)
    constructor(re: Int, im: Int): this(re.toDouble(), im.toDouble())
    constructor(re: Int, im: Double): this(re.toDouble(), im)
    constructor(re: Double, im: Int): this(re, im.toDouble())
    constructor(re: Long, im: Long): this(re.toDouble(), im.toDouble())
    constructor(re: Long, im: Double): this(re.toDouble(), im)
    constructor(re: Double, im: Long): this(re, im.toDouble())

    constructor(re: Float, im: Float): this(re.toDouble(), im.toDouble())
    constructor(re: Float, im: Double): this(re.toDouble(), im)
    constructor(re: Double, im: Float): this(re, im.toDouble())

    override fun toString(): String {
        return if (im.close(0.0))
            re.fmt()
        else if (re.close(0.0))
            im.fmt(J_CHAR)
        else if (im < 0.0)
            "${re.fmt()} - ${(-im).fmt(J_CHAR)}"
        else
            "${re.fmt()} + ${im.fmt(J_CHAR)}"
    }

    companion object {
        const val J_CHAR = 'j'
    }
}

fun Double.j(): Complex = Complex(0.0, this)

operator fun Complex.unaryPlus() = this

operator fun Complex.unaryMinus(): Complex = Complex(-re, -im)

operator fun Complex.plus(other: Complex): Complex =
    Complex(this.re + other.re, this.im + other.im)

operator fun Complex.minus(other: Complex): Complex =
    Complex(this.re - other.re, this.im - other.im)

operator fun Complex.times(other: Complex): Complex {
   val real = other.re * this.re - other.im * this.im
   val imag = other.re * this.im + other.im * this.re
   return Complex(real, imag)
}

operator fun Complex.div(other: Complex): Complex {
    val denominator = other.re * other.re + other.im * other.im
    val real = (re * other.re + im * other.im) / denominator
    val imag = (im * other.re - re * other.im) / denominator
    return Complex(real, imag)
}

fun Double.sqrt(): Complex {
    return if (this >= 0)
        Complex(sqrt(this), 0.0)
    else
        Complex(0.0, sqrt(-this))
}

fun Complex.sqrt(): Complex {
    if (im == 0.0)
        return re.sqrt()

    val magnitude: Double = sqrt(re * re + im * im)
    val imSign: Double = if (im > 0.0) 1.0 else -1.0
    return Complex(sqrt((magnitude + re) / 2.0), imSign * sqrt((magnitude - re) / 2.0) )
}

operator fun Double.plus(c: Complex): Complex = Complex(this + c.re, c.im)

operator fun Double.minus(other: Complex): Complex = Complex(this - other.re, other.im)

operator fun Double.times(other: Complex): Complex =
    Complex(this * other.re, this * other.im)

operator fun Double.div(other: Complex): Complex = Complex(this) / other

const val EPSILON = 1.0e-6
fun Double.close(other: Double): Boolean {
    return abs(other - this) < EPSILON
}

const val NUL = '\u0000'
fun Double.fmt(char: Char = NUL): String {
    // add epsilon AWAY FROM zero
    val add: Double = if (this > 0) EPSILON else -EPSILON
    val intPart = (this + add).toInt()
    val fraction = this.minus(intPart)

    val sb = StringBuilder()
    val printFraction = !fraction.close(0.0)

    if (intPart != 1 || char == NUL) {
        sb.append(intPart)
    }
    if (intPart == 0 && !printFraction) {
        sb.append('0')
    }
    if (printFraction) {
        sb.append(fraction.toString().substring(1))
    }
    if (char != NUL) {
        sb.append(char)
    }

    return sb.toString()
}

fun Complex.polar(): Pair<Double, Double> {
    val rho = sqrt(re * re + im * im)
    val theta = atan2(im, re)
    return rho to theta
}

// Thank you , Leonhard Euler!
fun expITheta(theta: Double): Complex {
    return Complex(cos(theta), sin(theta))
}

fun Complex.pow(exponent: Complex): Complex {
    val polar = this.polar()
    val realRealPart = Math.pow(polar.first, exponent.re)
    val imagRealPart = Math.exp(polar.second * -exponent.im)
    val exponentOfE = exponent.re * (polar.second + exponent.im * Math.log(polar.first))
    val eulersPart = expITheta(exponentOfE)

    return realRealPart * imagRealPart * eulersPart
}