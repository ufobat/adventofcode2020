package ufobat.aoc2020

import kotlin.collections.Map
import kotlin.streams.toList

object Data21 {
    fun getPuzzleInput() = this::class.java.getResourceAsStream("/day21_puzzle_input.txt")
            .bufferedReader()
            .lines()
            .toList()

    fun getDemoInput() = this::class.java.getResourceAsStream("/day21_demo_input.txt")
            .bufferedReader()
            .lines()
            .toList()

}

fun parseIngredientsAndAllergenes(list: List<String>): Map<List<String>, List<String>> {
    return list.associate {
        var (ingredients, allergens) = it.split(" (contains ")
        allergens = allergens.substring(0, allergens.length-1)
        ingredients.split(' ') to allergens.split(""", """)
    }
}

fun main() {
    val data= parseIngredientsAndAllergenes(Data21.getPuzzleInput())

    val allergenMapping = mutableMapOf<String, Set<String>>()
    val save = mutableMapOf<String, Int>()
    data.forEach { (i, a) ->
        i.forEach { save[it] = save.getOrDefault(key = it, defaultValue = 0) + 1 }
        a.forEach {
            if (!allergenMapping.contains(it)) { allergenMapping[it] = i.toSet() }
            else { allergenMapping[it] = allergenMapping[it]!!.intersect(i.toSet()) }
        }
    }

    allergenMapping.forEach { a, ingredients ->
        ingredients.forEach { save.remove(it) }
    }

    println(save.values.sum())

    do {
        allergenMapping.forEach { a, set ->
            if (set.size == 1) {
                allergenMapping.forEach { oa, oset ->
                    if (a != oa) {
                        allergenMapping[oa] = allergenMapping[oa]!!.subtract(allergenMapping[a]!!)
                    }
                }
            }
        }
    }while (allergenMapping.values.filter { it.size > 1 }.count() > 0)

    println( allergenMapping.mapValues { it.value.first() }
                     .toSortedMap()
                     .values
                     .joinToString(",",)
    )

//  while False in [len(x) == 1 for x in allergen_mapping.values()]:

}