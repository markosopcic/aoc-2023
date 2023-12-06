fun main() {
    time {
        d5p1()
    }
    time {
        d5p2()
    }
}

private fun d5p1() {
    val input = readInput("day5")

    val seeds = input.first().replace("seeds: ", "").split(" ").map { it.toLong() }

    val mainMap = parseMaps(input)
    val solution =
        resolve(
            seeds.asSequence(),
            mainMap.entries.first().value.asSequence(),
            mainMap.entries.drop(1).associate { it.key to it.value })

    println(solution.min())
}

private fun d5p2() {
    val input = readInput("day5")

    val seeds = input.first().replace("seeds: ", "").split(" ").map { it.toLong() }.windowed(size = 2, step = 2)
    val seedRanges = seeds.map { it[0]..<(it[0] + it[1]) }
    val mainMap = parseMaps(input)
    val solution =
        resolveRanges(
            seedRanges,
            mainMap.entries.first().value,
            mainMap.entries.drop(1).associate { it.key to it.value })

    println(solution.minOf { it.first })
}

private fun parseMaps(input: List<String>): Map<Pair<String, String>, MutableList<Mapping>> {
    val mainMap = mutableMapOf<Pair<String, String>, MutableList<Mapping>>()
    var mapName: Pair<String, String>? = null
    for (line in input.drop(1)) {
        if (line.contains("map:")) {
            val mapNameParts = line.replace(" map:", "").split("-to-")
            mapName = mapNameParts[0] to mapNameParts[1].trim()
            mainMap[mapName] = mutableListOf()
        } else if (line.isNotEmpty()) {
            val parts = line.split(" ").map { it.toLong() }
            val mapping = Mapping(parts[1], parts[0], parts[2], parts[1]..<(parts[1] + parts[2]))
            mainMap[mapName]!!.add(mapping)
        } else {
            mapName = null
        }
    }
    return mainMap
}

private tailrec fun resolveRanges(
    currentValues: List<LongRange>,
    currentMappings: List<Mapping>,
    mainMap: Map<Pair<String, String>, MutableList<Mapping>>
): List<LongRange> {
    val rangesToResolve = currentValues.toMutableList()
    val nextValues = mutableListOf<LongRange>()

    while (rangesToResolve.any()) {
        val range = rangesToResolve.removeFirst()
        val mapping = currentMappings.firstOrNull { range.first in it.sourceRange || range.last in it.sourceRange }

        if (mapping != null) {
            var firstRange: LongRange
            var secondRange: LongRange? = null
            if (range.first in mapping.sourceRange && range.last in mapping.sourceRange) {
                firstRange = range

            } else if (range.first in mapping.sourceRange) {
                firstRange = range.first..mapping.sourceRange.last
                secondRange = (mapping.sourceRange.last + 1)..range.last
            } else {
                secondRange = range.first..<mapping.sourceRange.first
                firstRange = (mapping.sourceRange.first)..range.last
            }
            firstRange =
                (firstRange.first + mapping.destination - mapping.source)..(firstRange.last + mapping.destination - mapping.source)

            nextValues.add(firstRange)
            secondRange?.let { rangesToResolve.add(it) }
        } else {
            nextValues.add(range)
        }

    }

    return if (mainMap.entries.any()) {
        resolveRanges(
            nextValues,
            mainMap.entries.first().value,
            mainMap.entries.drop(1).associate { it.key to it.value })
    } else nextValues
}

private tailrec fun resolve(
    currentValues: Sequence<Long>,
    currentMappings: Sequence<Mapping>,
    mainMap: Map<Pair<String, String>, MutableList<Mapping>>
): Sequence<Long> {
    val nextValues = currentValues.map { value ->
        val mapping = currentMappings.firstOrNull { mapping -> value in mapping.sourceRange }
        //57451709
        if (mapping != null) {
            value + mapping.destination - mapping.sourceRange.first
        } else {
            value
        }
    }

    return if (mainMap.entries.any()) {
        resolve(
            nextValues,
            mainMap.entries.first().value.asSequence(),
            mainMap.entries.drop(1).associate { it.key to it.value })
    } else nextValues
}

private data class Mapping(val source: Long, val destination: Long, val length: Long, val sourceRange: LongRange)