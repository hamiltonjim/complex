package xyz.jimh.complex

import kotlin.math.E
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.cosh
import kotlin.math.exp
import kotlin.math.sin
import kotlin.math.sinh
import kotlin.math.sqrt
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.function.Executable
import xyz.jimh.complex.Complex.Companion.EPSILON_FLOAT
import xyz.jimh.complex.Complex.Companion.PI_J

class ComplexTest {

    @Test
    fun testClose() {
        assertTrue(Complex(1, 1).close(Complex(1.0000000000001, 1.0000000000001)))
    }

    @Test
    fun `test constructors`() {
        val real = Complex(1, 0)
        assertAll(
            Executable { assertEquals(real, Complex(1.0)) },
            Executable { assertEquals(real, Complex(1)) },
            Executable { assertEquals(real, Complex(1L)) },
            Executable { assertEquals(real, Complex(1.0F)) },
        )

        val complex = Complex(1.0, 1.0)
        assertAll(
            Executable { assertEquals(complex, Complex(1.0, 1)) },
            Executable { assertEquals(complex, Complex(1.0, 1L)) },
            Executable { assertEquals(complex, Complex(1.0, 1.0F)) },
            Executable { assertEquals(complex, Complex(1, 1.0)) },
            Executable { assertEquals(complex, Complex(1L, 1.0)) },
            Executable { assertEquals(complex, Complex(1.0F, 1.0)) },
            Executable { assertEquals(complex, Complex(1, 1)) },
            Executable { assertEquals(complex, Complex(1L, 1L)) },
            Executable { assertEquals(complex, Complex(1.0F, 1.0F)) },
        )
    }

    @Test
    fun `test exp`() {
        assertAll(
            Executable { assertEquals(Complex(-1.0), PI_J.exp()) },
            Executable { assertEquals(Complex(0.0, 1.0), (PI_J / 2.0).exp()) },
            Executable { assertEquals(Complex(0.0, -1.0), (PI_J * 1.5).exp()) },
        )
    }

    @Test
    fun `test abs and arg`() {
        assertAll(
            Executable { assertEquals(PI, PI_J.abs(), Complex.EPSILON) },
            Executable { assertEquals(sqrt(2.0), Complex(1, 1).abs(), Complex.EPSILON) },
            Executable { assertEquals(5.0, Complex(4, 3).abs(), Complex.EPSILON) },
            Executable { assertEquals(PI / 4, Complex(2, 2).arg(), Complex.EPSILON) },
        )
    }

    @Test
    fun round() {
        assertEquals(Complex(3, 3), Complex(PI, E).round(0))
        assertEquals(Complex(3.1, 2.7), Complex(PI, E).round(1))
        assertEquals(Complex(3.14, 2.72), Complex(PI, E).round(2))
        assertEquals(Complex(3.142, 2.718), Complex(PI, E).round(3))
    }

    @Test
    operator fun unaryPlus() {
        val test = Complex(1.23456, 7.89012)
        assertEquals(test, +test)
    }

    @Test
    operator fun unaryMinus() {
        val test = Complex(-1.23456, 7.89012)
        val negative = Complex(1.23456, -7.89012)
        assertEquals(negative, -test)
    }

    @Test
    fun plus() {
        val val1 = Complex(1, 2)
        val val2 = Complex(3, 4)
        val sum = Complex(4, 6)
        val sum2 = Complex(4, 7)
        assertAll(
            Executable { assertEquals(sum, val1 + val2) },
            Executable { assertEquals(sum2, val2 + val1 + Complex.J) },
        )
    }

    @Test
    fun minus() {
        val minuend = Complex(4, 7)
        val subtrahend = Complex(3, 5)
        val difference = Complex(1, 2)
        assertAll(
            Executable { assertEquals(difference, minuend - subtrahend) },
            Executable { assertEquals(Complex.ZERO, minuend - minuend) },
        )
    }

