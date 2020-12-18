package ufobat.aoc2020
import kotlin.streams.toList

object Data18 {
    fun getPuzzleInput() = this::class.java.getResourceAsStream("/day18_puzzle_input.txt")
            .bufferedReader()
            .lines()
            .toList()

}

object ParenOpen { override fun toString() = "(" }
object ParenClose { override fun toString() = ")" }

val parenClose = Regex("^\\)")
val parenOpen = Regex("^\\(")
val operator = Regex("^[+\\-*/]")
val number = Regex("^\\d+")

fun tokenize(line: String): List<Any> {
    var data = line.replace("""\s+""".toRegex(), "")
    val tokens = mutableListOf<Any>()

    while (data.isNotEmpty()) {
        number.find(data)?.let {
            it.groupValues[0].let {
                data = data.substring(it.length)
                tokens.add(it.toLong())
            }
        }
        operator.find(data)?.let {
            it.groupValues[0].let {
                data = data.substring(it.length)
                tokens.add(it)
            }
        }
        parenClose.find(data)?.let {
            it.groupValues[0].let {
                data = data.substring(it.length)
                tokens.add(ParenClose)
            }
        }
        parenOpen.find(data)?.let {
            it.groupValues[0].let {
                data = data.substring(it.length)
                tokens.add(ParenOpen)
            }
        }

    }

    return tokens
}

fun main () {
    val data = Data18.getPuzzleInput()
    val sum1 = data.map { reduceTokens(tokenize(it), false) }.sum()
    val sum2 = data.map { reduceTokens(tokenize(it), true) }.sum()
    println(sum1)
    println(sum2)
}

fun reduceTokens(tokens: List<Any>, strichVorPunkt: Boolean = false): Long {
    var tokens = tokens
    while (tokens.size > 1) {
        tokens = reduceTokensStep(tokens, strichVorPunkt)
    }
    return tokens[0].toString().toLong()
}

fun reduceTokensStep(tokens: List<Any>, strichVorPunkt: Boolean): List<Any> {
    val reduced = mutableListOf<Any>()

    if (tokens.contains(ParenClose) && tokens.contains(ParenOpen)) {
        val popen = tokens.lastIndexOf(ParenOpen)
        val pclose = tokens.subList(popen, tokens.size).indexOf(ParenClose) + popen
        val sublist = tokens.subList(popen+1, pclose) // remove parenthesis

        reduced.addAll( tokens.subList(0, popen) )
        reduced.add( reduceTokens(sublist, strichVorPunkt) )
        reduced.addAll(tokens.subList(pclose+1, tokens.size))
    }
    else
    {
        if (tokens.size < 3) throw IllegalStateException("can not calculate this $tokens")

        if (strichVorPunkt) {
            var idx = if (tokens.contains("+")) tokens.indexOf("+")
            else tokens.indexOf('-')
            if (idx == -1) { idx = 1 } // no + or - then thake the op on 1
            reduced.addAll(tokens.subList(0, idx-1))
            reduced.add(calculate(tokens[idx-1] as Long, tokens[idx] as String, tokens[idx+1] as Long))
            reduced.addAll(tokens.subList(idx+2, tokens.size))
        } else {
            reduced.add(calculate(tokens[0] as Long, tokens[1] as String, tokens[2] as Long))
            reduced.addAll(tokens.subList(3, tokens.size))
        }
    }

    return reduced
}

fun calculate(n1: Long, op: String, n2: Long) = when (op) {
    "+" -> n1 + n2
    "-" -> n1 - n2
    "*" -> n1 * n2
    else -> n1 / n2
}
