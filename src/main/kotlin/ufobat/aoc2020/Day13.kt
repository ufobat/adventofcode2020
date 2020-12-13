package ufobat.aoc2020

import java.math.BigInteger

object Data13 {
    val puzzleEarliest = 1000052
    val puzzleBusIds = "23,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,37,x,x,x,x,x,863,x,x,x,x,x,x,x,x,x,x,x,19,13,x,x,x,17,x,x,x,x,x,x,x,x,x,x,x,29,x,571,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,41"

    val demoEarliest = 939
    val demoBusIds = "7,13,x,x,59,x,31,19"
}

fun findNextBus() {
    val earliest = Data13.puzzleEarliest
    val busIds = Data13.puzzleBusIds
        .split(',')
        .filter { it != "x" }
        .map { it.toInt() }

    val firstBus = busIds
        .map { id -> id to id - earliest % id }
        .sortedBy { it.second }
        .first()

    println(firstBus)
    println(firstBus.first * firstBus.second)
}

fun main() {
    var busesToNext = Data13.puzzleBusIds
        .split(',')
        .mapIndexed { index, s -> s to index }
        .filter { it.first != "x" }
        .map { it.first.toInt() to it.second }

    println(busesToNext)
    // shift to last bus
    busesToNext = toRequirements(busesToNext)
    println(busesToNext)

    //https://de.wikipedia.org/wiki/Chinesischer_Restsatz
    var M = BigInteger.ONE
    busesToNext.map { it.first }.forEach { M *= it.toBigInteger() }

    val whatever = busesToNext.map { (m, a) ->
        val Mcur = M / m.toBigInteger()
        val r = extended_gcd(Mcur, m.toBigInteger())
        // gcd == r[0] && r[1] == r1 && r[2] == s1
        // println("${r[1]} * $m + ${r[2]} * $Mcur  = 1")
        a.toBigInteger() * Mcur * r[2]
    }
    println(whatever)
    val sum = whatever.reduce { acc, bigInteger -> acc + bigInteger }

    // shift to first bus
    println(sum.mod(M) - busesToNext.first().second.toBigInteger())
}

fun toRequirements(busesToNext: List<Pair<Int, Int>>): List<Pair<Int, Int>> {
    val max = busesToNext.last().second
    return busesToNext.map { (a, b) -> a to max - b }
}

fun extended_gcd(ap: BigInteger, bp: BigInteger): List<BigInteger> {
//    (old_r, r) := (a, b)
//    (old_s, s) := (1, 0)
//    (old_t, t) := (0, 1)
    var r = listOf(ap, bp)
    var s = listOf(BigInteger.ONE, BigInteger.ZERO)
    var t = listOf(BigInteger.ZERO, BigInteger.ONE)

    while (r[1] != BigInteger.ZERO) {
//        quotient := old_r div r
        val quotient = r[0] / r[1]
//        (old_r, r) := (r, old_r − quotient × r)
        r = listOf(r[1], r[0] - quotient * r[1])
//        (old_s, s) := (s, old_s − quotient × s)
        s = listOf(s[1], s[0] - quotient * s[1])
//        (old_t, t) := (t, old_t − quotient × t)
        t = listOf(t[1], t[0] - quotient * t[1])
    }
    // println("gcd " + r[0])
    // val bt = r[0] - bp * s[0] / ap
    return listOf(r[0], t[0], s[0])
}


//function extended_gcd(a, b)
//    (old_r, r) := (a, b)
//    (old_s, s) := (1, 0)
//    (old_t, t) := (0, 1)
//
//    while r ≠ 0 do
//        quotient := old_r div r
//        (old_r, r) := (r, old_r − quotient × r)
//        (old_s, s) := (s, old_s − quotient × s)
//        (old_t, t) := (t, old_t − quotient × t)
//
//    output "Bézout coefficients:", (old_s, old_t)
//    output "greatest common divisor:", old_r
//    output "quotients by the gcd:", (t, s)

fun extgcd(ap: Long, bp: Long): List<BigInteger> {
    // u, v, s, t = 1, 0, 0, 1
    var u = 1.toBigInteger()
    var v = 0.toBigInteger()
    var s = 0.toBigInteger()
    var t = 1.toBigInteger()

    var a = ap.toBigInteger()
    var b = bp.toBigInteger()
    while (b != BigInteger.ZERO) {
        val q = a / b

        // a, b = b, a-q*b
        val ao = a
        val bo = b
        a = bo
        b = ao - q*bo

        // u, s = s, u-q*s
        val so = s
        val uo = u
        u = so
        s = uo - q*so

        // v, t = t, v-q*t
        val to = t
        val vo = v
        v = to
        t = vo - q*to
    }
    // println("ggt $a $b $t $v $u $s")
    return listOf(a, u, v)
}
//def extgcd(a, b):
//    u, v, s, t = 1, 0, 0, 1
//    while b!=0:
//        q=a//b
//        a, b = b, a-q*b
//        u, s = s, u-q*s
//        v, t = t, v-q*t
//    return a, u, v