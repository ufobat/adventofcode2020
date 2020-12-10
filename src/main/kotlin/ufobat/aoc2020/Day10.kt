package ufobat.aoc2020

import kotlin.streams.toList

object Data {
    fun getDemoInput() = this::class.java.getResourceAsStream("/day10_demo_input.txt")
            .bufferedReader()
            .lines()
            .map { it.toLong() }
            .toList()

    fun getPuzzleInput() = this::class.java.getResourceAsStream("/day10_puzzle_input.txt")
            .bufferedReader()
            .lines()
            .map { it.toLong() }
            .toList()

    fun getSmallDemoData() = listOf<Long>(16, 10, 15, 5, 1, 11, 7, 19, 6, 12, 4)
}

private fun MutableMap<Long, Int>.inc(k: Long) { this[k] = 1 + (this[k] ?: 0) }

fun calcDistribution() {
    val distribution = mutableMapOf<Long, Int>()
    var lastValue: Long = 0

    Data.getPuzzleInput()
            .sorted()
            .forEach {
                distribution.inc(it - lastValue)
                lastValue = it
            }

    // my device
    distribution.inc(3)

    println(distribution[1]!! * distribution[3]!!)
}

fun calcCombinations() {
    val data = mutableListOf<Long>().apply {
        this.add(0)
        this.addAll( Data.getPuzzleInput().sorted() )
        this.add(this.last() +3 )
    }.toList()

    val dependsOn = mutableMapOf<Long, List<Long>>()
    val reversed = data.reversed()

    for (index in 0 until reversed.count()) {
        val jolts = reversed[index]
        val pos = mutableListOf<Long>()
        for (checkIdx in 1 .. 3) {
            val checkMe = reversed.getOrNull(index + checkIdx)
            if (checkMe != null && jolts - checkMe <= 3) { pos.add(checkMe) }
        }
        dependsOn[jolts] = pos.toList()
    }

    val variants = mutableMapOf<Long, Long>()
    val jolts = reversed[0]
    numOfVariants(jolts, variants, dependsOn)
    println( variants[data.last()] )
}

fun numOfVariants(jolts: Long,
                  variants: MutableMap<Long, Long>,
                  dependsOn: MutableMap<Long, List<Long>>): Long =
        variants.getOrPut(jolts) { dependsOn[jolts]!!
                .map { numOfVariants(it, variants, dependsOn) }
                .sum()
                .let { if (it == 0L) 1 else it }
        }

fun main() {
    calcDistribution()
    calcCombinations()
}