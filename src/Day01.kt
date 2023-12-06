fun main() {
    d1p1()
    d1p2()
}

fun d1p1() {
    val input = readInput("day1")
    val onlyDigitRegex = Regex("[^0-9]")
    println(
        input
            .map { onlyDigitRegex.replace(it, "") }
            .mapNotNull { if (it.isNotEmpty()) it.first().toString() + it.last() else null }
            .sumOf { it.toInt() }
    )
}

fun d1p2() {
    val input = readInput("day1")
    val onlyDigitRegex = Regex("[^0-9]")
    println(
        input
            .map { it.replaceStringDigits() }
            .map { onlyDigitRegex.replace(it, "") }
            .map { it.first().toString() + it.last() }
            .sumOf { it.toInt() }
    )
}

fun String.replaceStringDigits(): String {
    if (startsWith("one")) return "1${substring(2)}".replaceStringDigits()
    if (startsWith("two")) return "2${substring(2)}".replaceStringDigits()
    if (startsWith("three")) return "3${substring(2)}".replaceStringDigits()
    if (startsWith("four")) return "4${substring(2)}".replaceStringDigits()
    if (startsWith("five")) return "5${substring(2)}".replaceStringDigits()
    if (startsWith("six")) return "6${substring(2)}".replaceStringDigits()
    if (startsWith("seven")) return "7${substring(2)}".replaceStringDigits()
    if (startsWith("eight")) return "8${substring(2)}".replaceStringDigits()
    if (startsWith("nine")) return "9${substring(2)}".replaceStringDigits()
    else if (this.length > 1) return this[0] + substring(1).replaceStringDigits()
    else return this
}