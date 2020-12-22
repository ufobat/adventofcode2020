package ufobat.aoc2020
import kotlin.streams.toList

object Data22 {
    fun getPuzzleInput() = this::class.java.getResourceAsStream("/day22_puzzle_input.txt")
            .bufferedReader()
            .lines()
            .toList()

    fun getDemoInput() = this::class.java.getResourceAsStream("/day22_demo_input.txt")
            .bufferedReader()
            .lines()
            .toList()

}

fun main() {
    val data = Data22.getPuzzleInput()
    val (cardDeckPlayer1, cardDeckPlayer2) = parseSpaceCards(data)

    val winnerDeck = playSpaceGame(cardDeckPlayer1.clone(), cardDeckPlayer2.clone())
    val score = calcScore(winnerDeck)
    println(score)

    val winner = playRecursiveGame(cardDeckPlayer1, cardDeckPlayer2)
    println(calcScore(cardDeckPlayer2))
}

fun playRecursiveGame(cardDeckPlayer1: ArrayDeque<Int>, cardDeckPlayer2: ArrayDeque<Int>): Int {
    val playedDecks: MutableSet<Pair<ArrayDeque<Int>, ArrayDeque<Int>>> = mutableSetOf()

    while (cardDeckPlayer1.size > 0 && cardDeckPlayer2.size > 0) {
        if (playRecursiveRound(cardDeckPlayer1, cardDeckPlayer2, playedDecks)) return 1
    }

    val winner = if (cardDeckPlayer1.isEmpty()) 2 else 1
    return winner
}

fun playRecursiveRound(
        cardDeckPlayer1: ArrayDeque<Int>,
        cardDeckPlayer2: ArrayDeque<Int>,
        playedDecks: MutableSet<Pair<ArrayDeque<Int>, ArrayDeque<Int>>>): Boolean {

    val currentDecks = cardDeckPlayer1 to cardDeckPlayer2
    if (playedDecks.contains(currentDecks)) return true
    playedDecks.add(currentDecks)

    val (c1, c2) = listOf(cardDeckPlayer1.removeFirst(), cardDeckPlayer2.removeFirst())

    val winner =
            if (c1 <= cardDeckPlayer1.size && c2 <= cardDeckPlayer2.size)
                playRecursiveGame(cardDeckPlayer1.clone(c1), cardDeckPlayer2.clone(c2))
            else if (c1 < c2) 2
            else if (c1 > c2) 1
            else 0

    when (winner) {
        1 -> { cardDeckPlayer1.addLast(c1)
               cardDeckPlayer1.addLast(c2) }
        2 -> { cardDeckPlayer2.addLast(c2)
               cardDeckPlayer2.addLast(c1) }
    }
    return false
}

private fun <E> ArrayDeque<E>.clone(cnt: Int = 0): ArrayDeque<E>
= ArrayDeque<E>().also {
    if (cnt == 0) it.addAll(this)
    else it.addAll(this.subList(0, cnt))
}


fun playSpaceGame(cardDeckPlayer1: ArrayDeque<Int>,
                  cardDeckPlayer2: ArrayDeque<Int>): ArrayDeque<Int> {

    while (cardDeckPlayer1.size > 0 && cardDeckPlayer2.size > 0) {
        val c1 = cardDeckPlayer1.removeFirst()
        val c2 = cardDeckPlayer2.removeFirst()

        if (c1 > c2) {
            cardDeckPlayer1.addLast(c1)
            cardDeckPlayer1.addLast(c2)
        } else {
            cardDeckPlayer2.addLast(c2)
            cardDeckPlayer2.addLast(c1)
        }
    }

    val winner =
            if (cardDeckPlayer1.size == 0) cardDeckPlayer2
            else cardDeckPlayer1
    return winner
}

fun calcScore(winner: ArrayDeque<Int>): Long {
    return winner.reversed().mapIndexed { index, i ->  (index.toLong() + 1L) * i.toLong() }.sum()
}

fun parseSpaceCards(data: List<String>): Pair<ArrayDeque<Int>, ArrayDeque<Int>> {
    val p1 = ArrayDeque<Int>()
    val p2 = ArrayDeque<Int>()

    var cur = p1
    data.forEach {
        if (it == "Player 2:") { cur = p2 }
        try { cur.addLast(it.toInt() ) } catch (e: Exception) {}
    }

    return p1 to p2
}
