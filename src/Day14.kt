import Side.*

fun main() {
    time { d14p1() }
    time {
        d14p2()
    }
}


fun d14p1() {
    val input = readInput("day14").map { it.toCharArray() }.toTypedArray()
    input.cycle(North).withIndex().flatMap { row ->
        row.value.withIndex().map { XY(it.index, row.index) to it.value }
    }.filter {
        it.second == 'O'
    }.sumOf {
        (input.size - it.first.y)
    }.println()
}

fun d14p2() {
    var input = readInput("day14").map { it.toCharArray() }.toTypedArray()
    val limit = 1000000000

    val map = mutableMapOf<String, Long>()

    var cyclesUntilRepeat = 0
    while (input.findAllCircleIndices() !in map) {
        cyclesUntilRepeat++
        map[input.findAllCircleIndices()] = cyclesUntilRepeat.toLong()
        for (x in 0 until 4) {
            input = input.cycle(Side.entries[x % 4])
        }
    }

    var cycleLength = 0
    val cycleMap = mutableMapOf<String, Long>()
    while (input.findAllCircleIndices() !in cycleMap) {
        cycleLength++
        cycleMap[input.findAllCircleIndices()] = cycleLength.toLong()
        map[input.findAllCircleIndices()] = cyclesUntilRepeat.toLong()
        for (x in 0 until 4) {
            input = input.cycle(Side.entries[x % 4])
        }
    }

    val leftOverSteps = (limit - cyclesUntilRepeat) % cycleLength

    repeat(leftOverSteps) {
        for (x in 0 until 4) {
            input = input.cycle(Side.entries[x % 4])
        }
    }

    input.withIndex().flatMap { row ->
        row.value.withIndex().map { XY(it.index, row.index) to it.value }
    }.filter {
        it.second == 'O'
    }.sumOf {
        (input.size - it.first.y)
    }.println()
}

enum class Side {
    North, West, South, East
}

fun Table.cycle(side: Side): Table {
    val table = this
    when (side) {
        North -> {
            for (x in this[0].indices)
                for (y in indices) {
                    if (table[y][x] == 'O') {
                        var placeY = y
                        while (placeY > 0 && table[placeY - 1][x] == '.') placeY--
                        if (placeY != y) {
                            table[placeY][x] = 'O'
                            table[y][x] = '.'
                        }
                    }
                }
        }

        West -> {
            for (y in indices) {
                for (x in table[0].indices) {
                    if (table[y][x] == 'O') {
                        var placeX = x
                        while (placeX > 0 && table[y][placeX - 1] == '.') placeX--
                        if (placeX != x) {
                            table[y][placeX] = 'O'
                            table[y][x] = '.'
                        }
                    }
                }
            }
        }

        South -> {
            for (x in table[0].indices) {
                for (y in indices.reversed()) {
                    if (table[y][x] == 'O') {
                        var placeY = y
                        while (placeY < table.size - 1 && table[placeY + 1][x] == '.') placeY++
                        if (placeY != y) {
                            table[placeY][x] = 'O'
                            table[y][x] = '.'
                        }
                    }
                }
            }
        }

        East -> {
            for (y in indices) {
                for (x in table[0].indices.reversed()) {
                    if (table[y][x] == 'O') {
                        var placeX = x
                        while (placeX < table[0].size - 1 && table[y][placeX + 1] == '.') placeX++
                        if (placeX != x) {
                            table[y][placeX] = 'O'
                            table[y][x] = '.'
                        }
                    }
                }
            }
        }
    }
    return this
}

fun Table.findAllCircleIndices() =
    indices.flatMap { y -> this[0].indices.filter { x -> this[y][x] == 'O' }.map { it to y } }.map {
        "${it.first}-${it.second}"
    }.joinToString(",")
