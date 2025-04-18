package xyz.jimh.complex

import kotlin.math.E
import kotlin.math.PI

fun main() {
    val real = -1.0
    val complex = real.sqrt()

    println("sqrt(-1) is $complex")

    println("sqrt(j) is ${complex.sqrt()}   -sqrt(j) is ${(-complex).sqrt()}")
    println("rounded to 3 places: " +
            "${complex.sqrt().round(3)}   ${(-complex).sqrt().round(3)}")

    val cmp = 3.0 + 4.0.j()
    val cmp2 = Complex(3.0, 4.0)
    println("real + imag 3 + ${4.0.j()} == Complex(3, 4)? ${cmp == cmp2}")
    println("$cmp * $complex: ${cmp * complex}")
    println("$cmp / $complex: ${cmp / complex}")
    println("$cmp + $complex: ${cmp + complex}")
    println("$cmp - $complex: ${cmp - complex}")
    println("sqrt($cmp) : ${cmp.sqrt()}")

    val sq = Complex(2, 1)
    println("($sq)^2 is ${sq * sq}")

    val z1 = Complex(3, 4)
    val base = Complex(1, 1)

    println(base)
    println(base.pow(z1))
    println(Complex(2, 2).pow(Complex(2,-2)))
    println(Complex(2, 2) pow Complex(2,-2))
    println(Complex(2, 2) pow Complex.J)
    println(Complex.J pow Complex.J)
    println(Complex(5, 10) / Complex.ZERO)
    println(Complex(5, 10) / 0)
    println(Complex(Double.POSITIVE_INFINITY))
    println(Complex(3) / Complex(9, 6))
    println(Complex(E) pow Complex(0, PI))
    println(expITheta(PI))
    println(Complex(0, PI).exp())

    println("---trig & hyp functions---")
    val halfPi = PI / 2
    println(Complex(halfPi).sin())
    println(Complex(halfPi).sinh())
    println(Complex(halfPi).cos())
    println(Complex(halfPi).cosh())
    println(Complex(halfPi).tan())
    println(Complex(halfPi).tanh())
    println(Complex(halfPi).cot())
    println(Complex(halfPi).coth())
    println(Complex(halfPi).sec())
    println(Complex(halfPi).sech())
    println(Complex(halfPi).csc())
    println(Complex(halfPi).csch())

    println(Complex.ZERO.acsc())
    println(Complex.ZERO.asec())

    println((Complex.PI_C / 2.0).cos())

    val cmplex = Complex(-4, 4)
    val (radius, theta) = cmplex.polar()
    println("$cmplex has radius $radius and angle $theta (or ${theta * 180 / PI}°)")

    println(Complex.PI_C.toDouble())
    try {
        PI.j().toBigDecimal()
    } catch (e: ArithmeticException) {
        e.printStackTrace()
    }

}

