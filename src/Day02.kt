fun main() {
    d2p1()
    d2p2()
}

fun d2p2() {
    val input = input()

    println(
        input.map {
            GameSet(it.sets.maxOf { it.red }, it.sets.maxOf { it.green }, it.sets.maxOf { it.blue })
        }.sumOf {
            it.red * it.green * it.blue
        }
    )
}

fun d2p1() {
    val input = input()

    println(
        input.filter {
            it.sets.none {
                it.red > 12 || it.green > 13 || it.blue > 14
            }
        }.sumOf { it.id }
    )
}

private fun input() = readInput("day2").map { line ->
    val parts = line.split(":")

    val id = parts[0].replace("Game ", "").toInt()
    val sets = parts[1].split(";")
    val gameSets = mutableListOf<GameSet>()
    for (set in sets) {
        val cubes = set.split(",")
        val red = cubes.firstOrNull { it.contains("red") }?.replace(" red", "")?.trim()?.toInt() ?: 0
        val green = cubes.firstOrNull { it.contains("green") }?.replace(" green", "")?.trim()?.toInt() ?: 0
        val blue = cubes.firstOrNull { it.contains("blue") }?.replace(" blue", "")?.trim()?.toInt() ?: 0
        gameSets.add(GameSet(red, green, blue))
    }

    Game(id, gameSets)
}


data class GameSet(val red: Int, val green: Int, val blue: Int)
data class Game(val id: Int, val sets: List<GameSet>)