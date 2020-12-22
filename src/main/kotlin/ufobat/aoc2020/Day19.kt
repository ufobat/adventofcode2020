package ufobat.aoc2020
import kotlin.collections.Map
import kotlin.streams.toList

object Data19 {
    fun getPuzzleInput() = this::class.java.getResourceAsStream("/day19_puzzle_input.txt")
            .bufferedReader()
            .lines()
            .toList()
    fun getPuzzleInput2() = this::class.java.getResourceAsStream("/day19_puzzle_input_task2.txt")
            .bufferedReader()
            .lines()
            .toList()


    fun getDemoData() = listOf<String>(
            "0: 4 1 5",
            "1: 2 3 | 3 2",
            "2: 4 4 | 5 5",
            "3: 4 5 | 5 4",
            "4: \"a\"",
            "5: \"b\"",
            "",
            "ababbb",
            "bababa",
            "abbbab",
            "aaabbb",
            "aaaabbb",
    )
}

fun main() {
    val input = Data19.getPuzzleInput2()
    val (rules, data) = parseRuleData(input)
    println(rules.size)
    println(data.size)

    val regex = rulesToRegex(rules)

    println(data.filter { it.matches(regex) }.count())
//    data.forEach {
//        val r = it.matches(regex)
//        println("$it -> $r")
//    }
}

fun rulesToRegex(rules: Map<String, String>): Regex {
    // remove "
    var currentRules: Map<String, String?> = rules.mapValues { (k, v) ->
        v.replace("\"", "")
    }

    val parsedRules = mutableMapOf<String, String>()
    val numberRegex = """\d+""".toRegex()

    while (currentRules["0"] != null) {
        // map incomplete rules
        // keep the complete ones
        currentRules = currentRules.mapValues { (idx, rule) ->
            if (rule != null) {
                var newrule = rule!!
                if (newrule.contains(numberRegex)) {
                    numberRegex.findAll(newrule).forEach {
                        val number = it.value
                        val replacement = parsedRules[number]
                        if (replacement != null) {
                            newrule = newrule.replace("\\b$number\\b".toRegex(), replacement)
                        }
                    }
                    newrule
                } else {
                    // keep it
                    parsedRules[idx.toString()] = if (rule.contains('|')) {
                        "(?:" + rule.replace(" ", "") + ")"
                    } else {
                        rule.replace(" ", "")
                    }

                    null
                }
            } else {
                null
            }
        }

        println("current rules:")
        println(currentRules)
        println("parsed rules: ")
        println(parsedRules)
        println("")
//        Thread.sleep(2000)
    }
    parsedRules.toSortedMap().forEach {
        println(it)
    }
    println(parsedRules["0"])
    return parsedRules["0"]!!.toRegex()
}

fun parseRuleData(input: List<String>): Pair<Map<String, String>, MutableList<String>> {
    val rules = mutableMapOf<String, String>()
    val data = mutableListOf<String>()
    val regex = Regex("""^(\d+): (.*)$""")

    input.forEach {
        val result = regex.find(it)
        if( result != null ) rules[result.groupValues[1]] = result.groupValues[2]
        else if (it.isEmpty()) {}
        else data.add(it)
    }
    return rules to data
}
