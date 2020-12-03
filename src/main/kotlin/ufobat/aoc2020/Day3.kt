package ufobat.aoc2020

import kotlin.streams.toList

class Map(private val map: List<List<Boolean>>) {
    private val length = map.count()

    fun isTreeAt(line: Int, col: Int): Boolean {
        if (line > length) throw IllegalArgumentException("map end is reached")

        val lineData = map[line - 1]
        val pos = (col-1) % lineData.count()
        return lineData[pos]
    }

    fun countTreesInMyWay(right: Int, down: Int = 1): Long {
        var posX = 1
        // println("number of lines in the map $length")
        return (1..length step down)
                .map {
                    val isTree= isTreeAt(it, posX)
                    posX += right
                    isTree
                }
                .filter { it }
                .count()
                .toLong()
    }
}
class Day3 {

    fun getPuzzleInput() =
            this::class.java.getResourceAsStream("/day3_puzzle_input.txt")
            .bufferedReader()
            .lines()

    fun getDemoInput() =
            this::class.java.getResourceAsStream("/day3_demo_input.txt")
            .bufferedReader()
            .lines()

    fun parseMap(lines: List<String>): List<List<Boolean>> {
        return lines.map { line ->
            line
                    .toCharArray()
                    .map { char ->
                        when (char) {
                            '.'  -> false
                            '#'  -> true
                            else -> throw IllegalArgumentException("invalid char in map")
                        }
                    }
        }
    }

    fun getDemoMap(): Map {
        return Map(parseMap(getDemoInput().toList()))
    }

    fun getPuzzleMap(): Map {
        val lines = getPuzzleInput().toList()
        return Map(parseMap(lines))
    }

    fun solveTask1(): Long {
        val map = getPuzzleMap()
        return map.countTreesInMyWay(3,1)
    }

    fun solveTask2(): Long {
        val map = getPuzzleMap()
        return map.countTreesInMyWay(1, 1) *
               map.countTreesInMyWay(3,1) *
               map.countTreesInMyWay(5,1) *
               map.countTreesInMyWay(7,1) *
               map.countTreesInMyWay(1,2)
    }
}