import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

fun main() {
    time {
        d11p1()
    }

    time {
        d11p2()
    }
}

fun d11p1() {
    solve(2)
}

fun d11p2() {
    solve(1000000)
}


fun solve(expand: Int) {
    val input = readInput("day11")
    val matrix = input.map { it.toCharArray() }.toTypedArray()
    val duplicateRows = input.withIndex().filter { it.value.all { it == '.' } }.map { it.index }
    val duplicateCols = matrix.transpose().withIndex().filter { it.value.all { it == '.' } }
        .map { it.index }

    val galaxies = matrix.find { _, c -> c == '#' }

    val pairs = mutableListOf<Pair<XY, XY>>()
    for (i in galaxies.indices) {
        for (j in i + 1 until galaxies.size) {
            pairs.add(galaxies[i] to galaxies[j])
        }
    }

    pairs.map {
        val lowerX = min(it.first.x, it.second.x)
        val upperX = max(it.first.x, it.second.x)
        val lowerY = min(it.first.y, it.second.y)
        val upperY = max(it.first.y, it.second.y)

        val distance = upperX - lowerX + upperY - lowerY + duplicateCols.between(
            lowerX,
            upperX
        ) * (expand - 1) + duplicateRows.between(lowerY, upperY) * (expand - 1)


        distance
    }.sumOf { it.toLong() }.println()
}

fun List<Int>.between(lower: Int, upper: Int) = count { it in (lower + 1)..<upper }
