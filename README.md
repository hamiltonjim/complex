# Complex.kt
### Copyright © 2025 Jim Hamilton
## A Kotlin class (and extension functions) to handle complex math.

Usage: simply include Complex.kt in your project.

To create a complex val, just pass the real and 
imaginary parts to the constructor for Complex. 
```kotlin
    val num = Complex(1.5, 3.25)
```
Both parts are notionally Double, but you may 
pass both parts as Long, Int, or Float; or one 
Double and one other type.
```kotlin
    val withInts = Complex(1, 4)
    val withLongs = Complex(3L, 1L)
    val withOneInt = Complex(3.1, 4)
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

There are operator overloads for plus, minus, times,
and div, that work for Complex op Complex, and any
combination of Complex and Double, Float, Int, and
Long.

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
Floating Point](http://docs.sun.com/source/806-3568/ncg_goldberg.html)

The following functions are defined:
- polar(): returns magnitude and angle as a Pair
- abs(): Double (returns the magnitude of this)
- arg(): Double (returns the angle in radians,
  counter-clockwise from the real axis)
- exp(): Complex (returns e^this)
- ln(): Complex (returns the principal value of the natural logarithm--
remember polar coordinates are periodic)
- sqrt(): Complex (returns principal square root of this)
- Double.sqrt(): Complex (replaces the standard lib function, 
works on negative numbers)
- isNaN(): Boolean (returns true if either part is NaN)
- isInfinite(): Boolean (returns true if either part is 
infinite and neither part is NaN)
- isZero(): Boolean (returns true if both parts
are close enough to zero to PRINT zero when formatted)
- pow(exponent: Complex): Complex and
- pow(exponent: Double): Complex (to take this to
any arbitrary power)
- isReal(): Boolean (returns true if imaginary
part is zero)
- isImaginary(): Boolean (returns true if real
part is zero)
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

Distributed under the MIT License which is included
in the repository.