package xyz.jimh.complex

import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.cosh
import kotlin.math.exp
import kotlin.math.ln
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sinh
import kotlin.math.sqrt
import kotlin.math.tan
import kotlin.math.tanh

/**
 * Class representing a complex number ([re] + (im)j), where j == sqrt(-1). Yes, j,
 * not i, because I'm an electrical engineer, and 'i' stands for current (Ohm's Law: V = IR).
 *
 * Note: this class does Not inherit from Number, even though it is a numeric
 * data type. That's because Number defines a lot of functions that don't make
 * sense for complex numbers, like toDouble() and toInt().
 *
 * @constructor takes values [re] for the real part and [im] for the imaginary part.
 */
data class Complex(val re: Double, val im: Double = 0.0) {
    /**
     * Secondary constructor, where the real and/or imaginary parts are
     * from a type other than Double. We use Number, which is the super
     * interface of all numeric types (other than Complex).
     */
    constructor(re: Number, im: Number = 0.0): this(re.toDouble(), im.toDouble())

    /** A complex is a NaN if either part is NaN */
    val isNaN = re.isNaN() || im.isNaN()
    /** A complex is infinite if either part is infinite (and it's not a NaN) */
    val isInfinite = !isNaN && (re.isInfinite() || im.isInfinite())
    /** A complex is zero if BOTH parts are (close to) zero */
    val isZero = abs(re) <= EPSILON / 1000 && abs(im) <= EPSILON / 1000

    /**
     * A complex number is real if its imaginary part is zero
     */
    val isReal = im.close(0.0, EPSILON / 1000.0)

    /**
     * A complex number is imaginary if its real part is zero
     */
    val isImaginary = re.close(0.0, EPSILON / 1000.0)

    /**
     * Data class for polar coordinates ([rho], [theta])
     */
    data class Polar(val rho: Double, val theta: Double) {
        /**
         * Returns a Complex (rectangular coordinates) from the given number in
         * polar coordinate format.
         */
        fun fromPolar(): Complex = Complex(rho * cos(theta), rho * sin(theta))
    }

    /**
     * A complex is close to another if both parts of [other] are within [epsilon] of
     * their counterparts in this. Note: this is the adjective "close" as in "near to,"
     * not the verb "close" as in "shut the door."
     * @see equals()
     */
    fun close(other: Complex, epsilon: Double = EPSILON): Boolean {
        return abs(re - other.re) < epsilon && abs(im - other.im) < epsilon
    }

    /**
     * Round to the desired number of [decimals] places. If [decimals] is negative,
     * rounds to the desired power of 10; so round(-2) will round to the
     * nearest hundred.
     */
    fun round(decimals: Int): Complex {
        return Complex(re.round(decimals), im.round(decimals))
    }

