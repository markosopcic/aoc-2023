import kotlin.math.ceil

fun main() {
    time {
        d10p1()
    }

    time {
        d10p2()
    }
}

fun d10p1() {
    val input = readInput("day10")
    val table = input.map { it.toCharArray() }.toTypedArray()
    val s = findStartingPoint(table)
    val mainPath = calculateMainPath(s, table)
    println(ceil((mainPath.size).toDouble().div(2)))
}

fun d10p2() {
    val input = readInput("day10")
    val table = input.map { it.toCharArray() }.toTypedArray()
    val s = findStartingPoint(table)
    val mainPath = calculateMainPath(s, table)

    table.indices.flatMap { y -> table[y].indices.map { x -> XY(x,y) } }.filter { it !in mainPath }.count { it.isInsidePolygon(mainPath) }.println()
}

fun findStartingPoint(table: Array<CharArray>): XY {
    outer@ for ((y, row) in table.withIndex()) {
        for ((x, char) in row.withIndex()) {
            if (char == 'S') {
                return XY(x, y)
            }
        }
    }
    throw Exception("Must've found it")
}

fun XY.isInsidePolygon(polygon: List<XY>): Boolean {
    var inside = false

    val n = polygon.size
    var j = n - 1

    for (i in 0 until n) {
        val xi = polygon[i].x
        val yi = polygon[i].y
        val xj = polygon[j].x
        val yj = polygon[j].y

        val intersect = ((yi > y) != (yj > y)) &&
                (x < (xj - xi) * (y - yi) / (yj - yi) + xi)

        if (intersect) {
            inside = !inside
        }

        j = i
    }

    return inside
}

fun calculateMainPath(start: XY, table: Array<CharArray>): List<XY> {
    val visitedSet = mutableSetOf(start)
    val visited = mutableListOf<XY>(start)
    val distanceFromStart = mutableMapOf(start to 0)
    val options = mutableListOf<XY>().apply {
        val right = table.rightOf(start)
        val left = table.leftOf(start)
        val top = table.above(start)
        val bottom = table.below(start)
        if (table.atXY(right) in listOf('-', '7', 'J')) add(right!!)
        if (table.atXY(left) in listOf('-', 'L', 'F')) add(left!!)
        if (table.atXY(top) in listOf('|', 'F', '7')) add(top!!)
        if (table.atXY(bottom) in listOf('|', 'J', 'L')) add(bottom!!)
    }

    var lastX = start.x
    var lastY = start.y
    for (option in options) {
        var x = option.x
        var y = option.y
        while (true) {
            val curr = table[y][x]

            if (!visitedSet.add(XY(x, y))) break
            visited.add(XY(x, y))

            distanceFromStart[XY(x, y)] = distanceFromStart[XY(lastX, lastY)]!! + 1
            when (curr) {
                '|' -> {
                    if (y < lastY) {
                        lastY = y
                        y--
                    } else {
                        lastY = y
                        y++
                    }
                }

                '-' -> {
                    if (x < lastX) {
                        lastX = x
                        x--
                    } else {
                        lastX = x
                        x++
                    }
                }

                'L' -> {
                    if (y > lastY) {
                        lastX = x
                        lastY = y
                        x++
                    } else {
                        lastY = y
                        lastX = x
                        y--
                    }
                }

                'J' -> {
                    if (y > lastY) {
                        lastY = y
                        lastX = x
                        x--
                    } else {
                        lastY = y
                        lastX = x
                        y--
                    }
                }

                'F' -> {
                    if (y < lastY) {
                        lastY = y
                        lastX = x
                        x++
                    } else {
                        lastY = y
                        lastX = x
                        y++
                    }
                }

                '7' -> {
                    if (y < lastY) {
                        lastY = y
                        lastX = x
                        x--
                    } else {
                        lastY = y
                        lastX = x
                        y++
                    }
                }

                'S' -> {
                    break
                }

                else -> {

                }
            }
        }
    }

    return visited
}
