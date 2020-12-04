package ufobat.aoc2020

class Day4 {

    private val requiredFields = listOf(
            "byr",
            "iyr",
            "eyr",
            "hgt",
            "hcl",
            "ecl",
            "pid",
            // ,"cid:"
    )

    fun getPuzzleInput() =
            this::class.java.getResource("/day4_puzzle_input.txt").readText()

    fun getDemoInputTask1() =
            this::class.java.getResource("/day4_demo_input.txt").readText()

    fun getDemoInputTask2() =
            this::class.java.getResource("/day4_demo_input_task2.txt").readText()


    fun validateTask1(data: String) : Int {
        val passports = data.split("""\n\n""".toRegex())
        println(passports.count())

        return passports
                .map { passport ->
                    requiredFields
                            .map { "\\b$it:(\\S+)".toRegex() }
                            .map { it.find(passport) == null }
                            .count { it } == 0
                }
                .count { it -> it }
    }

    fun validateTask2(data: String) : Int {
        val passports = data.split("""\n\n""".toRegex())
        println(passports.count())

        return passports
                .map { passport ->
                    requiredFields
                            .map { isFieldInvalid(it, passport ) }
                            .count { it } == 0
                }
                .count { it -> it }
    }

    private fun isFieldInvalid(field: String, passport: String) : Boolean {
        val regex = "\\b$field:(\\S+)".toRegex()
        val value = regex.find(passport) ?: return true

        val groupValue = value.groupValues[1]
        return when(field) {
            "byr" -> groupValue.toInt() < 1920 || groupValue.toInt() > 2002
            "iyr" -> groupValue.toInt() < 2010 || groupValue.toInt() > 2020
            "eyr" -> groupValue.toInt() < 2020 || groupValue.toInt() > 2030
            "hgt" -> {
                val result = """(\d+)(in|cm)""".toRegex().find(groupValue) ?: return true
                val height = result.groupValues[1].toInt()
                val unit = result.groupValues[2]
                when (unit) {
                    "cm" -> height < 150 || height > 193
                    "in" -> height < 59 || height > 76
                    else -> true
                }
            }
            "hcl" -> ! groupValue.matches("""^#[0-9a-f]{6}$""".toRegex())
            "ecl" -> ! groupValue.matches("""^(amb|blu|brn|gry|grn|hzl|oth)$""".toRegex())
            "pid" -> ! groupValue.matches("""^\d{9}$""".toRegex())
            else  -> true
        }
    }


    fun solveDemoDataTask1() = validateTask1(getDemoInputTask1())
    fun solvePuzzleDataTask1() = validateTask1(getPuzzleInput())
    fun solvePuzzleDataTask2() = validateTask2(getPuzzleInput())
}

fun main() {
    val day4 = Day4()
    println(day4.solvePuzzleDataTask2())

}