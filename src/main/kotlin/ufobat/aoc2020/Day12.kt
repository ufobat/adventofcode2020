package ufobat.aoc2020

import kotlin.math.absoluteValue
import kotlin.streams.toList

object Data12 {
    fun getDemoInput() = this::class.java.getResourceAsStream("/day12_demo_input.txt")
            .bufferedReader()
            .lines()
            .toList()

    fun getPuzzleInput() = this::class.java.getResourceAsStream("/day12_puzzle_input.txt")
            .bufferedReader()
            .lines()
            .toList()
}

data class FerryTask1(var xPos: Int = 0, var yPos: Int = 0, var facing: Int = 0) {
    private val directions = listOf(
            "E" to 0,
            "N" to 1,
            "W" to 2,
            "S" to 3)

    private fun directionToInt(dir: String) = directions.first { it.first == dir }.second

    fun move(direction: String, steps: Int) = move(directionToInt(direction), steps)

    private fun move(direction: Int, steps: Int) {
        when(direction) {
            0 -> xPos += steps
            1 -> yPos += steps
            2 -> xPos -= steps
            3 -> yPos -= steps
        }
    }

    fun moveForward(steps: Int) = move(facing, steps)

    fun turn(leftOrRight: String, degree: Int) {
        // + directions.count() because i don't want negatives
        val turns = (degree / 90) * if (leftOrRight == "L") 1 else -1
        facing = (facing + turns + directions.count()) % directions.count()
    }

    fun whereAmI() {
        val manhattanDistance = xPos.absoluteValue + yPos.absoluteValue
        println("I am at $xPos, $yPos facing $facing with a manhatten distance of $manhattanDistance")
    }
}

class FerryTask2(
        xWaypoint: Int, yWaypoint: Int,
        private var xPos: Int = 0, private var yPos: Int = 0
) {
    private val waypoint = Waypoint(xWaypoint, yWaypoint)

    fun whereAmI() {
        val manhattanDistance = xPos.absoluteValue + yPos.absoluteValue
        println("I am at $xPos, $yPos with a manhatten distance of $manhattanDistance Waypoint(${waypoint.xPos}, ${waypoint.yPos})" )
    }

    fun turnWaypoint(first: String, second: Int) = waypoint.turn(first, second)
    fun moveWaypoint(first: String, second: Int) = waypoint.move(first, second)

    fun move(times: Int) {
        xPos += waypoint.xPos * times
        yPos += waypoint.yPos * times
    }
}
data class Waypoint(var xPos: Int = 0, var yPos: Int = 0) {
    private val directions = listOf(
            "E" to 0,
            "N" to 1,
            "W" to 2,
            "S" to 3)

    private fun directionToInt(dir: String) = directions.first { it.first == dir }.second

    fun move(direction: String, steps: Int) = when(directionToInt(direction)) {
        0 -> xPos += steps
        1 -> yPos += steps
        2 -> xPos -= steps
        3 -> yPos -= steps
        else  -> throw IllegalArgumentException("unknown direction")
    }

    fun turn(leftOrRight: String, degree: Int) {
        // + directions.count() because i don't want negatives
        val turns = (degree / 90) * if (leftOrRight == "L") 1 else -1
        val posTurns = (turns + 4) % 4

        // rotate 90 for turns times
        (0 until posTurns).forEach {
            val xOld = xPos
            val yOld = yPos
            xPos = - yOld
            yPos = xOld
        }
    }
}


fun main() {
    val instructions = Data12.getPuzzleInput()
            .map { it.substring(0, 1) to  it.substring(1).toInt() }

    val ferry = FerryTask1()
    instructions.forEach {
        when(it.first) {
            "N", "E", "S", "W" -> ferry.move(it.first, it.second)
            "F" -> ferry.moveForward(it.second)
            "L", "R" -> ferry.turn(it.first, it.second)
            else -> throw IllegalArgumentException("unknown command")
        }
    }

    ferry.whereAmI()

    val ferryWithWaypoint = FerryTask2(10, 1)
    instructions.forEach {
        when(it.first) {
            "N", "E", "S", "W" -> ferryWithWaypoint.moveWaypoint(it.first, it.second)
            "F" -> ferryWithWaypoint.move(it.second)
            "L", "R" -> ferryWithWaypoint.turnWaypoint(it.first, it.second)
            else -> throw IllegalArgumentException("unknown command")
        }
    }

    ferryWithWaypoint.whereAmI()
}



