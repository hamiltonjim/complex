package xyz.jimh.complex

import java.math.BigDecimal
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
 * Class representing a complex number ([re] + [im]j), where j == sqrt(-1). Yes, j,
 * not i, because I'm an electrical engineer, and 'i' stands for current (Ohm's Law: V = IR).
 *
 * Note: this class does Not inherit from Number, even though it is a numeric
 * data type. That's because complex numbers cannot really be ordered, as they exist in
 * a plane rather than on a line.
 *
 * This class implements:
 * 1. The four standard operations (as operators) for complex numbers,
 * combining with other complex numbers and with descendents of Number;
 * 1. Exponentiation (with either real or complex exponents), with a special
 * case for square roots;
 * 1. Trigonometric functions;
 * 1. Hyperbolic functions;
 * 1. Other transcendental functions such as e<sup>z</sup> and ln z;
 * 1. Conversion between polar and rectangular coordinates.
 *
 * @constructor takes values [re] for the real part and [im] for the imaginary part.
 * @property re The real part of the complex number
 * @property im The imaginary part of the complex number
 * @author Jim Hamilton
 */
data class Complex(val re: Double, val im: Double = 0.0) {
    /** A complex is a NaN if either part is NaN */
    val isNaN = re.isNaN() || im.isNaN()

    /** A complex is infinite if either part is infinite (and it's not a NaN) */
    val isInfinite = !isNaN && (re.isInfinite() || im.isInfinite())
    /** A complex is zero if BOTH parts are (close to) zero */
    val isZero = abs(re) <= EPSILON / 1000 && abs(im) <= EPSILON / 1000
    /**
     * A complex number is real if its imaginary part is (close to) zero
     */
    val isReal = im.close(0.0, EPSILON / 1000.0)

    /**
     * A complex number is imaginary if its real part is (close to) zero
     */
    val isImaginary = re.close(0.0, EPSILON / 1000.0)

    /**
     * Secondary constructor, where the real and/or imaginary parts are
     * from a type other than Double. We use Number, which is the super
     * interface of all numeric types (other than Complex).
     */
    constructor(re: Number, im: Number = 0.0): this(re.toDouble(), im.toDouble())

    /**
     * Data class for polar coordinates ([rho], [theta])
     */
    data class Polar(val rho: Double, val theta: Double) {
        /**
         * Returns a Complex (rectangular coordinates) from the given number in
         * polar coordinate format.
         * @sample xyz.jimh.complex.Complex.Polar.fromPolarSample
         */
        fun fromPolar(): Complex = Complex(rho * cos(theta), rho * sin(theta))

        companion object {
            /**
             * Sample code to show in KDoc.
             * @see Polar
             */
            fun fromPolarSample(): Complex {
                val polar = Polar(sqrt(2.0), PI / 4.0)
                val complex = polar.fromPolar()
                println("$polar.fromPolar() == $complex") // 1 + j
                return complex
            }
        }
    }

    /**
     * A complex is close to another if both parts of [other] are within [epsilon] of
     * their counterparts in the receiver.
     *
     * Notes:
     * 1. Any two complex numbers that are both infinite are equal to each other,
     * because infinity is a single point on the complex plane.
     *
     * 2. This is the adjective "close" as in "near to,"
     * not the verb "close" as in "shut the door."
     * @see equals
     * @see closeF
     */
    fun close(other: Complex, epsilon: Double = EPSILON): Boolean {
        return when {
            isNaN || other.isNaN -> false
            isInfinite && other.isInfinite -> true
            else -> abs(re - other.re) < epsilon && abs(im - other.im) < epsilon
        }
    }

