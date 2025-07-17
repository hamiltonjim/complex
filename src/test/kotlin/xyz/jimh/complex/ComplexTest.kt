package xyz.jimh.complex

import java.math.BigDecimal
import java.util.concurrent.atomic.AtomicInteger
import kotlin.math.E
import kotlin.math.PI
import kotlin.math.atan
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.cosh
import kotlin.math.exp
import kotlin.math.sin
import kotlin.math.sinh
import kotlin.math.sqrt
import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import xyz.jimh.complex.ComplexAssertions.assertEqualTo
import xyz.jimh.complex.ComplexAssertions.assertNotEqualTo
import xyz.jimh.complex.ComplexAssertions.assertNotEquals

class ComplexTest {

    @Test
    fun testClose() {
        assertTrue(Complex(1, 1).close(Complex(1.0000000000001, 1.0000000000001)))
    }

    @Test
    fun `test constructors`() {
        val real = Complex(1, 0)
        assertAll(
            { assertEquals(real, Complex(1.0)) },
            { assertEquals(real, Complex(1)) },
            { assertEquals(real, Complex(1L)) },
            { assertEquals(real, Complex(1.0F)) },
        )

        val complex = Complex(1.0, 1.0)
        assertAll(
            { assertEquals(complex, Complex(1.0, 1)) },
            { assertEquals(complex, Complex(1.0, 1L)) },
            { assertEquals(complex, Complex(1.0, 1.0F)) },
            { assertEquals(complex, Complex(1, 1.0)) },
            { assertEquals(complex, Complex(1L, 1.0)) },
            { assertEquals(complex, Complex(1.0F, 1.0)) },
            { assertEquals(complex, Complex(1, 1)) },
            { assertEquals(complex, Complex(1L, 1L)) },
            { assertEquals(complex, Complex(1.0F, 1.0F)) },
            { assertEquals(complex, Complex(1.toShort(), 1.toLong())) },
            { assertEquals(complex, Complex(BigDecimal("1"), BigDecimal("1"))) },
            { assertEquals(complex, Complex(AtomicInteger(1), 1.toByte())) },
        )
    }

    @Test
    fun `test exp`() {
        assertAll(
            { assertEqualTo(Complex(-1.0), Complex.PI_J.exp()) },
            { assertEqualTo(Complex(0.0, 1.0), (Complex.PI_J / 2.0).exp()) },
            { assertEqualTo(Complex(0.0, -1.0), (Complex.PI_J * 1.5).exp()) },
        )
    }

    @Test
    fun `test abs and arg`() {
        assertAll(
            { assertEqualTo(PI, Complex.PI_J.abs(), Complex.EPSILON) },
            { assertEqualTo(sqrt(2.0), Complex(1, 1).abs(), Complex.EPSILON) },
            { assertEqualTo(5.0, Complex(4, 3).abs(), Complex.EPSILON) },
            { assertEqualTo(PI / 4, Complex(2, 2).arg(), Complex.EPSILON) },
        )
    }

    @Test
    fun round() {
        assertEqualTo(Complex(3, 3), Complex(PI, E).round(0))
        assertEqualTo(Complex(3.1, 2.7), Complex(PI, E).round(1))
        assertEqualTo(Complex(3.14, 2.72), Complex(PI, E).round(2))
        assertEqualTo(Complex(3.142, 2.718), Complex(PI, E).round(3))
    }

