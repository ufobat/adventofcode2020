package ufobat.aoc2020

import kotlin.streams.toList

object Data16 {
        fun getPuzzleInput() = this::class.java.getResourceAsStream("/day16_puzzle_input.txt")
            .bufferedReader()
            .lines()
            .toList()

    fun getDemoData1() = listOf<String>(
            "class: 1-3 or 5-7",
            "row: 6-11 or 33-44",
            "seat: 13-40 or 45-50",
            "",
            "your ticket:",
            "7,1,14",
            "",
            "nearby tickets:",
            "7,3,47",
            "40,4,50",
            "55,2,20",
            "38,6,12",
    )

    fun getDemoData2() = listOf<String>(
            "class: 0-1 or 4-19",
            "row: 0-5 or 8-19",
            "seat: 0-13 or 16-19",
            "",
            "your ticket:",
            "11,12,13",
            "",
            "nearby tickets:",
            "3,9,18",
            "15,1,5",
            "5,14,9"

    )

}

class TrainTicketField(val name: String, val offset: Int, val ranges: List<IntRange>) {
    fun matches(i: Int): Boolean {
        return ranges.firstOrNull { i in it } != null
    }

    override fun toString(): String {
        return "TrainTicketField($name, $offset, ${ranges.toString()})"
    }
}


val ticketDefinitionRegex = """([\w\s]+): (\d+)-(\d+) or (\d+)-(\d+)""".toRegex()
val ticketDataLine = """^[\d\,]+$""".toRegex()

fun main() {

    val ticketFields = mutableListOf<TrainTicketField>()
    val ticketData = mutableListOf<List<Int>>()
    var defCnt = 0;
    Data16.getPuzzleInput().forEach { line ->
        ticketDefinitionRegex.find(line)?.let {
            val ranges = mutableListOf<IntRange>()
            ranges.add(it.groupValues[2].toInt() .. it.groupValues[3].toInt())
            ranges.add(it.groupValues[4].toInt() .. it.groupValues[5].toInt())
            ticketFields.add( TrainTicketField(it.groupValues[1], defCnt++, ranges) )
        }

        ticketDataLine.find(line)?.let {
            ticketData.add(line.split(',').map { it.toInt() })
        }
    }

    val falseData = mutableListOf<Int>()
    val validData = ticketData.filter { data ->
        data.map { value ->
            val fff = ticketFields.firstOrNull() { it.matches(value) }
            if (ticketFields.firstOrNull() { it.matches(value) } == null) {
                falseData.add(value)
                false
            } else {
                true
            }

        }.all { it }
    }

    println(falseData.sum())

    val checkSets = (0 until validData[0].count()).map {
        ticketFields.toMutableSet()
    }

    validData.forEach { data ->
        data.forEachIndexed {
            index, value ->
            // remove all that don't match
            checkSets[index].removeIf { ! it.matches(value) }
        }
    }

    var listOfIndexesToCheck = mutableListOf<Int>()

    while (true) {
        var indexWithOne = -1;
        var definitionWithOne: TrainTicketField? = null

        checkSets.forEachIndexed { index, it ->
            if (it.count() == 1) {
                definitionWithOne = it.first()
                indexWithOne = index
            }
        }

        if (indexWithOne != -1) {
            // println("found ${definitionWithOne?.name} at $indexWithOne")
            checkSets.forEach { it.removeIf { it.offset == definitionWithOne?.offset} }
            if (definitionWithOne!!.name.startsWith("departure"))
                listOfIndexesToCheck.add(indexWithOne)
        }
        else {
            break
        }
    }

    // my ticket == validData[0]

    var mul = 1L
    listOfIndexesToCheck.forEach {
        mul *= validData[0][it].toLong()
    }
    println(mul)
}