    override fun toString(): String {
        if (isNaN) {
            return Double.NaN.toString()
        } else if (isInfinite) {
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

    // Operator overloads

    /**
     * +foo == foo:  just return yourself
     */
    operator fun unaryPlus() = this

    /**
     * -foo negates both parts of foo
     */
    operator fun unaryMinus(): Complex = Complex(-re, -im)

    operator fun plus(other: Complex): Complex =
        Complex(this.re + other.re, this.im + other.im)

    operator fun minus(other: Complex): Complex =
        Complex(this.re - other.re, this.im - other.im)

    /**
     * Complex multiplication: returns this * [other]
     */
    operator fun times(other: Complex): Complex {
        val real = other.re * this.re - other.im * this.im
        val imag = other.re * this.im + other.im * this.re
        return Complex(real, imag)
    }

    /**
     * Complex division: returns this / [other]
     */
    operator fun div(other: Complex): Complex {
        val denominator = other.re * other.re + other.im * other.im
        val real = (re * other.re + im * other.im) / denominator
        val imag = (im * other.re - re * other.im) / denominator
        return Complex(real, imag)
    }

    /**
     * Returns the complex square root of this. When the imaginary part [im] is
     * zero, acts just like [Double].sqrt
     */
    fun sqrt(): Complex {
        if (im == 0.0)
            return re.sqrt()

        val magnitude: Double = sqrt(re * re + im * im)
        val imSign: Double = if (im > 0.0) 1.0 else -1.0
        return Complex(
            sqrt((magnitude + re) / 2.0),
            imSign * sqrt((magnitude - re) / 2.0)
        )
    }
    // operators for Complex op Number
    /**
     * Expands [other] to [Complex] and adds it to this.
     */
    operator fun plus(other: Number): Complex = Complex(re + other.toDouble(), im)
    /**
     * Expands [other] to [Complex] and subtracts it from this.
     */
    operator fun minus(other: Number): Complex = Complex(re - other.toDouble(), im)
    /**
     * Expands [other] to [Complex] and multiplies it with this.
     */
    operator fun times(other: Number): Complex {
        val real = other.toDouble()
        return Complex(re * real, im * real)
    }
    /**
     * Expands [other] to [Complex] and divides it into this.
     */
    operator fun div(other: Number): Complex = this / Complex(other)

    /**
     * Complex natural logarithm of this (principal value, where imaginary part
     * is always between -π and π) (inverse of [exp]).
     */
    fun ln(): Complex {
        // z = a + bj => Log(z) = Log(re^(jθ)) = ln(r) + jθ
        if (isZero) return INFINITY

        val polar = polar()
        return ln(polar.rho) + polar.theta.j()
    }

    /**
     * e to the power of this (inverse of [ln])
     */
    fun exp(): Complex {
        // e^(a + bj) == (e^a)(e^bj)
        return exp(re) * expITheta(im)
    }

    /**
     * Complex power [exponent] of a complex number this
     */
    infix fun pow(exponent: Complex): Complex {
        // z^w == e^(w log z)
        val wLogZ = exponent * ln()
        return wLogZ.exp()
    }

    /**
     * z^[exponent], where [exponent] is a real. Could have been done in
     * terms of z^w, where w's imaginary part is zero, but this is an
     * optimization.
     */
    infix fun pow(exponent: Number): Complex {
        val polar = this.polar()
        val realExponent = exponent.toDouble()
        val realRealPart = polar.rho.pow(realExponent)
        val exponentOfE = realExponent * polar.theta
        val eToTheExponent = expITheta(exponentOfE)
        return realRealPart * eToTheExponent
    }

    /**
     * Rectangular to polar format
     * @see abs
     * @see arg
     * @see Polar
     * @see Companion.fromPolar
     * @see Companion.fromPolar2
     */
    fun polar(): Polar = Polar(abs(), arg())

    /**
     * Returns the magnitude of this.
     * @see polar
     */
    fun abs(): Double = sqrt(re.sqr() + im.sqr())

    /**
     * Returns the angle of this.
     * @see polar
     */
    fun arg(): Double = atan2(im, re)

    /**
     * Returns the square of this.
     */
    private fun sqr(): Complex = this * this

    /**
     * Returns 1 / this
     */
    fun reciprocal(): Complex {
        return if (isZero)
            INFINITY
        else
            1.0 / this
    }

    // Trigonometric functions

    /**
     * Returns the circular sine of this.
     */
    fun sin(): Complex {
        return Complex(sin(re) * cosh(im), cos(re) * sinh(im))
    }

    /**
     * Returns the circular cosine of this.
     */
    fun cos(): Complex {
        return Complex(cos(re) * cosh(im), -sin(re) * sinh(im))
    }

    /**
     * Returns the circular tangent of this
     */
    fun tan(): Complex {
        val numerator = Complex(tan(re), tanh(im))
        val denominator = Complex(1, -tan(re) * tanh(im))
        return numerator / denominator
    }

    /**
     * Returns the circular secant of this
     */
    fun sec(): Complex {
        val cos = cos()
        if (cos.isZero) return INFINITY
        return cos.reciprocal()
    }

    /**
     * Returns the circular cosecant of this
     */
    fun csc(): Complex {
        val sin = sin()
        if (sin.isZero) return INFINITY
        return sin.reciprocal()
    }

    /**
     * Returns the circular cotangent of this
     */
    fun cot(): Complex {
        val sin = sin()
        if (sin.isZero) return INFINITY
        return cos() / sin
    }

    // inverse trig functions

    /**
     * Returns the (principal) arc sine of this
     */
    fun asin(): Complex {
        // acos(z) == ln(z + sqrt(z^2 - 1)) / i
        return ((1.0 - this.sqr()).sqrt() - this * J).ln() * J
    }

    /**
     * Returns the (principal) arc cosine of this
     */
    fun acos(): Complex = PI / 2.0 - asin()

    /**
     * Returns the (principal) arc tangent of this
     */
    fun atan(): Complex {
        return ((J - this) / (J + this)).ln() * -J / 2.0
    }

    /**
     * Returns the (principal) arc cotangent of this
     */
    fun acot(): Complex {
        return ((this + J) / (this - J)).ln() * -J / 2.0
    }

    /**
     * Returns the (principal) arc cosecant of this
     */
    fun acsc(): Complex {
        return if (isZero)
            INFINITY
        else
            (((1.0 - reciprocal().sqr()).sqrt() - J / this)).ln() * J
    }

    /**
     * Returns the (principal) arc secant of this
     */
    fun asec(): Complex = PI / 2.0 - acsc()

    // hyperbolic functions

    /**
     * Returns the hyperbolic cosine of this
     */
    fun cosh(): Complex = (exp() + (-this).exp()) / 2.0

    /**
     * Returns the hyperbolic sine of this
     */
    fun sinh(): Complex = (exp() - (-this).exp()) / 2.0

    /**
     * Returns the hyperbolic tangent of this
     */
    fun tanh(): Complex = ((this * 2.0).exp() - 1.0) / ((this * 2.0).exp() + 1.0)

    /**
     * Returns the hyperbolic cotangent of this
     */
    fun coth(): Complex {
        return if (isZero)
            INFINITY
        else
            ((this * 2.0).exp() + 1.0) / ((this * 2.0).exp() - 1.0)
    }

    /**
     * Returns the hyperbolic secant of this
     */
    fun sech(): Complex = cosh().reciprocal()

    /**
     * Returns the hyperbolic cosecant of this
     */
    fun csch(): Complex {
        return if (isZero)
            INFINITY
        else
            2.0 / (exp() - (-this).exp())
    }

    // inverse functions

    /**
     * Returns the (principal) arc sinh of this
     */
    fun asinh(): Complex {
        return (this + (this.sqr() + 1.0).sqrt()).ln()
    }

    /**
     * Returns the (principal) arc cosh of this
     */
    fun acosh(): Complex {
        return (this + (this + 1.0).sqrt() * (this - 1.0).sqrt()).ln()
    }

    /**
     * Returns the (principal) arc tanh of this
     */
    fun atanh(): Complex {
        return ((this + 1.0).ln() - (1.0 - this).ln()) / 2.0
    }

    /**
     * Returns the (principal) arc coth of this
     */
    fun acoth(): Complex {
        return if (isZero)
            INFINITY
        else
            ((1.0 + reciprocal()).ln() - (1.0 - reciprocal()).ln()) / 2.0
    }

    /**
     * Returns the (principal) arc sech of this
     */
    fun asech(): Complex {
        return if (isZero)
            INFINITY
        else
            (reciprocal() + (reciprocal() + 1.0).sqrt() * (reciprocal() - 1.0).sqrt()).ln()
    }

    /**
     * Returns the (principal) arc csch of this
     */
    fun acsch(): Complex {
        return if (isZero)
            INFINITY
        else
            (reciprocal() + (sqr().reciprocal() + 1.0).sqrt()).ln()
    }

    /**
     *  equals in terms of [close], so we don't have to deal with approximations.
     *  Note: will return false if either side is NaN; will return true if both
     *  sides are infinite (there is only a single infinity on the complex plane).
     *  */
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other is Number) {
            return isReal && re.close(other.toDouble())
        }
        if (javaClass != other?.javaClass) return false

        other as Complex

        // NaNs never equal anything, including other NaNs or themselves.
        if (isNaN || other.isNaN) return false
        // On the other hand, there is only one infinity in the complex plane.
        if (isInfinite && other.isInfinite) return true

        return this.close(other)
    }

