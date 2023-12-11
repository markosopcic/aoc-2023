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