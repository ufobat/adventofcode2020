package ufobat.aoc2020

class Day6 {
    fun getPuzzleInput() =
            this::class.java.getResource("/day6_puzzle_input.txt").readText()

    fun getDemoInputTask1() =
            this::class.java.getResource("/day6_demo_input.txt").readText()


    fun processDataTask1(input: String): Int =
            input
                .split("""\n\n""".toRegex())
                .map { block -> block.toCharArray().filter { it in 'a'..'z' }.toHashSet().count() }
                .sum()

    fun processDataTask2(input: String): Int =
            input
                    .split("""\n\n""".toRegex())
                    .map { it ->
                        val map = mutableMapOf<Char, Int>()
                        var memberPerGroup = 1
                        it.toCharArray().forEach { c ->
                            if (c in 'a'..'z') map[c] = 1 + (map[c] ?: 0)
                            if (c == '\n') { memberPerGroup++ }
                        }

                        map.filter { (_, value) -> value == memberPerGroup }.count()
                    }
                    .sum()
}

fun main() {
    val day6 = Day6()
    val data = day6.getPuzzleInput()
    println(day6.processDataTask1(data))
    println(day6.processDataTask2(data))
}