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
fun Number.j(): Complex = Complex(0.0, this)

```
(Aside: I used j instead of i because my degree is
in electrical engineering, and I stands for current, 
as in "V = IR" (Ohm's Law). If you want to use I, you have the 
source code. The companion object has definitions 
for J:
```kotlin
val J = 1.j()
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
true if other is within "epsilon" of the receiver. That is
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
combination of Complex and Double, Float, Int, 
Long, and BigDecimal.

The following functions are defined:
- `fun polar(): Polar` (returns radius and angle, as a Polar)
- `fun abs(): Double` (returns the magnitude of the receiver)
- `fun arg(): Double` (returns the angle in radians,
  counter-clockwise from the real axis)
- `fun exp(): Complex` (returns e^receiver)
- `fun ln(): Complex` (returns the principal value of the natural logarithm--
remember polar coordinates are periodic)
- `fun sqrt(): Complex` (returns principal square root of the receiver)
- `fun Double.sqrt(): Complex` (replaces the standard lib function, 
works on negative numbers)
- `fun pow(exponent: Complex): Complex` and
- `fun pow(exponent: Double): Complex` (to take the receiver to
any arbitrary power)
- `fun reciprocal(): Complex` (returns 1 / receiver)
- `fun equals(other: Any?): Boolean` (returns true
if other is Complex and close to the receiver; or if other
is a Number, the reciever isReal, and other is
close to the receiver. Written
in terms of close(other: Complex): Boolean)
- `fun expITheta(theta: Double): Complex` (leaning on
Euler's formula, returns cos(theta) + j * sin(theta))

#### Conversions to other numeric types

These return the real part, if and only
if the imaginary part is zero; otherwise,
they throw an ArithmeticException
- `fun toDouble(): Double`
- `fun toFloat(): Float`
- `fun toInt(): Int`
- `fun toLong(): Long`
- `fun toBigDecimal(): BigDecimal`


#### The trigonometric functions and their inverses:

- `fun sin(): Complex` (circular sine)
- `fun cos(): Complex` (circular cosine)
- `fun tan(): Complex` (circular tangent)
- `fun cot(): Complex` (circular cotangent == 1 / tangent)
- `fun sec(): Complex` (circular secant == 1 / cosine)
- `fun csc(): Complex` (circular cosecant == 1 / sine)
- `fun asin(): Complex` (Arc circular sine)
- `fun acos(): Complex` (Arc circular cosine)
- `fun atan(): Complex` (Arc circular tangent)
- `fun acot(): Complex` (Arc circular cotangent)
- `fun asec(): Complex` (Arc circular secant)
- `fun acsc(): Complex` (Arc circular cosecant)

#### The hyperbolic functions and their inverses:

- `fun cosh(): Complex` (hyperbolic cosine (exp(z) + exp(-z)) / 2)
- `fun sinh(): Complex` (hyperbolic sine (exp(z) - exp(-z)) / 2))
- `fun tanh(): Complex` (hyperbolic tangent sinh / cosh)
- `fun coth(): Complex` (hyperbolic cotangent == 1 / tanh)
- `fun sech(): Complex` (hyperbolic secant == 1 / cosh)
- `fun csch(): Complex` (hyperbolic cosecant == 1 / sinh)
- `fun asinh(): Complex` (Arc hyperbolic sine)
- `fun acosh(): Complex` (Arc hyperbolic cosine)
- `fun atanh(): Complex` (Arc hyperbolic tangent)
- `fun acoth(): Complex` (Arc hyperbolic cotangent)
- `fun asech(): Complex` (Arc hyperbolic secant)
- `fun acsch(): Complex` (Arc hyperbolic cosecant)

#### Polar

There is a nested class, Polar, consisting of the
radius (magnitude) and angle (arg) of a complex
number. Polar defines one function:
- `fun fromPolar(): Complex` (converts to rectangular
coordinates)

#### Complex's companion object also defines two functions:

- `fun Companion.fromPolar(polarCoordinates: Polar): Complex`
- `fun Companion.fromPolar2(radius: Double, theta: Double): Complex`
Either of these will create a complex number in
rectangular coordinates from the given values.

## Extension Functions
### on Double

- `fun round(decimals: Int): Double` (returns the
receiver rounded to the given number of places)
- `fun sqr(): Double` (returns receiver squared)
- `fun sqrt(): Complex` (returns the principal square
root, even of a negative real.)

### on Number

- `fun toComplex(): Complex` (returns the receiver as
a complex number, with imaginary part 0)
- `fun j(): Complex` (returns the receiver * sqrt(-1))

---

Distributed under the MIT License.
