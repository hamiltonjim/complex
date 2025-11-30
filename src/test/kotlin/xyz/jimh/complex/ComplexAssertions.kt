package xyz.jimh.complex

import kotlin.math.abs
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import org.junit.jupiter.api.Assertions.assertAll

/**
 * Assertions for unit testing.
 */
object ComplexAssertions {
    private const val EPSILON = Complex.EPSILON

    /**
     * Compare two Complex values, assert that they are equal. Covers the case where one or both
     * values is NaN (implicitly) or Infinite (explicitly).
     */
    fun assertEquals(expected: Complex, actual: Complex, epsilon: Double = EPSILON) {
        if (expected.isInfinite) assertTrue { actual.isInfinite }
        else
            assertAll(
                { assertEquals(expected.re, actual.re, epsilon) },
                { assertEquals(expected.im, actual.im, epsilon) },
            )
    }

    fun assertNotEquals(expected: Complex, actual: Complex, epsilon: Double) {
        assertFalse { abs(expected.re - actual.re) < epsilon && abs(expected.im - actual.im) < epsilon }
    }

    private fun assertEquals(expected: Double, actual: Double, epsilon: Double = EPSILON) {
        assertTrue {
            (expected.isInfinite() && actual.isInfinite()) || abs(expected - actual) < epsilon
        }
    }

    fun assertEqualTo(expected: Complex, actual: Any?) {
        assertTrue { expected.equalTo(actual) }
    }

    fun assertEqualTo(expected: Number, actual: Complex) {
        assertTrue { expected.equalTo(actual) }
    }

    fun assertNotEqualTo(expected: Complex, actual: Any?) {
        assertFalse { expected.equalTo(actual) }
    }

    fun assertNotEqualTo(expected: Number, actual: Complex) {
        assertFalse { expected.equalTo(actual) }
    }

    fun assertEqualTo(expected: Number, actual: Number, epsilon: Double = EPSILON) {
        assertTrue { expected.toDouble().close(actual.toDouble(), epsilon) }
    }
}