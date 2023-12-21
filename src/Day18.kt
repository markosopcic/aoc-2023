fun main() {
    time {
        solve(false)
    }

    time {
        solve(true)
    }
}

fun solve(part2: Boolean) {
    val input = readInput("day18")
    var xy = XY(0, 0)
    val points = mutableListOf<XY>()
    for (line in input) {
        val parts = line.split(" ")
        val color = parts[2].trimStart('(').trimEnd(')').trimStart('#')
        val direction = if (!part2) parts[0].first() else color.last().toDirection()
        val steps = if (!part2) parts[1].toInt() else color.substring(0, color.length - 1).toInt(16)

            when (direction) {
                'U' -> {
                    xy = xy.copy(y = xy.y - steps)
                }

                'D' -> {
                    xy = xy.copy(y = xy.y + steps)
                }

                'L' -> {
                    xy = xy.copy(x = xy.x - steps)
                }

                'R' -> {
                    xy = xy.copy(x = xy.x + steps)
                }
            }
        points.add(xy)
    }

    getSurfaceArea(points).println()
}

private fun Char.toDirection() = when (this) {
    '0' -> 'R'
    '1' -> 'D'
    '2' -> 'L'
    '3' -> 'U'
    else -> throw Exception("Unknown direction")
}

private fun getSurfaceArea(points: List<XY>): Long {
    var area: Long = 0
    for (i in points.indices) {
        val c1: XY = points[i]
        val c2: XY = points[if (i == points.size - 1) 0 else i + 1]
        val factor: Long = c1.x.toLong() * c2.y - c1.y.toLong() * c2.x
        area += factor
    }
    area /= 2
    return area - (getTotalManhattanDistance(points) / 2) + 1 + getTotalManhattanDistance(points)
}

private fun getTotalManhattanDistance(corners: List<XY>): Long {
    var total: Long = 0

    for (i in corners.indices) {
        val c1: XY = corners[i]
        val c2: XY = corners[if (i == corners.size - 1) 0 else i + 1]
        total += Math.abs(c1.x - c2.x) + Math.abs(c1.y - c2.y)
    }

    return total
}