    /**
     * A complex is close to another if both parts of [other] are within [epsilon] of
     * their counterparts in the receiver.
     * This version takes a Float as [epsilon].
     * @see equals
     * @see close
     */
    fun closeF(other: Complex, epsilon: Float): Boolean {
        return when {
            isNaN || other.isNaN -> false
            isInfinite && other.isInfinite -> true
            else -> abs(re - other.re) < epsilon && abs(im - other.im) < epsilon
        }
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
        return when {
            isNaN -> Double.NaN.toString()
            isInfinite -> Double.POSITIVE_INFINITY.toString()
            im.close(0.0) -> re.fmt()
            re.close(0.0) -> im.fmt(J_CHAR)
            im < 0.0 -> "${re.fmt()} - ${(-im).fmt(J_CHAR)}"
            else -> "${re.fmt()} + ${im.fmt(J_CHAR)}"
        }
    }

    /**
     * If the imaginary part is (close to) zero, return the real part.
     * @throws ArithmeticException if im is not zero.
     */
    fun toDouble(): Double {
        if (isReal) return re
        throw ArithmeticException("Cannot convert Complex to Double; imaginary part is $im")
    }

    /**
     * If the imaginary part is (close to) zero, return the real part as [Float].
     * @throws ArithmeticException if im is not zero.
     */
    fun toFloat(): Float {
        if (isReal) return re.toFloat()
        throw ArithmeticException("Cannot convert Complex to Float; imaginary part is $im")
    }

    /**
     * If the imaginary part is (close to) zero, return the real part as [Long].
     * @throws ArithmeticException if im is not zero.
     */
    fun toLong(): Long {
        if (isReal) return re.toLong()
        throw ArithmeticException("Cannot convert Complex to Long; imaginary part is $im")
    }

    /**
     * If the imaginary part is (close to) zero, return the real part as [Int].
     * @throws ArithmeticException if im is not zero.
     */
    fun toInt(): Int {
        if (isReal) return re.toInt()
        throw ArithmeticException("Cannot convert Complex to Int; imaginary part is $im")
    }

    /**
     * If the imaginary part is (close to) zero, return the real part as [BigDecimal].
     * @throws ArithmeticException if im is not zero.
     */
    fun toBigDecimal(): BigDecimal {
        if (isReal) return re.toBigDecimal()
        throw ArithmeticException("Cannot convert Complex to BigDecimal; imaginary part is $im")
    }

    // Operator overloads

    /**
     * +foo == foo:  just returns (a copy of) itself
     */
    operator fun unaryPlus() = this.copy()

    /**
     * -foo negates both parts of foo
     */
    operator fun unaryMinus() = this.copy(re = -re, im = -im)

    /**
     * Increments the real part, leaves the imaginary part alone. (Yes,
     * complex numbers are "unordered," but this sort of makes sense anyway.)
     * @see dec
     */
    operator fun inc() = this.copy(re = re + 1)

    /**
     * Decrements the real part, leaves the imaginary part alone. (See the
     * documentation for [inc] for the reasoning for this function to
     * exist.)
     * @see inc
     */
    operator fun dec() = this.copy(re = re - 1)

    /**
     * Adds [other] to the receiver, returns the sum.
     */
    operator fun plus(other: Complex): Complex =
        Complex(this.re + other.re, this.im + other.im)

    /**
     * Subtracts [other] from the receiver, returns the difference.
     */
    operator fun minus(other: Complex): Complex =
        Complex(this.re - other.re, this.im - other.im)

    /**
     * Complex multiplication: returns the receiver * [other]
     */
    operator fun times(other: Complex): Complex {
        val real = other.re * this.re - other.im * this.im
        val imag = other.re * this.im + other.im * this.re
        return Complex(real, imag)
    }

    /**
     * Complex division: returns the receiver / [other]
     */
    operator fun div(other: Complex): Complex {
        val denominator = other.re.sqr() + other.im.sqr()
        val real = (re * other.re + im * other.im) / denominator
        val imag = (im * other.re - re * other.im) / denominator
        return Complex(real, imag)
    }

