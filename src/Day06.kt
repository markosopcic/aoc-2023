fun main() {
    time {
        d6p1()
    }
    time {
        d6p2()
    }
}

private fun d6p2() {
    val input = input2()
    println(getNumberOfWaysToWinRace(input.first, input.second))
}

private fun d6p1() {
    val input = input()
    val possibleWins = mutableListOf<Long>()

    for ((raceTime, record) in input) {
        possibleWins.add(getNumberOfWaysToWinRace(raceTime, record))
    }
    println(possibleWins.reduce { acc, i -> acc * i })
}

private fun getNumberOfWaysToWinRace(raceTime: Long, record: Long): Long {
    var wasWinning = false
    var waysToWin = 0L
    for (holdTime in 1..raceTime) {
        val distance = holdTime * (raceTime - holdTime)
        if (distance > record) {
            waysToWin++
            wasWinning = true
        } else if (wasWinning) {
            return waysToWin
        }
    }

    return waysToWin
}

private fun input2(): Pair<Long, Long> =
    readInput("day6").map { it.split(":")[1].trim() }.map { it.replace(" ", "").toLong() }.let {
        it[0] to it[1]
    }

private fun input(): List<Pair<Long, Long>> =
    readInput("day6").map { it.split(":")[1].trim() }.map { it.split(Regex("\\s+")).map { it.trim().toLong() } }
        .let {
            it[0].zip(it[1])
        }