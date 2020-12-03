package ufobat.aoc2020

data class PwPolicyItem(val i1: Int, val i2: Int, val char: Char, val password: String)

class Day2 {
    private val pwRegex = """(\d+)-(\d+) (\w):\s+(.*)""".toRegex()

    fun getPuzzleInput() =
        this::class.java.getResourceAsStream("/day2_puzzle_input.txt")
            .bufferedReader()
            .lines()

    fun countValidSledRentalPasswords() = getPuzzleInput()
            .filter { checkSledRentalPassword(it) }
            .count()

    fun countValidTobogganRentalPasswords() = getPuzzleInput()
            .filter {
                val x = checkTobogganRentalPassword(it)
                x
            }
            .count()

    fun parsePolicyItem(line: String) : PwPolicyItem? {
        val result = pwRegex.find(line)
        return if (result != null) {
            PwPolicyItem(
                i1 = result.groupValues[1].toInt(),
                i2 = result.groupValues[2].toInt(),
                char = result.groupValues[3].toCharArray()[0],
                password = result.groupValues[4]
            )
        } else {
            null
        }
    }

    fun checkSledRentalPassword(line: String): Boolean {
        val policyItem = parsePolicyItem(line);
        return if (policyItem != null) {
            val count = policyItem.password
                .toCharArray()
                .filter { it == policyItem.char }
                .count()

            (count in policyItem.i1..policyItem.i2)
        } else {
            false
        }
    }

    fun checkTobogganRentalPassword(line: String): Boolean {
        val policyItem = parsePolicyItem(line);
        return if (policyItem != null) {
            val password = policyItem.password.toCharArray()
            val c = policyItem.char
            val c1 = password[policyItem.i1 - 1]
            val c2 = password[policyItem.i2 - 1]

            (( c1 == c && c2 != c) || (c1 != c && c2 == c))
        } else {
            throw IllegalArgumentException("WTF")
        }
    }
}