    /**
     * Returns the complex square root of the receiver. When the imaginary part [im] is
     * zero, acts just like [Double.sqrt]
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
     * Expands [other] to [Complex] and adds it to the receiver.
     */
    operator fun plus(other: Number): Complex = Complex(re + other.toDouble(), im)

    /**
     * Expands [other] to [Complex] and subtracts it from the receiver.
     */
    operator fun minus(other: Number): Complex = Complex(re - other.toDouble(), im)

    /**
     * Expands [other] to [Complex] and multiplies it with the receiver.
     */
    operator fun times(other: Number): Complex {
        val real = other.toDouble()
        return Complex(re * real, im * real)
    }

    /**
     * Expands [other] to [Complex] and divides it into the receiver.
     */
    operator fun div(other: Number): Complex = this / Complex(other)

    /**
     * Complex natural logarithm of the receiver (principal value, where imaginary part
     * is always between -π and π) (inverse of [exp]).
     */
    fun ln(): Complex {
        // z = a + bj => Log(z) = Log(re^(jθ)) = ln(r) + jθ
        if (isZero) return INFINITY

        val polar = polar()
        return ln(polar.rho) + polar.theta.j()
    }
    /**
     * e<sup>this</sup> (inverse of [ln])
     */
    fun exp(): Complex {
        // e^(a + bj) == (e^a)(e^bj)
        return exp(re) * expITheta(im)
    }

    /**
     * Complex power [exponent] of the receiver
     */
    infix fun pow(exponent: Complex): Complex {
        // z^w == e^(w log z)
        val wLogZ = exponent * ln()
        return wLogZ.exp()
    }

    /**
     * z<sup>[exponent]</sup>, where [exponent] is a real. Could have been done in
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
     * Returns the magnitude of the receiver (re^2 + im^2)^0.5.
     * @see polar
     */
    fun abs(): Double = sqrt(re.sqr() + im.sqr())

    /**
     * Returns the angle of the receiver, in radians.
     * @see polar
     */
    fun arg(): Double = atan2(im, re)

    /**
     * Returns the square of the receiver.
     */
    private fun sqr(): Complex = this * this

    /**
     * Returns 1 divided by the receiver
     */
    fun reciprocal(): Complex {
        return if (isZero)
            INFINITY
        else
            1.0 / this
    }

    // Trigonometric functions

    /**
     * Returns the circular sine of the receiver.
     */
    fun sin(): Complex {
        return Complex(sin(re) * cosh(im), cos(re) * sinh(im))
    }

    /**
     * Returns the circular cosine of the receiver.
     */
    fun cos(): Complex {
        return Complex(cos(re) * cosh(im), -sin(re) * sinh(im))
    }

    /**
     * Returns the circular tangent of the receiver
     */
    fun tan(): Complex {
        val numerator = Complex(tan(re), tanh(im))
        val denominator = Complex(1, -tan(re) * tanh(im))
        return numerator / denominator
    }

    /**
     * Returns the circular secant of the receiver
     */
    fun sec(): Complex {
        val cos = cos()
        return if (cos.isZero) INFINITY else cos.reciprocal()
    }

    /**
     * Returns the circular cosecant of the receiver
     */
    fun csc(): Complex {
        val sin = sin()
        return if (sin.isZero) INFINITY else sin.reciprocal()
    }

    /**
     * Returns the circular cotangent of the receiver
     */
    fun cot(): Complex {
        val sin = sin()
        return if (sin.isZero) INFINITY else cos() / sin
    }

    // inverse trig functions

    /**
     * Returns the (principal) circular arc sine of the receiver
     */
    fun asin(): Complex {
        // acos(z) == ln(z + sqrt(z^2 - 1)) / i
        return ((1.0 - this.sqr()).sqrt() - this * J).ln() * J
    }

    /**
     * Returns the (principal) circular arc cosine of the receiver
     */
    fun acos(): Complex = PI / 2.0 - asin()

