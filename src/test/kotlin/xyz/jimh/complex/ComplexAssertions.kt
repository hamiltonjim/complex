package xyz.jimh.complex

import kotlin.math.abs
import kotlin.test.assertFalse
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertTrue

/**
 * Assertions for unit testing.
 */
object ComplexAssertions {
    private const val EPSILON = 1.0e-10

    fun assertEquals(expected: Complex, actual: Complex, epsilon: Double = EPSILON) {
        Assertions.assertAll(
            { assertEquals(expected.re, actual.re, epsilon) },
            { assertEquals(expected.im, actual.im, epsilon) },
        )
    }

    fun assertNotEquals(expected: Complex, actual: Complex, epsilon: Double = EPSILON) {
        assertFalse { abs(expected.re - actual.re) < epsilon && abs(expected.im - actual.im) < epsilon }
    }

    private fun assertEquals(expected: Double, actual: Double, epsilon: Double = EPSILON) {
        assertTrue {
            (expected.isInfinite() && actual.isInfinite()) || abs(expected - actual) < epsilon
        }
    }

    fun assertEqualTo(expected: Complex, actual: Any?) {
        assertTrue(expected.equalTo(actual))
    }

    fun assertEqualTo(expected: Number, actual: Complex) {
        assertTrue(expected.equalTo(actual))
    }

    fun assertNotEqualTo(expected: Complex, actual: Any?) {
        assertFalse { expected.equalTo(actual) }
    }

    fun assertNotEqualTo(expected: Number, actual: Complex) {
        assertFalse { expected.equalTo(actual) }
    }

    fun assertEqualTo(expected: Number, actual: Number, epsilon: Double = EPSILON) {
        assertTrue(expected.toDouble().close(actual.toDouble(), epsilon))
    }
}