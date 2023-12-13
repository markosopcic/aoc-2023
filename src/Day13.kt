fun main() {
    time {
        d13p1()
    }
    time {
        d13p2()
    }
}

fun d13p2() {

    val maps = getInput()
    val transposedMaps = maps.map { it.transpose() }

    (maps.map { it.findMirrorIndexWithSmudge() * 100 } + transposedMaps.map { it.findMirrorIndexWithSmudge() }).sum().println()
}

fun d13p1() {
    val maps = getInput()
    val transposedMaps = maps.map { it.transpose() }

    (maps.map { it.findMirrorIndex() * 100 } + transposedMaps.map { it.findMirrorIndex() }).sum().println()
}

fun Table.findMirrorIndexWithSmudge(): Int {
    for (i in indices) {
        if (checkIfMirrorWithSmudge(i) && !checkIfMirror(i)) {
            return i + 1
        }
    }
    return 0
}

fun Table.findMirrorIndex(): Int {
    for (i in indices) {
        if (checkIfMirror(i)) {
            return i + 1
        }
    }
    return 0
}

fun Table.checkIfMirrorWithSmudge(row: Int): Boolean {
    var left = row
    var right = row + 1
    var isMirror = false
    var smudgeFixed = false
    while (left >= 0 && right < this.size) {
        isMirror = this[left].contentEquals(this[right])
        if(!isMirror && !smudgeFixed && this[left].diffIndices(this[right]).size == 1){
            smudgeFixed = true
            isMirror = true
        }
        if (!isMirror) break
        left--
        right++
    }

    return isMirror
}


fun Table.checkIfMirror(row: Int): Boolean {
    var left = row
    var right = row + 1
    var isMirror = false
    while (left >= 0 && right < this.size) {
        isMirror = this[left].contentEquals(this[right])
        if (!isMirror) break
        left--
        right++
    }

    return isMirror
}

fun getInput(): List<Table> {
    val lines = readInput("day13")
    val maps = mutableListOf<Table>()
    val map = mutableListOf<CharArray>()
    for (line in lines) {
        if (line.isBlank()) {
            maps.add(map.toTypedArray())
            map.clear()
        } else {
            map.add(line.toCharArray())
        }
    }
    maps.add(map.toTypedArray())

    return maps
}