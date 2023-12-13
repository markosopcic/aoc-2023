fun main() {
    time {
        d12p1()
    }

    time {
        d12p2()
    }
}


fun d12p2() {
    val input = readInput("day12").map { it.split(" ").let { it[0] to it[1].split(",").map { it.toInt() } } }
        .map { pair ->
            List(5) { pair.first }.joinToString("?") to List(5) { pair.second }.flatten()
        }

    input.sumOf { solveLine(it.first, "", it.second, 0, 0, mutableMapOf()) }.println()
}

fun d12p1() {
    val input = readInput("day12").map { it.split(" ").let { it[0] to it[1].split(",").map { it.toInt() } } }

    input.sumOf { solveLine(it.first, "", it.second, 0, 0, mutableMapOf()) }.println()
}

fun solveLine(
    springsLeft: String,
    builtSpring: String,
    groups: List<Int>,
    currGroupIndex: Int,
    currLength: Int,
    cache: MutableMap<String, Long>
): Long {
    cache["$springsLeft-$currLength-$currGroupIndex"]?.let {
        return it
    }

    if ((currGroupIndex > groups.indices.last || (currLength == groups[currGroupIndex] && currGroupIndex == groups.indices.last)) && springsLeft.isEmpty()) {
        return 1
    } else if (springsLeft.isEmpty()) {
        return 0
    }

    if (currGroupIndex in groups.indices && (groups[currGroupIndex] - currLength + groups.subList(
            currGroupIndex + 1,
            groups.size
        ).sum() > springsLeft.length)
    ) return 0

    val solution =  if (springsLeft.first() == '.') {
        //if we encounter a dot, we must either be just finished with a group, or haven't started it yet
        if (currGroupIndex in groups.indices && currLength == groups[currGroupIndex]) {
            solveLine(springsLeft.substring(1), "$builtSpring.", groups, currGroupIndex + 1, 0, cache)
        } else if (currLength == 0) {
            solveLine(springsLeft.substring(1), "$builtSpring.", groups, currGroupIndex, currLength, cache)
        } else 0
    } else if (springsLeft.first() == '#') {
        // if we encounter a hash, it means it has to be inside the current group
        if (currGroupIndex in groups.indices && currLength >= groups[currGroupIndex]) 0
        else if (currGroupIndex in groups.indices && currLength < groups[currGroupIndex]) solveLine(
            springsLeft.substring(1),
            "$builtSpring#",
            groups,
            currGroupIndex,
            currLength + 1,
            cache
        ) else 0
    } else if (springsLeft.first() == '?') {
        // if we encounter a question mark, replace it with two calls, one for a dot and one for a hash
        return solveLine(
            "." + springsLeft.substring(1),
            builtSpring,
            groups,
            currGroupIndex,
            currLength,
            cache
        ) + solveLine("#" + springsLeft.substring(1), builtSpring, groups, currGroupIndex, currLength, cache)
    } else 0

    cache["$springsLeft-$currLength-$currGroupIndex"] = solution

    return solution
}
