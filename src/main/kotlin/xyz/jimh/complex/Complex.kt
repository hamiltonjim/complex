package xyz.jimh.complex

import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.exp
import kotlin.math.ln
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

data class Complex(val re: Double, val im: Double) {
    constructor(re: Double): this(re, 0.0)
    constructor(re: Int): this(re, 0.0)
    constructor(re: Long): this(re, 0.0)
    constructor(re: Float): this(re, 0.0)

    constructor(re: Int, im: Int): this(re.toDouble(), im.toDouble())
    constructor(re: Int, im: Double): this(re.toDouble(), im)
    constructor(re: Double, im: Int): this(re, im.toDouble())
    constructor(re: Long, im: Long): this(re.toDouble(), im.toDouble())
    constructor(re: Long, im: Double): this(re.toDouble(), im)
    constructor(re: Double, im: Long): this(re, im.toDouble())

    constructor(re: Float, im: Float): this(re.toDouble(), im.toDouble())
    constructor(re: Float, im: Double): this(re.toDouble(), im)
    constructor(re: Double, im: Float): this(re, im.toDouble())

    infix fun close(other: Complex): Boolean {
        return close(other, EPSILON)
    }

    fun close(other: Complex, epsilon: Double): Boolean {
        return abs(re - other.re) < epsilon && abs(im - other.im) < epsilon
    }

    fun round(decimals: Int): Complex {
        return Complex(re.round(decimals), im.round(decimals))
    }

    override fun toString(): String {
        if (re.isNaN() || im.isNaN()) {
            return Double.NaN.toString()
        } else if (re.isInfinite() || im.isInfinite()) {
            return Double.POSITIVE_INFINITY.toString()
        }
        return if (im.close(0.0))
            re.fmt()
        else if (re.close(0.0))
            im.fmt(J_CHAR)
        else if (im < 0.0)
            "${re.fmt()} - ${(-im).fmt(J_CHAR)}"
        else
            "${re.fmt()} + ${im.fmt(J_CHAR)}"
    }

    operator fun unaryPlus() = this

    operator fun unaryMinus(): Complex = Complex(-re, -im)

    operator fun plus(other: Complex): Complex =
        Complex(this.re + other.re, this.im + other.im)

    operator fun minus(other: Complex): Complex =
        Complex(this.re - other.re, this.im - other.im)

    operator fun times(other: Complex): Complex {
        val real = other.re * this.re - other.im * this.im
        val imag = other.re * this.im + other.im * this.re
        return Complex(real, imag)
    }

    operator fun div(other: Complex): Complex {
        val denominator = other.re * other.re + other.im * other.im
        val real = (re * other.re + im * other.im) / denominator
        val imag = (im * other.re - re * other.im) / denominator
        return Complex(real, imag)
    }

    fun sqrt(): Complex {
        if (im == 0.0)
            return re.sqrt()

        val magnitude: Double = sqrt(re * re + im * im)
        val imSign: Double = if (im > 0.0) 1.0 else -1.0
        return Complex(sqrt((magnitude + re) / 2.0), imSign * sqrt((magnitude - re) / 2.0) )
    }

    operator fun plus(other: Double): Complex = Complex(re + other, im)

    operator fun minus(other: Double): Complex = Complex(re - other, im)
    operator fun times(other: Double): Complex =
        Complex(re * other, im * other)

    operator fun div(other: Double): Complex = this / Complex(other)

    infix fun pow(exponent: Complex): Complex {
        val polar = this.polar()
        val realRealPart = polar.first.pow(exponent.re)
        val imagRealPart = exp(polar.second * -exponent.im)
        val exponentOfE = exponent.re * (polar.second + exponent.im * ln(polar.first))
        val eToTheExponent = expITheta(exponentOfE)

        return realRealPart * imagRealPart * eToTheExponent
    }

    infix fun pow(exponent: Double): Complex {
        val polar = this.polar()
        val realRealPart = polar.first.pow(exponent)
        //  imagRealPart = 1.0 [== exp(0)]
        val exponentOfE = exponent * polar.second
        val eToTheExponent = expITheta(exponentOfE)
        return realRealPart * eToTheExponent
    }
    fun polar(): Pair<Double, Double> {
        val rho = sqrt(re * re + im * im)
        val theta = atan2(im, re)
        return rho to theta
    }

    fun isReal(): Boolean = im == 0.0
    fun isImaginary(): Boolean = re == 0.0

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Complex

        return this close other
    }

    override fun hashCode(): Int {
        var result = re.hashCode()
        result = 31 * result + im.hashCode()
        return result
    }

    companion object {
        const val EPSILON = 1.0e-10
        const val J_CHAR = 'j'
        val J = 1.0.j()
        val ZERO = Complex(0.0)
        val ONE = Complex(1)
        // there is only one infinity in the complex plain, and it's a point
        val INFINITY = Complex(Double.POSITIVE_INFINITY)
    }
}
fun Double.round(decimals: Int): Double {
    val multiplier = 10.0.pow(decimals)
    return kotlin.math.round(this * multiplier) / multiplier
}

fun Double.j(): Complex = Complex(0.0, this)

fun Double.sqrt(): Complex {
    return if (this >= 0)
        Complex(sqrt(this), 0.0)
    else
        Complex(0.0, sqrt(-this))
}

operator fun Double.plus(other: Complex): Complex = Complex(this + other.re, other.im)
operator fun Double.minus(other: Complex): Complex = Complex(this - other.re, other.im)

operator fun Double.times(other: Complex): Complex =
    Complex(this * other.re, this * other.im)
operator fun Double.div(other: Complex): Complex = Complex(this) / other

fun Double.close(other: Double): Boolean {
    return abs(other - this) < Complex.EPSILON
}

const val NUL = '\u0000'

fun Double.fmt(char: Char = NUL): String {
    // add epsilon AWAY FROM zero
    val add: Double = if (this > 0) Complex.EPSILON else -Complex.EPSILON
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

// Thank you , Leonhard Euler!
fun expITheta(theta: Double): Complex {
    return Complex(cos(theta), sin(theta))
}
