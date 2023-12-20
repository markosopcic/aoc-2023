import java.util.*
import java.util.concurrent.LinkedBlockingQueue


fun main() {
    time {
        d17p1()
    }

    time {
        d17p2()
    }
}

fun d17p1() {
    val table = readInput("day17").map { it.toCharArray() }.toTypedArray()

    dijkstra(table, XY(0, 0), XY(table[0].size - 1, table.size - 1), 1,3).println()
}

fun d17p2(){
    val table = readInput("day17").map { it.toCharArray() }.toTypedArray()

    dijkstra(table, XY(0, 0), XY(table[0].size - 1, table.size - 1), 4,10).println()
}

data class Data(val heat: Int, val xy: XY, val forbiddenDirection: Direction?) : Comparable<Data> {
    override fun compareTo(other: Data): Int {
        return heat.compareTo(other.heat).takeIf { it != 0 } ?: xy.x.compareTo(other.xy.x).takeIf { it != 0 }
        ?: xy.y.compareTo(other.xy.y)
    }
}

fun dijkstra(table: Table, start: XY, end: XY, minSteps: Int, maxSteps: Int): Int {
    val queue = PriorityQueue<Data>()
    val seen = mutableSetOf<Data>()
    val costs = mutableMapOf<Pair<XY, Direction>, Int>()

    queue.add(Data(0, start, null))

    while (queue.any()) {
        val current = queue.poll()
        if (current.xy == end) return current.heat

        if(current in seen) continue
        seen.add(current)

        for(direction in Direction.entries.filter { it != current.forbiddenDirection && it != current.forbiddenDirection?.oposite }){
            var costIncrease = 0
            for(distance in 1..maxSteps){
                val coords = (1..distance).fold(current.xy) {acc, _ -> acc.toDirection(direction) }
                if(!table.isInside(coords)) continue
                costIncrease += table[coords.y][coords.x].digitToInt()
                if(distance < minSteps) continue
                val newCost = current.heat + costIncrease
                if(costs.getOrDefault(coords to direction, Integer.MAX_VALUE) < newCost) continue

                costs[coords to direction] = newCost
                queue.add(Data( newCost, coords, direction))
            }
        }
    }
    return 0
}