    /**
     * Returns the (principal) circular arc tangent of the receiver
     */
    fun atan(): Complex {
        return ((J - this) / (J + this)).ln() * -J / 2.0
    }

    /**
     * Returns the (principal) circular arc cotangent of the receiver
     */
    fun acot(): Complex {
        return ((this + J) / (this - J)).ln() * -J / 2.0
    }

    /**
     * Returns the (principal) circular arc cosecant of the receiver
     */
    fun acsc(): Complex {
        return if (isZero)
            INFINITY
        else
            (((1.0 - reciprocal().sqr()).sqrt() - J / this)).ln() * J
    }

    /**
     * Returns the (principal) circular arc secant of the receiver
     */
    fun asec(): Complex = PI / 2.0 - acsc()

    // hyperbolic functions

    /**
     * Returns the hyperbolic cosine of the receiver.
     *
     * "The avalanche has already started. It is too late for the pebbles to vote."
     */
    fun cosh(): Complex = (exp() + (-this).exp()) / 2.0

    /**
     * Returns the hyperbolic sine of the receiver
     */
    fun sinh(): Complex = (exp() - (-this).exp()) / 2.0

    /**
     * Returns the hyperbolic tangent of the receiver
     */
    fun tanh(): Complex = ((this * 2.0).exp() - 1.0) / ((this * 2.0).exp() + 1.0)

    /**
     * Returns the hyperbolic cotangent of the receiver
     */
    fun coth(): Complex {
        return if (isZero)
            INFINITY
        else
            ((this * 2.0).exp() + 1.0) / ((this * 2.0).exp() - 1.0)
    }

    /**
     * Returns the hyperbolic secant of the receiver
     */
    fun sech(): Complex = cosh().reciprocal()

    /**
     * Returns the hyperbolic cosecant of the receiver
     */
    fun csch(): Complex {
        return if (isZero)
            INFINITY
        else
            2.0 / (exp() - (-this).exp())
    }

    // inverse functions

    /**
     * Returns the (principal) hyperbolic arc sine of the receiver
     */
    fun asinh(): Complex {
        return (this + (this.sqr() + 1.0).sqrt()).ln()
    }

    /**
     * Returns the (principal) hyperbolic arc cosine of the receiver
     */
    fun acosh(): Complex {
        return (this + (this + 1.0).sqrt() * (this - 1.0).sqrt()).ln()
    }

    /**
     * Returns the (principal) hyperbolic arc tan of the receiver
     */
    fun atanh(): Complex {
        return ((this + 1.0).ln() - (1.0 - this).ln()) / 2.0
    }

    /**
     * Returns the (principal) hyperbolic arc cotangent of the receiver
     */
    fun acoth(): Complex {
        return if (isZero)
            INFINITY
        else
            ((1.0 + reciprocal()).ln() - (1.0 - reciprocal()).ln()) / 2.0
    }

    /**
     * Returns the (principal) hyperbolic arc secant of the receiver
     */
    fun asech(): Complex {
        return if (isZero)
            INFINITY
        else
            (reciprocal() + (reciprocal() + 1.0).sqrt() * (reciprocal() - 1.0).sqrt()).ln()
    }

    /**
     * Returns the (principal) hyperbolic arc cosecant of the receiver
     */
    fun acsch(): Complex {
        return if (isZero)
            INFINITY
        else
            (reciprocal() + (sqr().reciprocal() + 1.0).sqrt()).ln()
    }

    /**
     *  [equals] does not use the default implementation, because there are special
     *  rules for Complex numbers:
     *  1. If either [re] or [im] is NaN, the complex value is NaN
     *  2. If either [re] or [im] is (positive or negative) infinity, the complex
     *  value is [INFINITY] (and all complex infinities are equal to each other).
     *
     *  Note: [equals] will return false if either side is NaN, even if a
     *  Complex is being compared with itself. To test whether two references
     *  have the same referent, use the === operator.
     *  */
    override fun equals(other: Any?): Boolean {
        // NaNs never equal anything, including other NaNs or themselves.
        if (isNaN) return false
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Complex

        // NaNs never equal anything, including other NaNs or themselves.
        if (other.isNaN) return false
        // On the other hand, there is only one infinity in the complex plane.
        if (isInfinite && other.isInfinite) return true

        return re == other.re && im == other.im
    }

