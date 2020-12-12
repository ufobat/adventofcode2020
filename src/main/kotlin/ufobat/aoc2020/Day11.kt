package ufobat.aoc2020

import kotlin.streams.toList

object Data11 {
    fun getDemoInput() = this::class.java.getResourceAsStream("/day11_demo_input.txt")
            .bufferedReader()
            .lines()
            .map { it.toCharArray() }
            .toList().toTypedArray()

    fun getPuzzleInput() = this::class.java.getResourceAsStream("/day11_puzzle_input.txt")
            .bufferedReader()
            .lines()
            .map { it.toCharArray() }
            .toList().toTypedArray()
}

fun main() {
    println("task1: ")
    task1()
    println("task2: ")
    task2()
}

fun task2() {
    val layout = Data11.getPuzzleInput()
    var nextLayout = nextStep(layout, 2)

    var oldCount = countOccupiedSeats(layout)
    var newCount = countOccupiedSeats(nextLayout)

    while (oldCount != newCount) {
        nextLayout = nextStep(nextLayout, 2)
        oldCount = newCount
        newCount = countOccupiedSeats(nextLayout)
    }
    println(newCount)

}

fun task1() {
    val layout = Data11.getPuzzleInput()
    var nextLayout = nextStep(layout, 1)

    var oldCount = countOccupiedSeats(layout)
    var newCount = countOccupiedSeats(nextLayout)
    while (oldCount != newCount) {
        nextLayout = nextStep(nextLayout, 1)
        oldCount = newCount
        newCount = countOccupiedSeats(nextLayout)
    }
    println(newCount)
}

fun nextStep(layout: Array<CharArray>, rules: Int) : Array<CharArray> {
    val lineRange = 0 until layout.count()
    val rowRange = 0 until layout[0].count()

    val nextLayout = Array<CharArray>(layout.count()) { CharArray(layout[0].count())}

    for (i in lineRange) {
        for (j in rowRange) {
            if (rules == 1)
                nextLayout[i][j] = rulesTask1(layout, i, j)
            else
                nextLayout[i][j] = rulesTask2(layout, i, j)
        }
    }
    return nextLayout
}

fun rulesTask1(layout: Array<CharArray>, i: Int, j: Int): Char {
    var newChar = layout[i][j]
    val n = checkNeighborsOccupied(layout, i, j)

    // If a seat is empty (L) and there are no occupied seats adjacent to it, the seat becomes occupied.
    // If a seat is occupied (#) and four or more seats adjacent to it are also occupied, the seat becomes empty.
    when (layout[i][j]) {
        'L' ->                     if (n == 0) newChar = '#'
        '#' ->                     if (n > 3) newChar = 'L'
    }
    return newChar
}

fun rulesTask2(layout: Array<CharArray>, i: Int, j: Int): Char {
    var newChar = layout[i][j]
    val n = checkDirectoriesOccupied(layout, i, j)

    // If a seat is empty (L) and there are no occupied seats adjacent to it, the seat becomes occupied.
    // If a seat is occupied (#) and four or more seats adjacent to it are also occupied, the seat becomes empty.
    when (layout[i][j]) {
        'L' ->                     if (n == 0) newChar = '#'
        '#' ->                     if (n > 4) newChar = 'L'
    }
    return newChar
}


fun countOccupiedSeats(layout: Array<CharArray>) : Int {
    var cnt = 0
    val lineRange = 0 until layout.count()
    val rowRange = 0 until layout[0].count()
    for (i in lineRange) {
        for (j in rowRange) {
            if (layout[i][j] == '#') cnt++
        }
    }
    return cnt
}

fun checkNeighborsOccupied(layout: Array<CharArray>, i: Int, j: Int): Int {
    val lineRange = 0 until layout.count()
    val rowRange = 0 until layout[0].count()

    var cnt = 0
    for (iOffset in -1 .. 1) {
        for (jOffset in -1 .. 1) {
            if (jOffset == 0 && iOffset == 0) continue

            val iPos = i + iOffset
            val jPos = j + jOffset

            if (iPos in lineRange && jPos in rowRange && layout[iPos][jPos] == '#') cnt ++
        }
    }
    return cnt
}

fun checkDirectoriesOccupied(layout: Array<CharArray>, i: Int, j: Int): Int {
    val lineRange = 0 until layout.count()
    val rowRange = 0 until layout[0].count()
    var cnt = 0

    for (iDirection in -1 .. 1) {
        for (jDirection in -1 .. 1) {
            if (jDirection == 0 && iDirection == 0) continue
            var jPos = j
            var iPos = i
            while (true) {
                iPos += iDirection
                jPos += jDirection
                if (iPos !in lineRange || jPos !in rowRange) break
                if (layout[iPos][jPos] == '#') cnt++
                if (layout[iPos][jPos] != '.') break
            }
        }
    }
    return cnt
}

fun printLayout(layout: Array<CharArray>) = layout.forEach {
    it.forEach { print(it) }
    println("")
}

