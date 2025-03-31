# Complex.kt
### Copyright © 2025 Jim Hamilton
## A Kotlin class (and extension functions) to handle complex math.

Usage: simply include Complex.kt in your project. 
[KDoc](https://hamiltonjim.github.io/complex/index.html)

To create a complex val, just pass the real and 
imaginary parts to the constructor for Complex. 
```kotlin
    val num = Complex(1.5, 3.25)
```
The properties are called `re` and `im` for
the real and imaginary parts, respectively.
Both parts are Double, but you may construct a 
Complex with either or both parts as any type 
that descends from Number. For example:
```kotlin
    val withInts = Complex(1, 4)
    val withLongs = Complex(3L, 1L)
    val withOneInt = Complex(3.1, 4)
    val withOneIntOneFloat = Complex(3, 4F)
```

To create a pure real number as Complex (that is,
with zero for the imaginary part), just pass the
real part, such as with
```kotlin
    val pureReal = Complex(3.14)
```
You can create a pure imaginary with the extension
function 
```kotlin
fun Double.j(): Complex = Complex(0.0, this)

```
(Aside: I used j instead of i because my degree is
in electrical engineering, and I stands for current, 
as in "V = IR" (Ohm's Law). If you want to use I, you have the 
source code. The companion object has definitions 
for J:
```kotlin
val J = 1.0.j()
val J_CHAR = 'j'
```
The latter is used by `toString()` to display a
complex number as "3 + 4j" for example.)

If you want to modify J, or Double.j(), please do it 
with an IDE that supports "Refactor" so you don't 
miss any instances.

There is a method called
```kotlin
fun close(other: Complex, epsilon: Double = EPSILON): Boolean {
    return abs(re - other.re) < epsilon && abs(im - other.im) < epsilon
}
```
which will return
true if other is within "epsilon" of this. That is
defined as both the real and imaginary parts are
within "epsilon" (defaults to 1.0e-10) of their
counterparts. (Note that this is the adjective
"close," as in "nearby," not the verb "close," as
in "shut the door.") Why is this here? [What
Every Computer Scientist Should Know About
Floating Point](https://docs.oracle.com/cd/E19957-01/806-3568/ncg_goldberg.html)

There is also an extension function Double.close()
that does the same test.

A Complex has the following properties:
```kotlin
val isNaN = re.isNan || im.isNaN // true if either part is a NaN
val isInfinite = !isNaN && (re.isInfinite || im.isInfinite) // true if !isNan and either part is infinite
val isZero = abs(re) <= EPSILON / 1000 && abs(im) <= EPSILON / 1000 // true if both parts are "close" to zero
val isReal = im.close(0.0, EPSILON / 1000) // true if imaginary part is zero
val isImaginary = re.close(0.0, EPSILON / 1000) // true if real part is zero
```
Note that isZero, isReal, and isImaginary check
more stringently than the "standard" close() 
function.

There are operator overloads for plus, minus, times,
and div, that work for Complex op Complex, and any
combination of Complex and Double, Float, Int, and
Long.

The following functions are defined:
- polar(): Polar (returns radius and angle)
- abs(): Double (returns the magnitude of this)
- arg(): Double (returns the angle in radians,
  counter-clockwise from the real axis)
- exp(): Complex (returns e^this)
- ln(): Complex (returns the principal value of the natural logarithm--
remember polar coordinates are periodic)
- sqrt(): Complex (returns principal square root of this)
- Double.sqrt(): Complex (replaces the standard lib function, 
works on negative numbers)
- pow(exponent: Complex): Complex and
- pow(exponent: Double): Complex (to take this to
any arbitrary power)
- reciprocal(): Complex (returns 1 / this)
- equals(other: Any?): Boolean (returns true
if other is Complex and close to this; written
in terms of close(other: Complex): Boolean)
- expITheta(theta: Double): Complex (leaning on
Euler's formula, returns cos(theta) + j * sin(theta))

The trigonometric functions and their inverses:
- sin(): Complex (returns the sine)
- cos(): Complex (cosine)
- tan(): Complex (tangent)
- cot(): Complex (cotangent == 1 / tangent)
- sec(): Complex (secant == 1 / cosine)
- csc(): Complex (cosecant == 1 / sine)
- asin(): Complex (Arc sine)
- acos(): Complex (Arc cosine)
- atan(): Complex (Arc tangent)
- acot(): Complex (Arc cotangent)
- asec(): Complex (Arc secant)
- acsc(): Complex (Arc cosecant)

The hyperbolic functions and their inverses:
- cosh(): Complex (hyperbolic cosine (exp(z) + exp(-z)) / 2)
- sinh(): Complex (returns the sine (exp(z) - exp(-z)) / 2))
- tanh(): Complex (tangent sinh / cosh)
- coth(): Complex (cotangent == 1 / tanh)
- sech(): Complex (secant == 1 / cosh)
- csch(): Complex (cosecant == 1 / sinh)
- asinh(): Complex (Arc sine)
- acosh(): Complex (Arc cosine)
- atanh(): Complex (Arc tangent)
- acoth(): Complex (Arc cotangent)
- asech(): Complex (Arc secant)
- acsch(): Complex (Arc cosecant)

The companion object also defines two 
functions:
- Companion.fromPolar(polar: Pair<Double, Double>): Complex
- Companion.fromPolar2(radius: Double, theta: Double): Complex
Either of these will create a complex number in
rectangular coordinates from the given values.

Distributed under the MIT License.