import java.math.BigInteger

fun main() {
    time {
        d8p1()
    }

    time {
        d8p2()
    }
}

fun d8p1() {
    val input = readInput("day08")
    val instructions = input.first().toCharArray().toList()
    val nodes = parseTree(input.drop(2))
    val firstNode = nodes.first { it.value == "AAA" }

    calculate(firstNode, instructions, 0, 0) { it == "ZZZ" }.counter.println()
}

fun d8p2() {
    val input = readInput("day08")
    val instructions = input.first().toCharArray().toList()
    val nodes = parseTree(input.drop(2))

    val values = nodes.filter { it.value.endsWith("A") }.map { calculate(it, instructions, 0, 0L) { it.last() == 'Z' } }.map {
        it.counter.toBigInteger()
    }

    lcmOfList(values).println()
}

fun gcd(a: BigInteger, b: BigInteger): BigInteger {
    return if (b == BigInteger.ZERO) a else gcd(b, a % b)
}

// Function to calculate the least common multiple (LCM) of two numbers
fun lcm(a: BigInteger, b: BigInteger): BigInteger {
    return if (a == BigInteger.ZERO || b == BigInteger.ZERO) BigInteger.ZERO else (a * b).abs() / gcd(a, b)
}

// Function to calculate the LCM of a list of numbers
fun lcmOfList(numbers: List<BigInteger>): BigInteger {
    return numbers.reduce { acc, num -> lcm(acc, num) }
}

data class RecurseData(val node: TreeNode<String>, val instructionIndex: Int, val counter: Long)

fun calculate(
    node: TreeNode<String>,
    instructions: List<Char>,
    initialInstructionIndex: Int,
    initialCounter: Long,
    endCondition: (String) -> Boolean
): RecurseData {
    var instructionIndex = initialInstructionIndex
    var counter = initialCounter
    var currNode = node
    do {
        currNode = if (instructions[instructionIndex] == 'L') {
            currNode.left!!
        } else {
            currNode.right!!
        }
        counter++
        instructionIndex = (instructionIndex + 1) % instructions.size
    } while (!endCondition(currNode.value))

    return RecurseData(currNode, instructionIndex, counter)
}

fun parseTree(lines: List<String>): List<TreeNode<String>> {
    val childMap = mutableMapOf<TreeNode<String>, Pair<String, String>>()
    val nodeMap = mutableMapOf<String, TreeNode<String>>()
    lines.forEach {
        val parts = it.split("=").map { it.trim() }
        val node = TreeNode(parts[0], null, null)
        val childNames = parts[1].replace("(", "").replace(")", "").split(",").map { it.trim() }

        val left = childNames[0]
        val right = childNames[1]
        childMap[node] = left to right
        nodeMap[parts[0]] = node
    }

    childMap.onEach {
        it.key.left = nodeMap[it.value.first]
        it.key.right = nodeMap[it.value.second]
    }

    return nodeMap.values.toList()
}