    @Test
    fun times() {
        val factor1 = Complex(1, 2)
        val factor2 = Complex(3, 4)
        val product = Complex(-5, 10)
        assertAll(
            Executable { assertEquals(product, factor1 * factor2) },
            Executable { assertEquals(factor2, factor2 * Complex(1)) },
            Executable { assertEquals(Complex(5, -10), product * Complex(-1)) },
            Executable { assertEquals(Complex(-10, -5), product * Complex.J) },
            Executable { assertEquals(Complex(10, 5), product * -Complex.J) },
        )
        assertEquals(product, factor1 * factor2)
    }

    @Test
    fun div() {
        val quotient = Complex(1, 2)
        val divisor = Complex(3, 4)
        val dividend = Complex(-5, 10)
        val divJ = dividend * Complex.J
        assertAll(
            Executable { assertEquals(divisor, dividend / quotient) },
            Executable { assertEquals(quotient, dividend / divisor) },
            Executable { assertEquals(Complex.ONE, dividend / dividend) },
            Executable { assertEquals(dividend, dividend / Complex.ONE) },
            Executable { assertEquals(dividend, divJ / Complex.J) },
        )
    }

    @Test
    fun sqrt() {
        val number = Complex(1, 2)
        val sqrt2ov2 = sqrt(2.0) / 2.0
        assertAll(
            Executable { assertEquals(number, (number * number).sqrt()) },
            Executable { assertEquals(Complex.ONE, Complex.ONE.sqrt()) },
            Executable { assertEquals(Complex.J, (-Complex.ONE).sqrt()) },
            Executable { assertEquals(Complex(sqrt2ov2, -sqrt2ov2), (-Complex.J).sqrt()) },
        )
    }

    @Test
    fun `test complex plus double`() {
        val cmp = Complex(1, 2)
        val dub = PI
        val flo = dub.toFloat()
        val sum = Complex(PI + 1, 2)
        val fSum = Complex(PI.toFloat() + 1F, 2F)
        val iSum = Complex(4, 2)
        assertAll(
            Executable { assertEquals(sum, cmp + dub) },
            Executable { assertEquals(sum, dub + cmp) },
            Executable { assertEquals(cmp, 1.0 + 2.0.j()) },
        )
        assertAll(
            Executable { assertTrue(fSum.close(cmp + flo, EPSILON_FLOAT)) },
            Executable { assertTrue(fSum.close(flo + cmp, EPSILON_FLOAT)) },
            Executable { assertEquals(cmp, 1.0F + 2.0.j()) },
        )
        assertAll(
            Executable { assertEquals(iSum, cmp + 3) },
            Executable { assertEquals(iSum, 3 + cmp) },
            Executable { assertEquals(cmp, 1 + 2.0.j()) },
        )
        assertAll(
            Executable { assertEquals(iSum, cmp + 3L) },
            Executable { assertEquals(iSum, 3L + cmp) },
            Executable { assertEquals(cmp, 1L + 2.0.j()) },
        )
    }

    @Test
    fun `test complex minus double and vice versa`() {
        val cmp = Complex(2, 1)
        val diff = Complex(1, 1)
        val minusDiff = Complex(-1, 1)
        assertAll(
            Executable { assertEquals(diff, cmp - 1.0) },
            Executable { assertEquals(minusDiff, 1.0 - cmp) },
        )
        assertAll(
            Executable { assertEquals(diff, cmp - 1.0F) },
            Executable { assertEquals(minusDiff, 1.0F - cmp) },
        )
        assertAll(
            Executable { assertEquals(diff, cmp - 1) },
            Executable { assertEquals(minusDiff, 1 - cmp) },
        )
        assertAll(
            Executable { assertEquals(diff, cmp - 1L) },
            Executable { assertEquals(minusDiff, 1L - cmp) },
        )
    }