    /**
     * A complex hashCode is just the hash of each part.
     */
    override fun hashCode(): Int = 31 * re.hashCode() + im.hashCode()

    companion object {
        /** how close is "close enough"? */
        const val EPSILON = 1.0e-10
        /** a "close enough" when one side was made with Float */
        const val EPSILON_FLOAT = 1.0e-6
        /** symbol for sqrt(-1) [hey, I'm an electrical engineer, so 'i' is current] */
        const val J_CHAR = 'j'
        /** See extension [Double.j] below. */
        val J = 1.0.j()
        // useful constants
        /**
         * Just zero as a Complex
         */
        val ZERO = Complex(0.0)

        /**
         * 1 as a Complex
         */
        val ONE = Complex(1)

        /**
         * π * sqrt(-1)
         */
        val PI_J = PI.j()

        /**
         * π as a Complex
         */
        val PI_C = Complex(PI)
        /**
         * There is only one infinity in the complex plain, and it's a point. Among others,
         * see [here](https://math.stackexchange.com/questions/420557/whats-the-difference-between-complex-infinity-and-undefined#:~:text=%22Undefined%22%20is%20something%20that%20one,is%20itself%20a%20mathematical%20object.)
         */
        val INFINITY = Complex(Double.POSITIVE_INFINITY)

        /**
         * Constructs a Complex from polar coordinates; the values in [polarCoordinates] are the
         * radius and the angle (in radians), respectively. This is the exact format returned by
         * the member function [polar].
         * @see Polar
         * @see polar
         * @see fromPolar2
         */
        fun fromPolar(polarCoordinates: Polar): Complex = polarCoordinates.fromPolar()

        /**
         * Constructs a Complex from polar coordinates, [rho] and [theta] (in radians).
         * @see polar
         * @see fromPolar
         */
        fun fromPolar2(rho: Double, theta: Double): Complex = Polar(rho, theta).fromPolar()
    }
}

