package ufobat.aoc2020

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test


class Day1Test {

    private val sut = Day1()

    @Test
    fun given_DemoData_MultipliesTo() {
        // Arrange
        val expenses: List<Int> = listOf(
            1721,
            979,
            366,
            299,
            675,
            1456
        )

        // Act
        val result = sut.multiplyTwoMatchingExpanses(expenses)

        // Assert
        assertEquals(514579, result)
    }

    @Test
    fun given_PuzzleDataInResources_then_TheyCanBeRead() {
        // Act
        val list = sut.getPuzzleInput()

        // Assert
        assertEquals(200, list.size) // 200 elements
        assertEquals(2004, list[0]) // 1st element
    }

    @Test
    fun given_FirstPuzzleOfDay1_then_WeSolveIt() {
        // Act
        val wheee = sut.solveFirstTask()

        // Assert
        assertEquals(1020084, wheee)
    }

    @Test
    fun given_SecondPuzzleOfDay1_then_WeSolveIt() {
        // Act
        val wheee = sut.solveSecondTask()

        // Assert
        assertEquals(295086480, wheee)
    }
}