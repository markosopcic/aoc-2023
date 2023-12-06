fun main() {
    d3p1()
    d3p2()
}

fun d3p2() {
    val input = input()
    val coordinatesToBold = mutableMapOf<Pair<Int, Int>, Color>()
    var sum: Long = 0
    for (y in input.indices) {
        for (x in 0 until input[y].size) {
            val isGear = input[y][x] == '*'

            if (isGear) {
                val neighbours = input.findNeighboringNumbers(x, y)
                neighbours.forEach { number ->
                    (number.xStart..number.xEnd).forEach { x ->
                        coordinatesToBold[(x to number.y)] = neighbours.size.neighboursToColor()
                    }
                }
                coordinatesToBold[x to y] = neighbours.size.neighboursToColor()
                if (neighbours.size == 2) {
                    sum += neighbours[0].number * neighbours[1].number
                }
            }
        }
    }

    println(sum)

    for (y in input.indices) {
        for (x in 0..<input[y].size) {
            if (x to y in coordinatesToBold) {
                val color = coordinatesToBold[x to y]
                print("${color!!.ansi}${input[y][x]}${Color.Reset.ansi}")
            } else {
                print(input[y][x])
            }
        }
        println()
    }
}

private fun Int.neighboursToColor() = when (this) {
    1 -> Color.Blue
    2 -> Color.Red
    3 -> Color.Yellow
    4 -> Color.Green
    5 -> Color.Cyan
    6 -> Color.Purple
    7 -> Color.White
    8 -> Color.Black
    else -> Color.White
}


fun d3p1() {
    val input = input()

    var sum = 0
    for (y in input.indices) {
        var xFrom: Int? = null
        var xTo: Int? = null
        for (x in 0..<input[y].size) {
            if (input[y][x].isDigit()) {
                if (xFrom == null) {
                    xFrom = x
                }
                xTo = x
            }
            if (!input[y][x].isDigit() || x == input[y].size - 1) {
                if (xFrom != null) {
                    val isPartNumber = (xFrom..xTo!!).any { input.hasNeighboringSymbol(it, y) }
                    if (isPartNumber) {
                        sum += (xFrom..xTo).map { input[y][it] }.joinToString("").toInt()
                    }

                    xFrom = null
                    xTo = null
                }
            }
        }
    }
    println(sum)
}

private fun input() = readInput("day3").map { it.toCharArray() }.toTypedArray()

private fun Array<CharArray>.findNeighboringNumbers(x: Int, y: Int): List<NumberWithXRange> {
    val numbers = mutableListOf<NumberWithXRange>()
    //left
    if (x > 0 && this[y][x - 1].isDigit() && numbers.coordinatesNotInAny(x - 1, y)) numbers.add(
        findWholeNumber(
            x - 1,
            y
        )!!
    )
    //right
    if (x < this[y].size - 1 && this[y][x + 1].isDigit() && numbers.coordinatesNotInAny(x + 1, y)) numbers.add(
        findWholeNumber(x + 1, y)!!
    )
    //up
    if (y > 0 && this[y - 1][x].isDigit() && numbers.coordinatesNotInAny(x, y - 1)) numbers.add(
        findWholeNumber(
            x,
            y - 1
        )!!
    )
    //down
    if (y < this.size - 1 && this[y + 1][x].isDigit() && numbers.coordinatesNotInAny(x, y + 1)) numbers.add(
        findWholeNumber(x, y + 1)!!
    )
    //up left
    if (y > 0 && x > 0 && this[y - 1][x - 1].isDigit() && numbers.coordinatesNotInAny(x - 1, y - 1)) numbers.add(
        findWholeNumber(x - 1, y - 1)!!
    )
    //down left
    if (y < this.size - 1 && x > 0 && this[y + 1][x - 1].isDigit() && numbers.coordinatesNotInAny(
            x - 1,
            y + 1
        )
    ) numbers.add(findWholeNumber(x - 1, y + 1)!!)
    //down right
    if (y < this.size - 1 && x < this[y].size - 1 && this[y + 1][x + 1].isDigit() && numbers.coordinatesNotInAny(
            x + 1,
            y + 1
        )
    ) numbers.add(
        findWholeNumber(
            x + 1,
            y + 1
        )!!
    )
    //up right
    if (y > 0 && x < this[y].size - 1 && this[y - 1][x + 1].isDigit() && numbers.coordinatesNotInAny(
            x + 1,
            y - 1
        )
    ) numbers.add(findWholeNumber(x + 1, y - 1)!!)

    return numbers
}

private data class NumberWithXRange(val number: Int, val xStart: Int, val xEnd: Int, val y: Int)

private fun List<NumberWithXRange>.coordinatesNotInAny(x: Int, y: Int) = filter { it.y == y }.none {
    x in it.xStart..it.xEnd
}

private fun Array<CharArray>.findWholeNumber(x: Int, y: Int): NumberWithXRange? {
    if (!this[y][x].isDigit()) return null

    var number: String = this[y][x].toString()
    var xRight = x + 1
    var xLeft = x - 1
    while (xRight < this[y].size && this[y][xRight].isDigit()) {
        number += this[y][xRight]
        xRight++
    }
    while (xLeft >= 0 && this[y][xLeft].isDigit()) {
        number = this[y][xLeft].toString() + number
        xLeft--
    }

    return NumberWithXRange(number.toInt(), xLeft + 1, xRight - 1, y)
}

fun Array<CharArray>.hasNeighboringSymbol(x: Int, y: Int): Boolean {
    //left
    if (x > 0 && this[y][x - 1].isSymbol()) return true
    //right
    if (x < this[y].size - 1 && this[y][x + 1].isSymbol()) return true
    //up
    if (y > 0 && this[y - 1][x].isSymbol()) return true
    //down
    if (y < this.size - 1 && this[y + 1][x].isSymbol()) return true
    //up left
    if (y > 0 && x > 0 && this[y - 1][x - 1].isSymbol()) return true
    //down left
    if (y < this.size - 1 && x > 0 && this[y + 1][x - 1].isSymbol()) return true
    //down right
    if (y < this.size - 1 && x < this[y].size - 1 && this[y + 1][x + 1].isSymbol()) return true
    //up right
    if (y > 0 && x < this[y].size - 1 && this[y - 1][x + 1].isSymbol()) return true

    return false
}

fun Char.isSymbol() = !isDigit() && this != '.'