package ufobat.aoc2020

import kotlin.streams.toList

class Day9() {
    fun getDemoInput() =
        this::class.java.getResourceAsStream("/day9_demo_data.txt")
                .bufferedReader()
                .lines()
                .map { it.toLong() }
                .toList()

    fun getPuzzleInput() =
        this::class.java.getResourceAsStream("/day9_puzzle_data.txt")
            .bufferedReader()
            .lines()
            .map { it.toLong() }
            .toList()

    fun findFirstInvalidNumber(preambleLength: Int, data: List<Long>): Long {
        for (i in preambleLength until data.count()) {
            val num = data[i]
            val checkData = data.subList(i-preambleLength, i) // i is exclusive

            if (! combinationSums(checkData).contains(num)) {
                return num
            }
        }
        return -1
    }

    private fun combinationSums(data: List<Long>): List<Long> {
        val result = mutableListOf<Long>()
        for (i in 0 until data.count()) {
            for (j in i + 1 until data.count()) {
                result.add( data[j] + data[i] )
            }
        }
        return result.distinct().sorted()
    }

    fun findRangeThatSumsUpTo(invalidNumber: Long, data: List<Long>): Long {
        for (start in 0 until data.count()) {
            var curpos = start
            var sum = 0L
            val s = mutableListOf<Long>()
            while (sum < invalidNumber) {
                s.add(data[curpos])
                sum += data[curpos]
                if (sum == invalidNumber && curpos - start > 2) {
                    return (s.maxOrNull() ?: 0) + (s.minOrNull() ?: 0)
                }
                curpos++
            }
        }
        return -1
    }
}

fun main() {
    val day9 = Day9()
//    val demoData = day9.getDemoInput()
//    day9.findFirstInvalidNumber(5, demoData)

    val puzzleData = day9.getPuzzleInput()
    val invalidNumber = day9.findFirstInvalidNumber(25, puzzleData)
    val sumOfRange = day9.findRangeThatSumsUpTo(invalidNumber, puzzleData)
    println(invalidNumber)
    println(sumOfRange)
}