    /**
     * Check whether the receiver is "close enough" to [other] (which might be
     * either another [Complex] or a [Number] type). Written in terms of close,
     * so we don't have to deal with approximations.
     *
     * Note: will return false if either side is NaN; will return true if both
     * sides are infinite (there is only a single infinity on the complex plane).
     */
    fun equalTo(other: Any?): Boolean {
        return when {
            isNaN -> return false     // a NaN is never equal to anything, including itself
            other is Complex -> close(other)
            isReal && other is Number -> re.close(other.toDouble())
            else -> false   // this is not a pure real, or other is not Number
        }
    }

    /**
     *
     */
    override fun hashCode(): Int {
        return when {
            isNaN -> Double.NaN.hashCode()
            isInfinite -> Double.POSITIVE_INFINITY.hashCode ()
            else -> 31 * re.hashCode() + im.hashCode()
        }
    }
    companion object {
        /** how close is "close enough"? */
        const val EPSILON = 1.0e-10
        /** a "close enough" when one side was made with Float */
        const val EPSILON_FLOAT = 1.0e-6F

        /** symbol for sqrt(-1) [hey, I'm an electrical engineer, so 'i' is current] */
        const val J_CHAR = 'j'
        /** See extension [Number.j]. */
        val J = 1.j()

        // useful constants
        /**
         * Just zero as a Complex
         */
        val ZERO = Complex(0)
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
         * By analogy to atan2(opp, adj)
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
 * Creates a "pure real" Complex number from the receiver
 */
fun Number.toComplex() = Complex(this.toDouble())

/**
 * Creates a "pure imaginary" number from receiver * sqrt(-1).
 * @sample xyz.jimh.complex.doubleJSample
 * @sample xyz.jimh.complex.bigDecimalJSample
 */
fun Number.j(): Complex = Complex(0.0, this)

/**
 * Sample code to show in KDoc.
 * @see j
 */
fun doubleJSample(): Complex {
    val aDouble = 2.5
    val complex = aDouble.j()
    println("$aDouble.j() == $complex")
    return complex
}

/**
 * Sample code to show in KDoc.
 * @see j
 */
fun bigDecimalJSample(): Complex {
    val aNumber = BigDecimal("10.1378")
    val complex = aNumber.j()
    println("$aNumber.j() == $complex")
    return complex
}

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
 * @see Complex.close
 * @see closeF
 */
fun Double.close(other: Double, epsilon: Double = Complex.EPSILON): Boolean {
    if (other.isNaN()) return false     // NaNs are unordered, therefore never close to anything
    return abs(other - this) < epsilon
}

/**
 * Just like Complex.closeF; returns true if a Float is within epsilon of another.
 * @see Complex.closeF
 * @see close
 */
fun Float.closeF(other: Float, epsilon: Float = Complex.EPSILON_FLOAT): Boolean {
    if (other.isNaN()) return false     // NaNs are unordered, therefore never close to anything
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
 * <br>
 * Thanks to Leonhard Euler for discovering this property: e<sup>jθ</sup> = cos θ + j sin θ
 */
fun expITheta(theta: Double): Complex {
    return Complex(cos(theta), sin(theta))
}

/**
 * Counterpart of Complex.equalTo(Any?): Complex
 * Compare "closeness"
 */
fun Number.equalTo(other: Any?): Boolean {
    return when (other) {
        is Complex -> other.isReal && toDouble().close(other.re)
        is Number -> toDouble().close(other.toDouble())
        else -> false
    }
}
