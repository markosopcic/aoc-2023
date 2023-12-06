fun main() {
    time {
        d7p1()
    }
    time {
        d7p2()
    }
}

private fun d7p1() {
    val hands = readInput("day7").map {
        val parts = it.split(" ")
        Hand(parts[0].trim().toCharArray().toList(), parts[1].trim().toInt(), false)
    }

    hands.sorted().withIndex().sumOf { (it.index + 1).toLong() * it.value.bid.toLong() }.println()
}

private fun d7p2() {
    val hands = readInput("day07").map {
        val parts = it.split(" ")
        Hand(parts[0].trim().toCharArray().toList(), parts[1].trim().toInt(), true)
    }
    hands.sorted().withIndex().sumOf { (it.index + 1) * it.value.bid }.println()
}

private data class Hand(val cards: List<Char>, val bid: Int, val jokerCard: Boolean) : Comparable<Hand> {

    val strength: Strength
    val jokers: Int = cards.count { it == 'J' }

    init {
        val groups = if (!jokerCard) cards.groupBy { it } else cards.filter { it != 'J' }.groupBy { it }
        if (!jokerCard) {
            strength = when {
                groups.any { it.value.size == 5 } -> Strength.Five
                groups.any { it.value.size == 4 } -> Strength.Four
                groups.any { it.value.size == 3 } && groups.any { it.value.size == 2 } -> Strength.FullHouse
                groups.any { it.value.size == 3 } -> Strength.Three
                groups.count { it.value.size == 2 } == 2 -> Strength.TwoPair
                groups.count { it.value.size == 2 } == 1 -> Strength.Pair
                else -> Strength.HighCard
            }
        } else {
            strength = when {
                groups.any { it.value.size == 5 }
                        || (groups.any { it.value.size == 4 } && jokers == 1)
                        || (groups.any { it.value.size == 3 } && jokers == 2)
                        || (groups.any { it.value.size == 2 } && jokers == 3)
                        || jokers >= 4 -> Strength.Five

                groups.any { it.value.size == 4 }
                        || (groups.any { it.value.size == 3 } && jokers == 1)
                        || (groups.any { it.value.size == 2 } && jokers == 2)
                        || jokers >= 3 -> Strength.Four

                (groups.any { it.value.size == 3 } && groups.any { it.value.size == 2 })
                        || (groups.count { it.value.size == 2 } == 2 && jokers == 1) -> Strength.FullHouse

                groups.any { it.value.size == 3 }
                        || (groups.any { it.value.size == 2 } && jokers >= 1)
                        || (groups.none { it.value.size > 1 } && jokers >= 2) -> Strength.Three

                groups.count { it.value.size == 2 } == 2 -> Strength.TwoPair
                groups.any { it.value.size == 2 } || jokers >= 1 -> Strength.Pair
                else -> Strength.HighCard
            }
        }
    }

    override fun compareTo(other: Hand): Int {
        if (this.strength.ordinal < other.strength.ordinal) return 1
        if (this.strength.ordinal > other.strength.ordinal) return -1
        val map = if (jokerCard) CardStrengthMapJoker else CardStrengthMap
        for (pair in cards.zip(other.cards)) {
            val firstStrength = map[pair.first]!!
            val secondStrength = map[pair.second]!!
            if (firstStrength > secondStrength) {
                return 1
            }
            if (secondStrength > firstStrength) {
                return -1
            }
        }
        return 0
    }

    enum class Strength {
        Five,
        Four,
        FullHouse,
        Three,
        TwoPair,
        Pair,
        HighCard
    }

    companion object {
        val CardStrengthMap = mapOf(
            '2' to 1,
            '3' to 2,
            '4' to 3,
            '5' to 4,
            '6' to 5,
            '7' to 6,
            '8' to 7,
            '9' to 8,
            'T' to 9,
            'J' to 10,
            'Q' to 11,
            'K' to 12,
            'A' to 13
        )

        val CardStrengthMapJoker = mapOf(
            'J' to 0,
            '2' to 1,
            '3' to 2,
            '4' to 3,
            '5' to 4,
            '6' to 5,
            '7' to 6,
            '8' to 7,
            '9' to 8,
            'T' to 9,
            'Q' to 11,
            'K' to 12,
            'A' to 13
        )
    }
}