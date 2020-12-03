package ufobat.aoc2020

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class Day3Test {
    private val sut = Day3()

    @Test
    fun given_PuzzleMap_then_CheckTrees() {
        val map = sut.getPuzzleMap()

        assertFalse(map.isTreeAt(1,1))
        assertFalse(map.isTreeAt(1,4))
        assertTrue(map.isTreeAt(1,5))
        assertTrue(map.isTreeAt(1,6))
        assertTrue(map.isTreeAt(1,30))
        assertTrue(map.isTreeAt(1,31))

        // end of map
        assertFalse(map.isTreeAt(1,32))
        assertFalse(map.isTreeAt(1,35))
        assertTrue(map.isTreeAt(1,36))
        assertTrue(map.isTreeAt(1,37))
        assertTrue(map.isTreeAt(1,61))
        assertTrue(map.isTreeAt(1,62))
    }

    @Test
    fun given_PuzzleMap_then_SolveTaskStepByStep1() {
        val map = sut.getPuzzleMap()
        val result = map.countTreesInMyWay(3)
        assertEquals(164, result)
    }

    @Test
    fun given_PuzzleMap_then_SolveTask1() {
        val result = sut.solveTask1()
        assertEquals(164, result)
    }

    @Test
    fun given_PuzzleMap_then_SolveTaskStepByStep2() {
        val map = sut.getPuzzleMap()

//        println("right 1, down 1, noOfTrees " + map.countTreesInMyWay(1, 1))
//        println("right 3, down 1, noOfTrees " + map.countTreesInMyWay(3, 1))
//        println("right 5, down 1, noOfTrees " + map.countTreesInMyWay(5, 1))
//        println("right 7, down 1, noOfTrees " + map.countTreesInMyWay(7, 1))
//        println("right 1, down 2, noOfTrees " + map.countTreesInMyWay(1, 2))

        val result =
                map.countTreesInMyWay(1, 1) *
                map.countTreesInMyWay(3,1) *
                map.countTreesInMyWay(5,1) *
                map.countTreesInMyWay(7,1) *
                map.countTreesInMyWay(1,2)

        assertEquals(5007658656, result)
    }

    @Test
    fun given_PuzzleMap_then_SolveTask2() {
        val result = sut.solveTask2()
        assertEquals(5007658656, result)
    }

    @Test
    fun given_DemoMap_then_SolveTask2() {
        /*
        Right 1, down 1.
        Right 3, down 1. (This is the slope you already checked.)
        Right 5, down 1.
        Right 7, down 1.
        Right 1, down 2.
         */
        val map = sut.getDemoMap()

        val result =
                map.countTreesInMyWay(1, 1) *
                map.countTreesInMyWay(3,1) *
                map.countTreesInMyWay(5,1) *
                map.countTreesInMyWay(7,1) *
                map.countTreesInMyWay(1,2)


        assertEquals(336, result)
    }
}