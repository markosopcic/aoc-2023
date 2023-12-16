import Direction.*

data class XY(val x: Int, val y: Int)

fun XY.isAbove(other: XY) = this.y < other.y

fun XY.isBelow(other: XY) = this.y > other.y

fun XY.isLeftOf(other: XY) = this.x < other.x

fun XY.isRightOf(other: XY) = this.x > other.x

typealias Table = Array<CharArray>

fun Table.toDirectionOf(direction: Direction, point: XY) = when (direction) {
    Up -> this.above(point)
    Down -> below(point)
    Left -> leftOf(point)
    Right -> rightOf(point)
}

fun Table.leftOf(point: XY) =
    XY(point.x - 1, point.y).takeIf(::isInside)

fun Table.above(point: XY) = XY(point.x, point.y - 1).takeIf(::isInside)

fun Table.below(point: XY) = XY(point.x, point.y + 1).takeIf(::isInside)

fun Table.rightOf(point: XY) = XY(point.x + 1, point.y).takeIf(::isInside)

fun Table.atXY(point: XY?) = point?.takeIf(::isInside)?.let {
    this[it.y][it.x]
}

fun Table.isInside(point: XY) = point.x >= 0 && point.x < this[0].size && point.y >= 0 && point.y < this.size

fun Table.findNeighbors(x: Int, y: Int): List<Pair<XY, Char>> {
    val neighbors = mutableListOf<Pair<XY, Char>>()

    val numRows = this.size
    val numCols = this[0].size

    // Define the possible relative positions of neighbors
    val directions = arrayOf(
        Pair(-1, -1), Pair(-1, 0), Pair(-1, 1),
        Pair(0, -1), /* Current position */ Pair(0, 1),
        Pair(1, -1), Pair(1, 0), Pair(1, 1)
    )

    for ((dx, dy) in directions) {
        val newRow = y + dx
        val newCol = x + dy

        // Check if the new position is within the bounds of the table
        if (newRow in 0 until numRows && newCol in 0 until numCols) {
            val neighbor = Pair(XY(newCol, newRow), this[newRow][newCol])
            neighbors.add(neighbor)
        }
    }

    return neighbors
}

fun Table.print(colorFunction: ((Char) -> Color?)? = null) {
    for (line in this) {
        for (char in line) {
            val color = colorFunction?.invoke(char)
            if (color != null) {
                print("${color.ansi}$char${Color.Reset.ansi}")
            } else print(char)
        }
        kotlin.io.println()
    }
}

fun Table.transpose(): Table {
    if (isEmpty() || this[0].isEmpty()) {
        return emptyArray()
    }

    val numRows = size
    val numCols = this[0].size

    return Array(numCols) { col ->
        CharArray(numRows) { row ->
            this[row][col]
        }
    }
}

fun Table.find(condition: (XY, Char) -> Boolean) =
    indices.flatMap { y -> this[0].indices.map { XY(it, y) } }.filter { condition(it, this[it.y][it.x]) }

operator fun Table.get(point: XY) = this[point.y][point.x]