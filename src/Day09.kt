fun main() {
    time {
        d9p1()
    }
    time {
        d9p2()
    }
}

fun d9p1() {
    readInput("day09").map { it.split(" ").map { it.toInt() } }.map { readings ->
        var diff = readings
        val diffs = mutableListOf(readings)
        while (!diff.all { it == 0 }) {
            diff = diff.windowed(2, 1).map { it[1] - it[0] }
            diffs.add(diff)
        }
        diffs.map {
            listOf(it[it.size - 2], it[it.size - 1])
        }
            .reversed()
            .reduce { acc, ints -> listOf(ints[0], ints[1] + acc[1]) }
    }.sumOf { it[1] }.println()
}

fun d9p2() {
    readInput("day09").map { it.split(" ").map { it.toInt() } }.map { readings ->
        var diff = readings
        val diffs = mutableListOf(readings)
        while (!diff.all { it == 0 }) {
            diff = diff.windowed(2, 1).map { it[1] - it[0] }
            diffs.add(diff)
        }
        diffs.map {
            listOf(it[0], it[1])
        }
            .reversed()
            .reduce { acc, ints -> listOf(ints[0] - acc[0], ints[0]) }
    }.sumOf { it[0] }.println()
}