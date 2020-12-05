package ufobat.aoc2020

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day5Test {

    val sut = Day5()

    @Test
    fun given_FBFBBFFRLR_then_ParseTheBoardingPass() {
        val boardingPassConfiguration = "FBFBBFFRLR"

        val boardingPass = sut.parseBoardingPass(boardingPassConfiguration)

        assertEquals(44, boardingPass.row)
        assertEquals(5, boardingPass.col)
        assertEquals(357, boardingPass.id)
    }

    @Test
    fun given_BFFFBBFRRR_then_ParseTheBoardingPass() {
        val boardingPassConfiguration = "BFFFBBFRRR"

        val boardingPass = sut.parseBoardingPass(boardingPassConfiguration)

        assertEquals(70, boardingPass.row)
        assertEquals(7, boardingPass.col)
        assertEquals(567, boardingPass.id)
    }

    @Test
    fun given_FFFBBBFRRR_then_ParseTheBoardingPass() {
        val boardingPassConfiguration = "FFFBBBFRRR"

        val boardingPass = sut.parseBoardingPass(boardingPassConfiguration)

        assertEquals(14, boardingPass.row)
        assertEquals(7, boardingPass.col)
        assertEquals(119, boardingPass.id)
    }

    @Test
    fun given_Task1_then_WeSolveIt() {
        val boardingPass = sut.solveTask1()!!
        assertEquals(919, boardingPass.id)
    }

    @Test
    fun given_Task2_then_WeSolveIt() {
        val boardingPassId = sut.solveTask2()!!
        assertEquals(642, boardingPassId)
    }
}