    @Test
    fun `test complex times double`() {
        val cmp = Complex(1, 2)
        val product = Complex(E, 2 * E)
        val iProduct = Complex(4, 8)
        assertAll(
            Executable { assertEquals(product, cmp * E) },
            Executable { assertEquals(product, E * cmp) },
            Executable { assertEquals(product, product * 1.0) },
            Executable { assertEquals(product, 1.0 * product) },
        )
        assertAll(
            Executable { assertTrue(product.close(cmp * E.toFloat(), EPSILON_FLOAT)) },
            Executable { assertTrue(product.close(E.toFloat() * cmp, EPSILON_FLOAT)) },
            Executable { assertEquals(product, product * 1.0F) },
            Executable { assertEquals(product, 1.0F * product) },
        )
        assertAll(
            Executable { assertEquals(iProduct, cmp * 4) },
            Executable { assertEquals(iProduct, 4 * cmp) },
            Executable { assertEquals(iProduct, iProduct * 1) },
            Executable { assertEquals(iProduct, 1 * iProduct) },
        )
        assertAll(
            Executable { assertEquals(iProduct, cmp * 4L) },
            Executable { assertEquals(iProduct, 4L * cmp) },
            Executable { assertEquals(iProduct, iProduct * 1L) },
            Executable { assertEquals(iProduct, 1L * iProduct) },
        )
    }

    @Test
    fun `test complex divided by double and vice versa`() {
        // (9 + 6j) / 3, calculated by hand
        val cmp = Complex(0.23076923076923078, -0.15384615384615385)
        assertAll(
            Executable { assertEquals(Complex.ONE, Complex(5) / 5.0) },
            Executable { assertEquals(Complex.ONE,  5.0 / Complex(5)) },
            Executable { assertEquals(Complex.J, -1.0 / Complex.J) },
            Executable { assertEquals(Complex.J,  Complex.J / 1.0) },
            Executable { assertEquals(Complex(3, 2),  Complex(9, 6) / 3.0) },
            Executable { assertEquals(cmp,  3.0 / Complex(9, 6)) },
        )
        assertAll(
            Executable { assertEquals(Complex.ONE, Complex(5) / 5.0F) },
            Executable { assertEquals(Complex.ONE,  5.0F / Complex(5)) },
            Executable { assertEquals(Complex.J, -1.0F / Complex.J) },
            Executable { assertEquals(Complex.J,  Complex.J / 1.0F) },
            Executable { assertEquals(Complex(3, 2),  Complex(9, 6) / 3.0F) },
            Executable { assertEquals(cmp,  3.0F / Complex(9, 6)) },
        )
        assertAll(
            Executable { assertEquals(Complex.ONE, Complex(5) / 5) },
            Executable { assertEquals(Complex.ONE,  5 / Complex(5)) },
            Executable { assertEquals(Complex.J, -1 / Complex.J) },
            Executable { assertEquals(Complex.J,  Complex.J / 1) },
            Executable { assertEquals(Complex(3, 2),  Complex(9, 6) / 3) },
            Executable { assertEquals(cmp,  3 / Complex(9, 6)) },
        )
        assertAll(
            Executable { assertEquals(Complex.ONE, Complex(5) / 5L) },
            Executable { assertEquals(Complex.ONE,  5L / Complex(5)) },
            Executable { assertEquals(Complex.J, -1L / Complex.J) },
            Executable { assertEquals(Complex.J,  Complex.J / 1L) },
            Executable { assertEquals(Complex(3, 2),  Complex(9, 6) / 3L) },
            Executable { assertEquals(cmp,  3L / Complex(9, 6)) },
        )
    }

    @Test
    fun pow() {
        val complex = Complex(1, 2)
        assertAll(
            Executable { assertEquals(Complex.ONE, Complex.ONE pow Complex.ONE) },
            Executable { assertEquals(-Complex.ONE, Complex(E) pow Complex(0, PI)) },
            Executable { assertEquals(Complex.ONE, Complex.ONE pow 8.0) },
            Executable { assertEquals(Complex.ONE, Complex.ONE pow -10.0) },
            Executable { assertEquals(Complex.ONE, Complex.ONE pow 0.5) },
            Executable { assertEquals(Complex.ONE, Complex(3, E) pow 0.0) },
            Executable { assertEquals(Complex(-3, 4), complex pow 2.0) },
            Executable { assertEquals(
                Complex(-12.419913389139916, -11.191704454359792),
                complex.pow(3.5)
            ) },
        )
    }

    @Test
    fun ln() {
        assertAll(
            Executable { assertEquals(Complex.ZERO, Complex.ONE.ln()) },
            Executable { assertEquals(Complex(0, -PI), (-Complex.ONE).ln()) },
            Executable { assertEquals(Complex.INFINITY, Complex.ZERO.ln()) },
        )
    }

