
private data class Equation(val total: Long, val numbers: List<Long>)

private object Operations {
    fun plus(n1: Long, n2: Long) = n1 + n2
    fun multiply(n1: Long, n2: Long) = n1 * n2
    fun concat(n1: Long, n2: Long) = (n1.toString() + n2.toString()).toLong()
}


private fun parseInput(input: List<String>): List<Equation> {
    return input.map { line ->
        val parts = line.split(": ")
        val total = parts[0].trim().toLong()
        val numbers = parts[1].trim().split(Regex("\\s+")).map(String::toLong)
        Equation(total, numbers)
    }
}

/** returns the number of ways this evaluates to true */
private fun validate(eq: Equation, operations: List<(Long, Long) -> Long>): Int {
    if (eq.numbers.size == 1) {
        return when {
            eq.total == eq.numbers[0] -> 1
            else -> 0
        }
    }

    return operations.sumOf { op ->
        val newFirst = op(eq.numbers[0], eq.numbers[1])
        validate(Equation(eq.total, listOf(newFirst) + eq.numbers.drop(2)), operations)
    }
}

fun main() {
    val day = "07"

    fun part1(input: List<String>): Long {
        val equations = parseInput(input)
        val operations = listOf(Operations::plus, Operations::multiply)
        return equations.filter { validate(it, operations) > 0 }.sumOf { it.total }
    }

    fun part2(input: List<String>): Long {
        val equations = parseInput(input)
        val operations = listOf(Operations::plus, Operations::multiply, Operations::concat)
        return equations.filter { validate(it, operations) > 0 }.sumOf { it.total }
    }

    // Or read a large test input from the `src/Day01_test.txt` file:
    val testInput = readInput("Day${day}_test")
    check(part1(testInput) == 3749L)

    // Read the input from the `src/Day01.txt` file.
    val input = readInput("Day${day}")
    part1(input).println()


    check(part2(testInput) == 11387L)
    part2(input).println()
}