    @Test
    operator fun unaryPlus() {
        val test = Complex(1.23456, 7.89012)
        val newOne = +test
        assertEquals(test, newOne)
        assertFalse(test === newOne)     // because it's a copy, not the original object
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
            { assertEquals(sum, val1 + val2) },
            { assertEquals(sum2, val2 + val1 + Complex.J) },
        )
    }

    @Test
    fun minus() {
        val minuend = Complex(4, 7)
        val subtrahend = Complex(3, 5)
        val difference = Complex(1, 2)
        assertAll(
            { assertEquals(difference, minuend - subtrahend) },
            { assertEquals(-difference,  subtrahend - minuend) },
            { assertEquals(Complex.ZERO, minuend - minuend) },
        )
    }

    @Test
    fun times() {
        val factor1 = Complex(1, 2)
        val factor2 = Complex(3, 4)
        val product = Complex(-5, 10)
        assertAll(
            { assertEquals(product, factor1 * factor2) },
            { assertEquals(factor2, factor2 * Complex(1)) },
            { assertEquals(Complex(5, -10), product * Complex(-1)) },
            { assertEquals(Complex(-10, -5), product * Complex.J) },
            { assertEquals(Complex(10, 5), product * -Complex.J) },
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
            { assertEquals(divisor, dividend / quotient) },
            { assertEquals(quotient, dividend / divisor) },
            { assertEquals(Complex.ONE, dividend / dividend) },
            { assertEquals(dividend, dividend / Complex.ONE) },
            { assertEquals(dividend, divJ / Complex.J) },
        )
    }

    @Test
    fun sqrt() {
        val number = Complex(1, 2)
        val sqrt2ov2 = sqrt(2.0) / 2.0
        assertAll(
            { assertEquals(number, (number * number).sqrt()) },
            { assertEquals(Complex.ONE, Complex.ONE.sqrt()) },
            { assertEquals(Complex.J, (-Complex.ONE).sqrt()) },
            { assertEquals(Complex(sqrt2ov2, -sqrt2ov2), (-Complex.J).sqrt()) },
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
            { assertEquals(sum, cmp + dub) },
            { assertEquals(sum, dub + cmp) },
            { assertEquals(cmp, 1.0 + 2.0.j()) },
        )
        assertAll(
            { assertTrue(fSum.close(cmp + flo, Complex.EPSILON_FLOAT)) },
            { assertTrue(fSum.close(flo + cmp, Complex.EPSILON_FLOAT)) },
            { assertEquals(cmp, 1.0F + 2.0.j()) },
        )
        assertAll(
            { assertEquals(iSum, cmp + 3) },
            { assertEquals(iSum, 3 + cmp) },
            { assertEquals(cmp, 1 + 2.0.j()) },
        )
        assertAll(
            { assertEquals(iSum, cmp + 3L) },
            { assertEquals(iSum, 3L + cmp) },
            { assertEquals(cmp, 1L + 2.0.j()) },
        )
    }

    @Test
    fun `test complex minus double and vice versa`() {
        val cmp = Complex(2, 1)
        val diff = Complex(1, 1)
        val minusDiff = Complex(-1, 1)
        assertAll(
            { assertEquals(diff, cmp - 1.0) },
            { assertEquals(minusDiff, 1.0 - cmp) },
        )
        assertAll(
            { assertEquals(diff, cmp - 1.0F) },
            { assertEquals(minusDiff, 1.0F - cmp) },
        )
        assertAll(
            { assertEquals(diff, cmp - 1) },
            { assertEquals(minusDiff, 1 - cmp) },
        )
        assertAll(
            { assertEquals(diff, cmp - 1L) },
            { assertEquals(minusDiff, 1L - cmp) },
        )
    }

    @Test
    fun `test toComplex()`() {
        assertAll(
            { assertEquals(Complex(10), 10.toComplex()) },
            { assertEquals(Complex.PI_C, PI.toComplex()) },
            { assertEquals(Complex(BigDecimal.TEN), BigDecimal.TEN.toComplex()) },
            { assertEquals(Complex(BigDecimal.TEN), 10L.toComplex()) },
        )
    }

    @Test
    fun `test j function`() {
        assertAll(
            { assertEquals(Complex(0,2), 2.0.j()) },
            { assertEquals(Complex(2), (2.0.j() / Complex.J)) },
            { assertEquals(Complex(0, 2.5), doubleJSample()) },
            { assertEquals(Complex(0, 10.1378), bigDecimalJSample()) },
        )
    }

    @Test
    fun `test toDouble`() {
        assertAll(
            { assertEquals(10.0, Complex(10.0).toDouble()) },
            { assertThrows<ArithmeticException> { 1.j().toDouble() } },
            { assertThrows<ArithmeticException> { Complex(1, 1).toDouble() } },
        )
    }

    @Test
    fun `test toFloat`() {
        assertAll(
            { assertEquals(10.0F, Complex(10.0).toFloat()) },
            { assertThrows<ArithmeticException> { 1.j().toFloat() } },
            { assertThrows<ArithmeticException> { Complex(1, 1).toFloat() } },
        )
    }

    @Test
    fun `test toLong`() {
        assertAll(
            { assertEquals(10L, Complex(10.0).toLong()) },
            { assertThrows<ArithmeticException> { 1.j().toLong() } },
            { assertThrows<ArithmeticException> { Complex(1, 1).toLong() } },
        )
    }

    @Test
    fun `test toInt`() {
        assertAll(
            { assertEquals(10, Complex(10.0).toInt()) },
            { assertThrows<ArithmeticException> { 1.j().toInt() } },
            { assertThrows<ArithmeticException> { Complex(1, 1).toInt() } },
        )
    }

    @Test
    fun `test toBigDecimal`() {
        assertAll(
            { assertEquals(0, BigDecimal.TEN.compareTo(Complex(10.0).toBigDecimal())) },
            { assertThrows<ArithmeticException> { 1.j().toBigDecimal() } },
            { assertThrows<ArithmeticException> { Complex(1, 1).toBigDecimal() } },
        )
    }

    @Test
    fun `test complex times double`() {
        val cmp = Complex(1, 2)
        val product = Complex(E, 2 * E)
        val iProduct = Complex(4, 8)
        assertAll(
            { assertEquals(product, cmp * E) },
            { assertEquals(product, E * cmp) },
            { assertEquals(product, product * 1.0) },
            { assertEquals(product, 1.0 * product) },
        )
        assertAll(
            { assertTrue(product.close(cmp * E.toFloat(), Complex.EPSILON_FLOAT)) },
            { assertTrue(product.close(E.toFloat() * cmp, Complex.EPSILON_FLOAT)) },
            { assertEquals(product, product * 1.0F) },
            { assertEquals(product, 1.0F * product) },
        )
        assertAll(
            { assertEquals(iProduct, cmp * 4) },
            { assertEquals(iProduct, 4 * cmp) },
            { assertEquals(iProduct, iProduct * 1) },
            { assertEquals(iProduct, 1 * iProduct) },
        )
        assertAll(
            { assertEquals(iProduct, cmp * 4L) },
            { assertEquals(iProduct, 4L * cmp) },
            { assertEquals(iProduct, iProduct * 1L) },
            { assertEquals(iProduct, 1L * iProduct) },
        )
    }

    @Test
    fun `test complex divided by double and vice versa`() {
        // (9 + 6j) / 3, calculated by hand
        val cmp = Complex(0.23076923076923078, -0.15384615384615385)
        assertAll(
            { assertEquals(Complex.ONE, Complex(5) / 5.0) },
            { assertEquals(Complex.ONE,  5.0 / Complex(5)) },
            { assertEquals(Complex.J, -1.0 / Complex.J) },
            { assertEquals(Complex.J,  Complex.J / 1.0) },
            { assertEquals(Complex(3, 2),  Complex(9, 6) / 3.0) },
            { assertEquals(cmp,  3.0 / Complex(9, 6)) },
        )
        assertAll(
            { assertEquals(Complex.ONE, Complex(5) / 5.0F) },
            { assertEquals(Complex.ONE,  5.0F / Complex(5)) },
            { assertEquals(Complex.J, -1.0F / Complex.J) },
            { assertEquals(Complex.J,  Complex.J / 1.0F) },
            { assertEquals(Complex(3, 2),  Complex(9, 6) / 3.0F) },
            { assertEquals(cmp,  3.0F / Complex(9, 6)) },
        )
        assertAll(
            { assertEquals(Complex.ONE, Complex(5) / 5) },
            { assertEquals(Complex.ONE,  5 / Complex(5)) },
            { assertEquals(Complex.J, -1 / Complex.J) },
            { assertEquals(Complex.J,  Complex.J / 1) },
            { assertEquals(Complex(3, 2),  Complex(9, 6) / 3) },
            { assertEquals(cmp,  3 / Complex(9, 6)) },
        )
        assertAll(
            { assertEquals(Complex.ONE, Complex(5) / 5L) },
            { assertEquals(Complex.ONE,  5L / Complex(5)) },
            { assertEquals(Complex.J, -1L / Complex.J) },
            { assertEquals(Complex.J,  Complex.J / 1L) },
            { assertEquals(Complex(3, 2),  Complex(9, 6) / 3L) },
            { assertEquals(cmp,  3L / Complex(9, 6)) },
        )
    }

    @Test
    fun pow() {
        val complex = Complex(1, 2)
        assertAll(
            { assertEqualTo(Complex.ONE, Complex.ONE pow Complex.ONE) },
            { assertEqualTo(-Complex.ONE, Complex(E) pow Complex(0, PI)) },
            { assertEqualTo(Complex.ONE, Complex.ONE pow 8.0) },
            { assertEqualTo(Complex.ONE, Complex.ONE pow -10.0) },
            { assertEqualTo(Complex.ONE, Complex.ONE pow 0.5) },
            { assertEqualTo(Complex.ONE, Complex(3, E) pow 0.0) },
            { assertEqualTo(Complex(-3, 4), complex pow 2.0) },
            { assertEqualTo(
                Complex(-12.419913389139916, -11.191704454359792),
                complex.pow(3.5)
            ) },
        )
    }

    @Test
    fun ln() {
        assertAll(
            { assertEqualTo(Complex.ZERO, Complex.ONE.ln()) },
            { assertEqualTo(Complex(0, -PI), (-Complex.ONE).ln()) },
            { assertEqualTo(Complex.INFINITY, Complex.ZERO.ln()) },
        )
    }

    @Test
    fun `funny values`() {
        assertAll(
            { assertTrue(Complex(Double.NaN, Double.POSITIVE_INFINITY).isNaN) },
            { assertTrue(Complex(Double.POSITIVE_INFINITY, Double.NaN).isNaN) },
            { assertFalse(Complex(Double.POSITIVE_INFINITY, Double.NaN).isInfinite) },
            { assertFalse(Complex(Double.NaN, Double.POSITIVE_INFINITY).isInfinite) },
            { assertTrue(Complex(1, Double.POSITIVE_INFINITY).isInfinite) },
            { assertTrue(Complex(Double.POSITIVE_INFINITY, 1).isInfinite) },
            { assertFalse(Complex(0, 1).isInfinite) },
            { assertFalse(Complex(0, 1).isZero) },
            { assertFalse(Complex(1, 0).isZero) },
            { assertTrue(Complex(0, 0).isZero) },
        )
    }

    @Test
    fun hash() {
        val expect11 = -33554432
        val expect10 = -1106247680
        val expect01 = 1072693248
        assertAll(
            { assertEquals(expect11, Complex(1, 1).hashCode()) },
            { assertEquals(expect10, Complex(1, 0).hashCode()) },
            { assertEquals(expect01, Complex(0, 1).hashCode()) },
        )


    }

    @Test
    fun polar() {
        assertAll(
            {
                val rho = sqrt(2.0)
                val theta = PI / 4.0
                val polar = Complex(1, 1).polar()
                assertAll(
                    { assertEquals(rho, polar.rho, Complex.EPSILON) },
                    { assertEquals(theta, polar.theta, Complex.EPSILON) }
                )
            },
            {
                val rho = sqrt(5.0)
                val theta = atan2(1.0, 2.0)
                val polar = Complex(2, 1).polar()
                assertAll(
                    { assertEquals(rho, polar.rho, Complex.EPSILON) },
                    { assertEquals(theta, polar.theta, Complex.EPSILON) }
                )
            },
            {
                val rho = 5.0
                val theta = atan2(3.0, 4.0)
                val polar = Complex(4, 3).polar()
                assertAll(
                    { assertEquals(rho, polar.rho, Complex.EPSILON) },
                    { assertEquals(theta, polar.theta, Complex.EPSILON) }
                )
            },
            {
                val rho = 5.0 + Complex.EPSILON * 0.99
                val theta = atan2(3.0, 4.0)
                val polar = Complex(4, 3).polar()
                assertAll(
                    { assertEquals(rho, polar.rho, Complex.EPSILON) },
                    { assertEquals(theta, polar.theta, Complex.EPSILON) }
                )
            },
        )
    }

    @Test
    fun equals() {
        val one = Complex(1, 1)
        val two = Complex(1, 1)

        val inf1 = Complex(Double.POSITIVE_INFINITY, 1)
        val inf2 = Complex(1, Double.NEGATIVE_INFINITY)

        val nan1 = Complex(Double.NaN)

        assertAll(
            // reflexive
            {
                val only = Complex(1, 1)
                assertEquals(only, only)
            },
            // next two for symmetric
            { assertEquals(one, two) },
            { assertEquals(two, one) },
            // against null
            { assertNotEquals(one, null) },
            // against infinity
            { assertNotEquals(one, Complex.INFINITY) },
            { assertNotEquals(Complex.INFINITY, one) },
            { assertEquals(inf1, inf2) },

            // using close
            { assertNotEqualTo(one, Complex.INFINITY) },
            { assertNotEqualTo(Complex.INFINITY, one) },

            // against NaN
            { assertNotEquals(nan1, nan1, Complex.EPSILON) },
            { assertNotEquals(nan1, one, Complex.EPSILON) },
            { assertNotEquals(one, nan1, Complex.EPSILON) },

            // using close
            { assertNotEqualTo(nan1, nan1) },
            { assertNotEqualTo(nan1, one) },
            { assertNotEqualTo(one, nan1) },

            // some other combinations with equalTo
            { assertEqualTo(Complex.ONE, 1)},
            { assertEqualTo(1, Complex.ONE)},
            { assertNotEqualTo(one, null) },
            { assertNotEqualTo(1.0, nan1)},
            { assertFalse(Complex.ONE.equalTo("foo"))},

            { assertEqualTo(1, 1.0)},
            { assertEqualTo(1, Complex.ONE)},
            { assertNotEqualTo(1, Complex.ZERO)},
            { assertNotEqualTo(1, one)},
            { assertFalse { 1.equalTo("one")  }},
            { assertTrue { 1.equalTo(1F) }},

            { assertEqualTo(
                Complex(3, 3),
                Complex(3.0 + Complex.EPSILON / 2, 3)
            ) },
            { assertEqualTo(
                Complex(PI + Complex.EPSILON / 2, PI + Complex.EPSILON / 2),
                Complex(PI, PI)
            ) },
            { assertEqualTo(
                Complex(E, E + Complex.EPSILON / 2),
                Complex(E, E)
            ) },

            // is the complex number equal to a real number?
            { assertTrue(Complex.ONE.equalTo(1.0)) },
            { assertTrue(Complex.ONE.equalTo(1.0F)) },
            { assertTrue(Complex.ONE.equalTo(1)) },
            { assertTrue(Complex.ONE.equalTo(1L)) },
            { assertTrue(Complex.ONE.equalTo(1.toShort())) },
            { assertFalse(Complex(1, 1).equalTo(1.0)) },
            { assertFalse(Complex.ONE.equalTo(1.1)) },

            { assertNotEquals(Complex.INFINITY, Complex(1.0, 1.0)) },
            { assertNotEquals(Complex(1.0, 1.0), Complex.INFINITY) },
            { assertNotEquals(Complex(Double.NaN), Complex(Double.NaN)) },
            { assertNotEquals(Complex(1), Complex(Double.NaN)) },
            { assertNotEquals(Complex(Double.NaN), Complex(1)) },
        )
    }

    @Test
    fun `zeroed parts`() {
        assertAll(
            { assertTrue(Complex(1).isReal) },
            { assertFalse(Complex(1, 1).isReal) },
            { assertFalse(Complex.J.isReal) },
            { assertTrue(Complex.J.isImaginary) },
            { assertFalse(Complex.ONE.isImaginary) },
            { assertFalse(Complex(1, 2).isImaginary) },
            { assertTrue(Complex(1e-40, 2e-50).isZero) },
        )
    }

    @Test
    fun `close with specified epsilon`() {
        val test = Complex(PI, E)
        assertAll(
            { assertTrue(test.close(Complex(3.14, 2.71), 0.1)) },
            { assertFalse(test.close(Complex(3.14, 2.71), 0.001)) },
            { assertFalse(test.close(Complex(3.14159, 2.71), 0.001)) },
        )
    }

    /**
     * Testing corner cases for toString()
     */
    @Test
    fun `toString oddities`() {
        assertAll(
            { assertEquals("NaN", Complex(1.0, Double.NaN).toString()) },
            { assertEquals("NaN", Complex(Double.NaN, 1.0).toString()) },
            { assertEquals("NaN", Complex(Double.POSITIVE_INFINITY, Double.NaN).toString()) },
            { assertEquals("NaN", Complex(Double.NaN, Double.POSITIVE_INFINITY).toString()) },
            { assertEquals("Infinity", Complex(Double.POSITIVE_INFINITY, 1.0).toString()) },
            { assertEquals("Infinity", Complex(1.0, Double.POSITIVE_INFINITY).toString()) },
            { assertEquals("Infinity", Complex.INFINITY.toString()) },
            { assertEquals("j", Complex(0, 1.0).toString()) },
            { assertEquals("-j", Complex(0, -1.0).toString()) },
            { assertEquals("-2j", Complex(0, -2.0).toString()) },
            { assertEquals("-2", Complex(-2.0, 0).toString()) },
            { assertEquals("-2 - j", Complex(-2.0, -1).toString()) },
            { assertEquals("-2 + j", Complex(-2.0, 1).toString()) },
            { assertEquals("-1 + 2j", Complex(-1.0, 2).toString()) },
            { assertEquals("-1.5 + 1.5j", Complex(-1.5, 1.5).toString()) },
            { assertEquals("0", Complex.ZERO.toString()) },
        )
    }

    @Test
    fun `trig functions`() {
        assertAll(
            { assertEqualTo(Complex.ONE, Complex.ZERO.cos()) },
            { assertEqualTo(Complex.ZERO, Complex.ZERO.sin()) },
            { assertEqualTo(Complex.ZERO, Complex.PI_C.sin()) },
            { assertEqualTo(Complex.ZERO, Complex.ZERO.tan()) },
            { assertEqualTo(Complex.INFINITY, Complex.ZERO.cot()) },
            { assertEqualTo(Complex.ONE, Complex(PI / 4).cot()) },
            { assertEqualTo(Complex.ONE, Complex.ZERO.sec()) },
            { assertTrue(Complex(PI / 2).sec().abs() > 1e10) }, // because approximations
            { assertEqualTo(Complex.INFINITY, Complex.ZERO.csc()) },
            { assertEqualTo(Complex.ONE, Complex(PI / 2)
                .csc()) },
            { assertEqualTo(
                Complex(sin(1.0) * cosh(2.0), cos(1.0) * sinh(2.0)),
                Complex(1, 2).sin()
            ) },
            { assertEqualTo(
                Complex(cos(1.0) * cosh(2.0), -sin(1.0) * sinh(2.0)),
                Complex(1, 2).cos()
            ) },
        )
    }

    @Test
    fun `inverse trig functions`() {
        assertAll(
            { ComplexAssertions.assertEquals(Complex.ZERO, Complex.ONE.acos()) },
            { ComplexAssertions.assertEquals(Complex.ZERO, Complex.ZERO.asin()) },
            { ComplexAssertions.assertEquals(Complex.ZERO, Complex.ZERO.atan()) },
            { ComplexAssertions.assertEquals(Complex.PI_C / 2.0, Complex.ZERO.acot()) },
            { ComplexAssertions.assertEquals(Complex.PI_C / 4.0, Complex.ONE.acot()) },
            { ComplexAssertions.assertEquals(Complex.ZERO, Complex.ONE.asec()) },
            { ComplexAssertions.assertEquals(Complex.INFINITY, Complex.ZERO.asec()) }, // because approximations
            { ComplexAssertions.assertEquals(Complex.INFINITY, Complex.ZERO.acsc()) },
            { ComplexAssertions.assertEquals(Complex.PI_C / 2.0, Complex.ONE.asin()) },
            { ComplexAssertions.assertEquals(Complex.PI_C / 6.0, Complex(0.5).asin()) },
        )
    }

    @Test
    fun `hyperbolic functions`() {
        assertAll(
            { assertEquals(Complex.ONE, Complex.ZERO.cosh()) },
            { assertEquals(Complex.ZERO, Complex.ZERO.sinh()) },
            { assertEquals(Complex.ZERO, Complex.ZERO.tanh()) },
            { assertEquals(Complex.INFINITY, Complex.ZERO.coth()) },
            { assertEquals(Complex.ONE, Complex(100).coth()) }, // exp(100) is HUGE!
            { assertEquals(Complex.ONE, Complex.ZERO.sech()) },
            { assertEquals(Complex.INFINITY, Complex.ZERO.csch()) },
            { assertEquals(Complex(2.0 / (exp(1.0) - exp(-1.0))), Complex.ONE.csch()) },
            { assertEquals(Complex((exp(1.0) + exp(-1.0)) / 2), Complex.ONE.cosh()) },
            { assertEquals(Complex((exp(1.0) - exp(-1.0)) / 2), Complex.ONE.sinh()) },
        )

        val onePlus3j = Complex(1, 3)
        val fourMinus2j = Complex(4, -2)
        assertAll(
            { assertEqualTo(
                Complex(cosh(1.0) * cos(3.0), sinh(1.0) * sin(3.0)),
                onePlus3j.cosh()
            ) },
            { assertEqualTo(
                Complex(cosh(4.0) * cos(-2.0), sinh(4.0) * sin(-2.0)),
                fourMinus2j.cosh()
            ) },
        )
    }

    @Test
    fun `inverse hyperbolic functions`() {
        assertAll(
            { assertEqualTo(Complex.ZERO, Complex.ONE.acosh()) },
            { assertEqualTo(Complex.ZERO, Complex.ZERO.asinh()) },
            { assertEqualTo(Complex.ZERO, Complex.ZERO.atanh()) },
            { assertEqualTo(Complex.INFINITY, Complex.ZERO.coth()) },
            { assertEqualTo(Complex.ZERO, Complex.ONE.asech()) },
            { assertEqualTo(Complex.ONE, Complex(2.0 / (exp(1.0) - exp(-1.0))).acsch()) },
            { assertEqualTo(Complex.INFINITY, Complex.ZERO.acoth()) },
            { assertEqualTo(Complex(0.8047189562), Complex(1.5).acoth()) },
            { assertEqualTo(Complex.INFINITY, Complex.ZERO.asech()) },
            { assertEqualTo(Complex.INFINITY, Complex.ZERO.acsch()) },
        )

        val onePlus3j = Complex(1, 3)
        val fourMinus2j = Complex(4, -2)
        assertAll(
            { assertEquals(
                onePlus3j,
                Complex(cosh(1.0) * cos(3.0), sinh(1.0) * sin(3.0)).acosh()
            ) },
            { assertEquals(
                fourMinus2j,
                Complex(cosh(4.0) * cos(-2.0), sinh(4.0) * sin(-2.0)).acosh()
            ) },
        )
    }

    @Test
    fun `reciprocal test`() {
        assertAll(
            { assertEquals(Complex.ONE, Complex.ONE.reciprocal()) },
            { assertEquals(Complex.INFINITY, Complex.ZERO.reciprocal()) },
            { assertEquals(Complex.J, (-Complex.J).reciprocal()) },
        )
    }

    @Test
    fun `from polar test`() {
        assertAll(
            { assertEqualTo(Complex(1, 1), Complex.fromPolar2(sqrt(2.0), PI / 4.0)) },
            { assertEqualTo(Complex(1, 1), Complex.Polar.fromPolarSample()) },
            { assertEqualTo(Complex(2, 1), Complex.fromPolar2(sqrt(5.0), atan(0.5))) },
            { assertEqualTo(
                Complex(472, 981),
                Complex.fromPolar(Complex(472,981).polar())
            ) },
        )
    }

    @Test
    fun `test increment and decrement`() {
        var number = Complex(1, 1)
        assertAll(
            { assertEquals(Complex(2, 1), ++number) },
            { assertEquals(Complex(3, 1), ++number) },
            { assertEquals(Complex(2, 1), --number) },
            { assertEquals(Complex(1, 1), --number) },
            { assertEquals(Complex(1, 1), number--) },
            { assertTrue(number.isImaginary) },
            { assertEquals(Complex(-1, 1), --number) },
            { assertEquals(Complex(-1, 1), number--) },
            { assertEquals(Complex(-2, 1), number) },
            { assertEquals(Complex(-2, 1), number++) },
            { assertEquals(Complex(-1, 1), number) },
            { assertFalse(number.isImaginary) },
        )
    }

    /**
     * Not really testing anything, main() is just some ad hoc tests; but
     * will confirm that main runs and returns Unit.
     */
    @Test
    fun `completion test main`() {
        assertEquals(Unit, main())
    }
}