    @Test
    fun `funny values`() {
        assertAll(
            Executable { assertTrue(Complex(Double.NaN, Double.POSITIVE_INFINITY).isNaN) },
            Executable { assertTrue(Complex(Double.POSITIVE_INFINITY, Double.NaN).isNaN) },
            Executable { assertFalse(Complex(Double.POSITIVE_INFINITY, Double.NaN).isInfinite) },
            Executable { assertFalse(Complex(Double.NaN, Double.POSITIVE_INFINITY).isInfinite) },
            Executable { assertTrue(Complex(1, Double.POSITIVE_INFINITY).isInfinite) },
            Executable { assertTrue(Complex(Double.POSITIVE_INFINITY, 1).isInfinite) },
            Executable { assertFalse(Complex(0, 1).isInfinite) },
            Executable { assertFalse(Complex(0, 1).isZero) },
            Executable { assertFalse(Complex(1, 0).isZero) },
            Executable { assertTrue(Complex(0, 0).isZero) },
        )
    }

    @Test
    fun hash() {
        val expect11 = -33554432
        val expect10 = -1106247680
        val expect01 = 1072693248
        assertAll(
            Executable { assertEquals(expect11, Complex(1, 1).hashCode()) },
            Executable { assertEquals(expect10, Complex(1, 0).hashCode()) },
            Executable { assertEquals(expect01, Complex(0, 1).hashCode()) },
        )


    }

    @Test
    fun polar() {
        assertAll(
            Executable {
                val rho = sqrt(2.0)
                val theta = PI / 4.0
                val polar = Complex(1, 1).polar()
                assertAll(
                    Executable { assertEquals(rho, polar.first, Complex.EPSILON) },
                    Executable { assertEquals(theta, polar.second, Complex.EPSILON) }
                )
            },
            Executable {
                val rho = sqrt(5.0)
                val theta = atan2(1.0, 2.0)
                val polar = Complex(2, 1).polar()
                assertAll(
                    Executable { assertEquals(rho, polar.first, Complex.EPSILON) },
                    Executable { assertEquals(theta, polar.second, Complex.EPSILON) }
                )
            },
            Executable {
                val rho = 5.0
                val theta = atan2(3.0, 4.0)
                val polar = Complex(4, 3).polar()
                assertAll(
                    Executable { assertEquals(rho, polar.first, Complex.EPSILON) },
                    Executable { assertEquals(theta, polar.second, Complex.EPSILON) }
                )
            },
            Executable {
                val rho = 5.0 + Complex.EPSILON * 0.99
                val theta = atan2(3.0, 4.0)
                val polar = Complex(4, 3).polar()
                assertAll(
                    Executable { assertEquals(rho, polar.first, Complex.EPSILON) },
                    Executable { assertEquals(theta, polar.second, Complex.EPSILON) }
                )
            },
        )
    }

    @Test
    fun equals() {
        val one = Complex(1, 1)
        val two = Complex(1, 1)

        assertAll(
            // reflexive
            Executable {
                val only = Complex(1, 1)
                assertEquals(only, only)
            },
            // next two for symmetric
            Executable { assertEquals(one, two) },
            Executable { assertEquals(two, one) },
            // against null
            Executable { assertNotEquals(one, null) },
            Executable { assertEquals(
                Complex(3, 3),
                Complex(3.0 + Complex.EPSILON / 2, 3)
            ) },
            Executable { assertEquals(
                Complex(PI + Complex.EPSILON / 2, PI + Complex.EPSILON / 2),
                Complex(PI, PI)
            ) },
            Executable { assertEquals(
                Complex(E, E + Complex.EPSILON / 2),
                Complex(E, E)
            ) },
            Executable { assertNotEquals(Complex.ONE, 1.0) },
            Executable { assertNotEquals(Complex.INFINITY, Complex(1.0, 1.0)) },
            Executable { assertNotEquals(Complex(1.0, 1.0), Complex.INFINITY) },
            Executable { assertNotEquals(Complex(Double.NaN), Complex(Double.NaN)) },
            Executable { assertNotEquals(Complex(1), Complex(Double.NaN)) },
            Executable { assertNotEquals(Complex(Double.NaN), Complex(1)) },
        )
    }

