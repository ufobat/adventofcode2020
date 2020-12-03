package ufobat.aoc2020

import kotlin.streams.toList

class Day1 {
    fun multiplyTwoMatchingExpanses(expenses: List<Int>) : Int {
        expenses.forEach { a ->
            expenses.forEach { b ->
                if (a + b == 2020) {
                    return a * b
                }
            }
        }
        return 0
    }

    fun multiplyThreeMatchingExpanses(expenses: List<Int>) : Int {
        expenses.forEach { a ->
            expenses.forEach { b ->
                expenses.forEach { c ->
                    if (a + b + c == 2020) {
                        return a * b * c
                    }
                }
            }
        }
        return 0
    }

    fun getPuzzleInput() =
        this::class.java.getResourceAsStream("/day1_puzzle_input.txt")
            .bufferedReader()
            .lines()
            .map { it.toInt() }
            .toList()

    fun solveFirstTask() : Int {
        return multiplyTwoMatchingExpanses(getPuzzleInput())
    }

    fun solveSecondTask() : Int {
        return multiplyThreeMatchingExpanses(getPuzzleInput())
    }
}
