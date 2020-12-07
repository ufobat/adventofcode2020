package ufobat.aoc2020

import java.lang.IllegalStateException
import kotlin.collections.Map
import kotlin.streams.toList

class Day7 {

    fun getPuzzleInput(): List<String> =
            this::class.java.getResourceAsStream("/day7_puzzle_input.txt")
                    .bufferedReader()
                    .lines()
                    .toList()

//    fun getDemoInput(): List<String> =
//            this::class.java.getResourceAsStream("/day7_demo_input.txt")
//                    .bufferedReader()
//                    .lines()
//                    .toList()

    private val lineRegex = """^(.+) bags contain (.*)\.$""".toRegex()
    private val defRegex = """^(\d+) ([\w\s]+) bags?""".toRegex()
    private val splitRegex = """\, """.toRegex()

    fun parseBagRules(rules: List<String>): Map<String, Map<String, Int>> {

        return rules.map { rule ->
            val match = lineRegex.find(rule)
            if (match == null) throw IllegalArgumentException("can not read line: $rule")
            else {
                val bagType = match.groupValues[1]
                val definitions = match.groupValues[2]
                bagType to
                        if (definitions == "no other bags") mapOf()
                        else {
                            definitions
                                    .split(splitRegex)
                                    .mapNotNull {
                                        val result = defRegex.find(it)
                                        if (result != null) result.groupValues[2] to result.groupValues[1].toInt()
                                        else null
                                    }
                                    .associate { it }
                        }
            }
        }.associate { it }
    }

    fun findBagContainers(
            yourBag: String,
            bagRules: Map<String, Map<String, Int>>,
            result: MutableSet<String> = mutableSetOf()
    ): MutableSet<String> {
        bagRules
                .filter { (_, contains) -> contains.containsKey(yourBag) }
                .keys
                .forEach { bag ->
                    if (!result.contains(bag)) {
                        result.add(bag)
                        findBagContainers(bag, bagRules, result)
                    }
                }
        return result
    }

    fun countContainingBags(yourBag: String, bagRules: Map<String, Map<String, Int>>): Long =
            bagRules[yourBag]
                    ?.map { (newBag, count) -> count.toLong() * (countContainingBags(newBag, bagRules) + 1) }
                    ?.sum() ?: throw IllegalStateException("unknown bag $yourBag")
}

fun main() {
    val day = Day7()
    val bagRules = day.parseBagRules(day.getPuzzleInput())
    val myBag = "shiny gold"
    println(day.findBagContainers(myBag, bagRules).count())
    println(day.countContainingBags(myBag, bagRules))
}