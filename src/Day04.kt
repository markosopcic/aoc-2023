import kotlin.math.pow

fun main() {
    d4p1()
    d4p2()
}

fun d4p1() {
    val cards = input()

    val points = cards.map { it.winningNumbers.intersect(it.yourNumbers).size }.sumOf { 2.0.pow(it - 1).toInt() }

    println(points)
}

fun d4p2() {
    var cards = input().toMutableList()
    val originalCards = cards.associateBy { it.id }
    val allCards = mutableListOf<ScratchCard>()

    while (cards.isNotEmpty()) {
        val newCards = mutableListOf<ScratchCard>()
        for (card in cards) {
            allCards.add(card)
            newCards.addAll((card.id + 1..<card.id + 1 + card.matchingNumbers).map { originalCards[it]!! })
        }
        cards = newCards
    }

    println(allCards.size)
}

private fun input() = readInput("day4").map {
    val whitespaceRegex = Regex("\\s+")
    val parts = it.split("|")
    val leftParts = parts[0].split(":")
    val cardId = leftParts[0].split(whitespaceRegex)[1].toInt()
    val winningNumbers = leftParts[1].trim().split(whitespaceRegex).map { it.toInt() }.toSet()
    val yourNumbers = parts[1].trim().split(whitespaceRegex).map { it.toInt() }.toSet()

    ScratchCard(cardId, winningNumbers, yourNumbers, winningNumbers.intersect(yourNumbers).size)
}

data class ScratchCard(val id: Int, val winningNumbers: Set<Int>, val yourNumbers: Set<Int>, val matchingNumbers: Int)