    @Test
    fun `zeroed parts`() {
        assertAll(
            Executable { assertTrue(Complex(1).isReal()) },
            Executable { assertFalse(Complex(1, 1).isReal()) },
            Executable { assertFalse(Complex.J.isReal()) },
            Executable { assertTrue(Complex.J.isImaginary()) },
            Executable { assertFalse(Complex.ONE.isImaginary()) },
            Executable { assertFalse(Complex(1, 2).isImaginary()) },
        )
    }

    @Test
    fun `close with specified epsilon`() {
        val test = Complex(PI, E)
        assertAll(
            Executable { assertTrue(test.close(Complex(3.14, 2.71), 0.1)) },
            Executable { assertFalse(test.close(Complex(3.14, 2.71), 0.001)) },
            Executable { assertFalse(test.close(Complex(3.14159, 2.71), 0.001)) },
        )
    }

    /**
     * Testing corner cases for toString()
     */
    @Test
    fun `toString oddities`() {
        assertAll(
            Executable { assertEquals("NaN", Complex(1.0, Double.NaN).toString()) },
            Executable { assertEquals("NaN", Complex(Double.NaN, 1.0).toString()) },
            Executable { assertEquals("NaN", Complex(Double.POSITIVE_INFINITY, Double.NaN).toString()) },
            Executable { assertEquals("NaN", Complex(Double.NaN, Double.POSITIVE_INFINITY).toString()) },
            Executable { assertEquals("Infinity", Complex(Double.POSITIVE_INFINITY, 1.0).toString()) },
            Executable { assertEquals("Infinity", Complex(1.0, Double.POSITIVE_INFINITY).toString()) },
            Executable { assertEquals("Infinity", Complex.INFINITY.toString()) },
            Executable { assertEquals("j", Complex(0, 1.0).toString()) },
            Executable { assertEquals("-j", Complex(0, -1.0).toString()) },
            Executable { assertEquals("-2j", Complex(0, -2.0).toString()) },
            Executable { assertEquals("-2", Complex(-2.0, 0).toString()) },
            Executable { assertEquals("-2 - j", Complex(-2.0, -1).toString()) },
            Executable { assertEquals("-2 + j", Complex(-2.0, 1).toString()) },
            Executable { assertEquals("-1 + 2j", Complex(-1.0, 2).toString()) },
            Executable { assertEquals("-1.5 + 1.5j", Complex(-1.5, 1.5).toString()) },
            Executable { assertEquals("0", Complex.ZERO.toString()) },
        )
    }

    @Test
    fun `trig functions`() {
        assertAll(
            Executable { assertEquals(Complex.ONE, Complex.ZERO.cos()) },
            Executable { assertEquals(Complex.ZERO, Complex.ZERO.sin()) },
            Executable { assertEquals(Complex.ZERO, Complex.PI_C.sin()) },
            Executable { assertEquals(Complex.ZERO, Complex.ZERO.tan()) },
            Executable { assertEquals(Complex.INFINITY, Complex.ZERO.cot()) },
            Executable { assertEquals(Complex.ONE, Complex(PI / 4).cot()) },
            Executable { assertEquals(Complex.ONE, Complex.ZERO.sec()) },
            Executable { assertTrue(Complex(PI / 2).sec().abs() > 1e10) }, // because approximations
            Executable { assertEquals(Complex.INFINITY, Complex.ZERO.csc()) },
            Executable { assertEquals(Complex.ONE, Complex(PI / 2)
                .csc()) },
            Executable { assertEquals(
                Complex(sin(1.0) * cosh(2.0), cos(1.0) * sinh(2.0)),
                Complex(1, 2).sin()
            ) },
            Executable { assertEquals(
                Complex(cos(1.0) * cosh(2.0), -sin(1.0) * sinh(2.0)),
                Complex(1, 2).cos()
            ) },
        )
    }

