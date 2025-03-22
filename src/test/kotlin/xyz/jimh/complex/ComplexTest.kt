package xyz.jimh.complex

import kotlin.math.E
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.sqrt
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.function.Executable

class ComplexTest {

    @Test
    fun testClose() {
        assertTrue(Complex(1, 1) close Complex(1.0000000000001, 1.0000000000001))
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
        val divj = dividend * Complex.J
        assertAll(
            Executable { assertEquals(divisor, dividend / quotient) },
            Executable { assertEquals(quotient, dividend / divisor) },
            Executable { assertEquals(Complex.ONE, dividend / dividend) },
            Executable { assertEquals(dividend, dividend / Complex.ONE) },
            Executable { assertEquals(dividend, divj / Complex.J) },
        )
    }

    @Test
    fun sqrt() {
        val number = Complex(1, 2)
        assertAll(
            Executable { assertEquals(number, (number * number).sqrt()) },
            Executable { assertEquals(Complex.ONE, Complex.ONE.sqrt()) },
            Executable { assertEquals(Complex.J, (-Complex.ONE).sqrt()) }
        )
    }

    @Test
    fun `test complex plus double`() {
        val cmp = Complex(1, 2)
        val dub = PI
        val sum = Complex(PI + 1, 2)
        assertAll(
            Executable { assertEquals(sum, cmp + dub) },
            Executable { assertEquals(sum, dub + cmp) },
            Executable { assertEquals(cmp, 1.0 + 2.0.j()) },
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
    }

    @Test
    fun `test complex times double`() {
        val cmp = Complex(1, 2)
        val product = Complex(E, 2 * E)
        assertAll(
            Executable { assertEquals(product, cmp * E) },
            Executable { assertEquals(product, E * cmp) },
            Executable { assertEquals(product, product * 1.0) },
            Executable { assertEquals(product, 1.0 * product) },
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
    }

    @Test
    fun pow() {
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
        assertAll(
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
        )
    }
}