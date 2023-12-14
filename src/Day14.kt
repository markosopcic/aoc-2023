import Side.*
fun main() {
    time {
        d14p2()
    }
}


fun d14p1() {
    val input = readInput("day14").map { it.toCharArray() }.toTypedArray().transpose()
    input.map {
        it.joinToString("").split("#")
            .joinToString("#") {
                it.toCharArray().sortedArrayDescending().joinToString("")
            }.toCharArray()
    }.toTypedArray().also { it.print() }.withIndex().flatMap { row ->
        row.value.withIndex().map { XY(it.index, row.index) to it.value }
    }.filter {
        it.second == 'O'
    }.sumOf {
        (input.size - it.first.x)
    }.println()
}

fun d14p2() {
    var input = readInput("day14").map { it.toCharArray() }.toTypedArray()

    repeat(10000000) {
        repeat(4) {
            input = input.cycle(Side.entries[it % 4])
        }
    }

    input.transpose().withIndex().flatMap { row ->
        row.value.withIndex().map { XY(it.index, row.index) to it.value }
    }.filter {
        it.second == 'O'
    }.sumOf {
        (input.size - it.first.x)
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

fun Table.findAllCircleIndices() = indices.flatMap { y-> this[0].indices.filter { x-> this[y][x] == 'O' } }
