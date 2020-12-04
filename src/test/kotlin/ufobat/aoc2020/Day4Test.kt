package ufobat.aoc2020

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day4Test {

    private val sut = Day4()

    @Test
    fun given_DemoData_then_Task1() {
        val result = sut.solveDemoDataTask1()
        assertEquals(2, result)
    }

    @Test
    fun given_PuzzleData_then_Task1() {
        val result = sut.solvePuzzleDataTask1()
        assertEquals(256, result)
    }

    @Test
    fun given_PuzzleData_then_Task2() {
        val result = sut.solvePuzzleDataTask2()
        assertEquals(198, result)
    }
}