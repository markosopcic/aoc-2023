import java.math.BigInteger
import java.security.MessageDigest
import kotlin.io.path.Path
import kotlin.io.path.readLines
import kotlin.system.measureTimeMillis

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = Path("src/$name.txt").readLines()

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

/**
 * The cleaner shorthand for printing output.
 */
fun Any?.println() = println(this)


/**
 * measures time for code block to execute and prints duration in millis.
 */
fun time(code: () -> Unit) {
    measureTimeMillis {
        code()
    }.also {
        println("$it ms")
    }
}

data class TreeNode<T>(val value: T, var left: TreeNode<T>?, var right: TreeNode<T>?)

fun <T> Iterable<T>.combinations(length: Int): Sequence<List<T>> =
    sequence {
        val pool = this@combinations as? List<T> ?: toList()
        val n = pool.size
        if (length > n) return@sequence
        val indices = IntArray(length) { it }
        while (true) {
            yield(indices.map { pool[it] })
            var i = length
            do {
                i--
                if (i == -1) return@sequence
            } while (indices[i] == i + n - length)
            indices[i]++
            for (j in i + 1 until length) indices[j] = indices[j - 1] + 1
        }
    }

fun CharArray.diffIndices(other: CharArray): List<Int> {
    assert(this.size == other.size)

    return indices.filter { this[it] != other[it] }
}

enum class Direction {
    Up, Down, Left, Right;

    val oposite
        get() = when(this){
            Up -> Down
            Down -> Up
            Left -> Right
            Right -> Left
        }
}