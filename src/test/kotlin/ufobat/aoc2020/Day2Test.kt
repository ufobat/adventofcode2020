package ufobat.aoc2020

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class Day2Test {
    private val sut = Day2()

    @Test
    fun given_PuzzleData_then_ItIs1000Lines() {
        val linecount = sut.getPuzzleInput().count()
        assertEquals(1000, linecount)
    }

    @Test
    fun given_PuzzelInputAsSledRental_then_SomeAreValid() {
        val result = sut.countValidSledRentalPasswords()
        assertEquals(398, result)
    }

    @Test
    fun given_PuzzleInputAsTobogganRental_then_SomeAreValid() {
        val result = sut.countValidTobogganRentalPasswords()
        assertEquals(562, result)
    }

    /* Sled Rental */
    @Test
    fun given_FirstSledRentalSimpleCorrectInputLine_then_ItContainsAValidPassword() {
        val line = """1-3 a: abcde"""
        val result = sut.checkSledRentalPassword(line)
        assertTrue(result)
    }

    @Test
    fun given_SecondSledRentalSimpleFalseInputLine_then_ItContainsAInvalidPassword() {
        val line = """1-3 b: cdefg"""
        val result = sut.checkSledRentalPassword(line)
        assertFalse(result)
    }

    @Test
    fun given_ThirdSledRentalSimpleCorrectInputLine_then_ItContainsAValidPassword() {
        val line = """2-9 c: ccccccccc"""
        val result = sut.checkSledRentalPassword(line)
        assertTrue(result)
    }

    /* Toboggan Rental */
    @Test
    fun given_FirstTobogganRentalSimpleCorrectInputLine_then_ItContainsAValidPassword() {
        val line = """1-3 a: abcde"""
        val result = sut.checkTobogganRentalPassword(line)
        assertTrue(result)
    }

    @Test
    fun given_SecondTobogganRentalSimpleFalseInputLine_then_ItContainsAInvalidPassword() {
        val line = """1-3 b: cdefg"""
        val result = sut.checkTobogganRentalPassword(line)
        assertFalse(result)
    }

    @Test
    fun given_ThirdTobogganRentalSimpleCorrectInputLine_then_ItContainsAValidPassword() {
        val line = """2-9 c: ccccccccc"""
        val result = sut.checkTobogganRentalPassword(line)
        assertFalse(result)
    }

}