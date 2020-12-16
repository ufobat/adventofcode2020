package ufobat.aoc2020

object Data15 {
    fun demoData() = listOf<Int>(0,3,6)
    fun puzzleInput() = listOf<Int>(15,12,0,14,3,1)

}

fun main() {
    val numberToLastTurn = mutableMapOf<Int, Int>()
    val data = Data15.puzzleInput().toMutableList()

    var currentNumber = 0
    for (turn in 0 until 30000000) {
        val nextNumber = calcNextNumber(currentNumber, numberToLastTurn, data, turn)
        currentNumber = nextNumber
    }

    // println(numberToLastTurn)
}

fun calcNextNumber(currentNumber: Int, numbers: MutableMap<Int, Int>, data: MutableList<Int>, turn: Int): Int {
    val nextNumber = when {
        data.size > 0                      -> data.removeFirst()
        numbers.containsKey(currentNumber) -> turn - numbers[currentNumber]!!
        else                               -> 0
    }

    println("${turn+1} -> $nextNumber")

    numbers[currentNumber] = turn

    return nextNumber
}
