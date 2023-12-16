fun main() {
    time {
        d15p1()
    }

    time {
        d15p2()
    }
}

fun d15p1() {
    readInput("day15").first()
        .split(",")
        .map(String::aocHash)
        .sumOf { it }.println()
}

fun d15p2() {
    val parts = readInput("day15").first().split(",")

    val boxes = Array(256) {
        mutableMapOf<String, Int>()
    }

    for (part in parts) {
        val operationIndex = part.indexOfFirst { it == '-' || it == '=' }
        val operation = part[operationIndex]
        val label = part.substring(0, operationIndex)
        val hash = label.aocHash()

        when (operation) {
            '-' -> {
                boxes[hash].remove(label)
            }

            '=' -> {
                val lens = part.substring(operationIndex + 1).toInt()
                boxes[hash][label] = lens
            }
        }
    }

    boxes.withIndex().sumOf {
        (it.index + 1) * it.value.entries.withIndex().sumOf { (it.index + 1) * it.value.value }
    }
        .println()
}

fun String.aocHash() = fold(0) { acc, c -> ((acc + c.code) * 17) % 256 }