import Direction.*

fun main() {
    time {
        d16p1()
    }

    time {
        d16p2()
    }
}

fun d16p2() {
    val input = readInput("day16").map { it.toCharArray() }.toTypedArray()

    val combinations = input.indices.map { XY(-1, it) to Right } +
            input.indices.map { XY(input.size, it) to Left } +
            input[0].indices.map { XY(it, -1) to Down } +
            input[0].indices.map { XY(it, input.size) to Up }

    combinations.maxOf {
        val visited = mutableSetOf<Pair<XY, Direction>>()
        traverseMirrors(input, it.first, it.second, visited)
        visited.distinctBy { it.first }.size
    }.println()
}

fun d16p1() {
    val input = readInput("day16").map { it.toCharArray() }.toTypedArray()

    val visited = mutableSetOf<Pair<XY, Direction>>()

    traverseMirrors(input, XY(-1, 0), Right, visited)

    visited.distinctBy { it.first }.size.println()
}

fun Direction.toChar() = when (this) {
    Up -> '^'
    Down -> 'v'
    Left -> '<'
    Right -> '>'
}

fun traverseMirrors(table: Table, current: XY, direction: Direction, visited: MutableSet<Pair<XY, Direction>>) {

    val next = table.toDirectionOf(direction, current) ?: return

    //map next to direction so we have a pair of where to where we're going to and where we came from
    if (!visited.add(next to direction)) return

    val nextChar = table[next]
    when (nextChar) {
        '/' -> when (direction) {
            Up -> traverseMirrors(table, next, Right, visited)
            Down -> traverseMirrors(table, next, Left, visited)
            Left -> traverseMirrors(table, next, Down, visited)
            Right -> traverseMirrors(table, next, Up, visited)
        }

        '\\' -> {
            when (direction) {
                Up -> traverseMirrors(table, next, Left, visited)
                Down -> traverseMirrors(table, next, Right, visited)
                Left -> traverseMirrors(table, next, Up, visited)
                Right -> traverseMirrors(table, next, Down, visited)
            }
        }

        '|' -> {
            when (direction) {
                Up, Down -> traverseMirrors(table, next, direction, visited)
                Left, Right -> {
                    traverseMirrors(table, next, Up, visited)
                    traverseMirrors(table, next, Down, visited)
                }
            }
        }

        '-' -> {
            when (direction) {
                Up, Down -> {
                    traverseMirrors(table, next, Left, visited)
                    traverseMirrors(table, next, Right, visited)
                }

                Left, Right -> {
                    traverseMirrors(table, next, direction, visited)
                }
            }
        }

        else -> traverseMirrors(table, next, direction, visited)
    }
}