/**
 * Round to [decimals] places. If [decimals] is negative,
 * rounds to the desired power of 10; so round(-2) will round to the
 * nearest hundred.
 */
fun Double.round(decimals: Int): Double {
    val multiplier = 10.0.pow(decimals)
    return kotlin.math.round(this * multiplier) / multiplier
}

/**
 * Returns the square of the receiver.
 */
fun Double.sqr(): Double = this * this

/**
 * Creates a "pure imaginary" number from receiver * sqrt(-1).
 */
fun Double.j(): Complex = Complex(0.0, this)

/**
 * Takes the square root, as a Complex number. Works just fine with negative
 * real numbers. Does not interfere with Double.sqrt(x: Double), since the
 * signature is different.
 */
fun Double.sqrt(): Complex {
    return if (this >= 0)
        Complex(sqrt(this), 0.0)
    else
        Complex(0.0, sqrt(-this))
}

//  Operator overloads for Double op Complex.
/**
 * Expands the receiver to [Complex] and adds [other] to it.
 */
operator fun Number.plus(other: Complex): Complex = Complex(toDouble() + other.re, other.im)

/**
 * Expands the receiver to [Complex] and subtracts [other] from it.
 */
operator fun Number.minus(other: Complex): Complex = Complex(toDouble() - other.re, other.im)

/**
 * Expands the receiver to [Complex] and multiplies it by [other]
 */
operator fun Number.times(other: Complex): Complex {
    val real = toDouble()
    return Complex(real * other.re, real * other.im)
}

/**
 * Expands the receiver to [Complex] and divides it by [other]
 */
operator fun Number.div(other: Complex): Complex = Complex(this) / other

/**
 * Just like Complex.close; returns true if a Double is within epsilon of another.
 * @see [Complex.close]
 */
fun Double.close(other: Double, epsilon: Double = Complex.EPSILON): Boolean {
    return abs(other - this) < epsilon
}

const val NUL = '\u0000'
/**
 * Format a double, optionally with another [char] (like i or j). If [char] is
 * the default [NUL] (ASCII 0), it is not part of the result string.
 */
fun Double.fmt(char: Char = NUL): String {
    val negative = this < 0.0
    val positive = abs(this)
    val intPart = (positive + Complex.EPSILON).toInt()
    val fraction = positive - intPart

    val sb = StringBuilder()
    val printFraction = !fraction.close(0.0)

    if (negative) sb.append('-')

    if (intPart != 1 || char == NUL || printFraction) {
        sb.append(intPart)
    }

    if (printFraction) {
        sb.append(fraction.toString().substring(1))
    }
    if (char != NUL) {
        sb.append(char)
    }

    return sb.toString()
}

/**
 * Turns the (radian) angle [theta] into the complex number at that angle, with unit magnitude.
 * Thanks to Leonhard Euler for discovering this property: e^(jθ) = cos θ + j sin θ
 */
fun expITheta(theta: Double): Complex {
    return Complex(cos(theta), sin(theta))
}
