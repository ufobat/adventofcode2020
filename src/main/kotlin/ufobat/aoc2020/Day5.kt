package ufobat.aoc2020

import kotlin.math.pow
import kotlin.streams.toList

private fun Int.pow(i: Int) :Int= this.toDouble().pow(i).toInt()

class BoardingPassBuilder(
        noOfRows: Int = 7,
        noOfCols :Int = 3
) {
    private var rowPos = noOfRows - 1
    private var colPos = noOfCols - 1

    private var row = 0
    private var col = 0

    fun configureSeatPosition(c: Char) {
        when (c) {
            'B' -> { row += 2.pow(rowPos--) }
            'F' -> { rowPos-- }
            'R' -> { col += 2.pow(colPos--) }
            'L' -> { colPos-- }
        }
    }

    fun build() = BoardingPass(row, col)
}

data class BoardingPass(
        val row: Int,
        val col: Int
) {
    val id: Int = row * 8 + col
}

class Day5 {

    fun getPuzzleInput() =
            this::class.java.getResourceAsStream("/day5_puzzle_input.txt")
                    .bufferedReader()
                    .lines()
                    .toList()

    fun parseBoardingPass(pass: String): BoardingPass {
        val builder = BoardingPassBuilder()
        pass.toCharArray().forEach { builder.configureSeatPosition(it) }
        return builder.build()
    }

    fun solveTask1(): BoardingPass? = getPuzzleInput()
            .map { parseBoardingPass(it) }
            .maxByOrNull { it.id }

    fun solveTask2(): Int? {
        val boardingPassList = getPuzzleInput()
                .map { parseBoardingPass(it) }
                .sortedBy { it.id }
        var firstId = boardingPassList.first().id

        boardingPassList.forEach {
            if (it.id != firstId){
                return firstId
            }
            firstId++
        }
        return null
    }
}

fun main() {
     Day5().solveTask2()
}