    @Test
    fun `inverse trig functions`() {
        assertAll(
            Executable { assertEquals(Complex.ZERO, Complex.ONE.acos()) },
            Executable { assertEquals(Complex.ZERO, Complex.ZERO.asin()) },
            Executable { assertEquals(Complex.ZERO, Complex.ZERO.atan()) },
            Executable { assertEquals(Complex.PI_C / 2.0, Complex.ZERO.acot()) },
            Executable { assertEquals(Complex.PI_C / 4.0, Complex.ONE.acot()) },
            Executable { assertEquals(Complex.ZERO, Complex.ONE.asec()) },
            Executable { assertEquals(Complex.INFINITY, Complex.ZERO.asec()) }, // because approximations
            Executable { assertEquals(Complex.INFINITY, Complex.ZERO.acsc()) },
            Executable { assertEquals(Complex.PI_C / 2.0, Complex.ONE.asin()) },
            Executable { assertEquals(Complex.PI_C / 6.0, Complex(0.5).asin()) },
        )
    }

    @Test
    fun `hyperbolic functions`() {
        assertAll(
            Executable { assertEquals(Complex.ONE, Complex.ZERO.cosh()) },
            Executable { assertEquals(Complex.ZERO, Complex.ZERO.sinh()) },
            Executable { assertEquals(Complex.ZERO, Complex.ZERO.tanh()) },
            Executable { assertEquals(Complex.INFINITY, Complex.ZERO.coth()) },
            Executable { assertEquals(Complex.ONE, Complex(100).coth()) }, // exp(100) is HUGE!
            Executable { assertEquals(Complex.ONE, Complex.ZERO.sech()) },
            Executable { assertEquals(Complex.INFINITY, Complex.ZERO.csch()) },
            Executable { assertEquals(Complex(2.0 / (exp(1.0) - exp(-1.0))), Complex.ONE.csch()) },
            Executable { assertEquals(Complex((exp(1.0) + exp(-1.0)) / 2), Complex.ONE.cosh()) },
            Executable { assertEquals(Complex((exp(1.0) - exp(-1.0)) / 2), Complex.ONE.sinh()) },
        )

        val onePlus3j = Complex(1, 3)
        val fourMinus2j = Complex(4, -2)
        assertAll(
            Executable { assertEquals(
                Complex(cosh(1.0) * cos(3.0), sinh(1.0) * sin(3.0)),
                onePlus3j.cosh()
            ) },
            Executable { assertEquals(
                Complex(cosh(4.0) * cos(-2.0), sinh(4.0) * sin(-2.0)),
                fourMinus2j.cosh()
            ) },
        )
    }

    @Test
    fun `inverse hyperbolic functions`() {
        assertAll(
            Executable { assertEquals(Complex.ZERO, Complex.ONE.acosh()) },
            Executable { assertEquals(Complex.ZERO, Complex.ZERO.asinh()) },
            Executable { assertEquals(Complex.ZERO, Complex.ZERO.atanh()) },
            Executable { assertEquals(Complex.INFINITY, Complex.ZERO.coth()) },
            Executable { assertEquals(Complex.ZERO, Complex.ONE.asech()) },
            Executable { assertEquals(Complex.ONE, Complex(2.0 / (exp(1.0) - exp(-1.0))).acsch()) },
            Executable { assertEquals(Complex.INFINITY, Complex.ZERO.acoth()) },
            Executable { assertEquals(Complex(0.8047189562), Complex(1.5).acoth()) },
            Executable { assertEquals(Complex.INFINITY, Complex.ZERO.asech()) },
            Executable { assertEquals(Complex.INFINITY, Complex.ZERO.acsch()) },
        )

        val onePlus3j = Complex(1, 3)
        val fourMinus2j = Complex(4, -2)
        assertAll(
            Executable { assertEquals(
                onePlus3j,
                Complex(cosh(1.0) * cos(3.0), sinh(1.0) * sin(3.0)).acosh()
            ) },
            Executable { assertEquals(
                fourMinus2j,
                Complex(cosh(4.0) * cos(-2.0), sinh(4.0) * sin(-2.0)).acosh()
            ) },
        )
    }

@Test
fun `reciprocal test`() {
    assertAll(
        Executable { assertEquals(Complex.ONE, Complex.ONE.reciprocal()) },
        Executable { assertEquals(Complex.INFINITY, Complex.ZERO.reciprocal()) },
        Executable { assertEquals(Complex.J, (-Complex.J).reciprocal()) },
        )
    }
}