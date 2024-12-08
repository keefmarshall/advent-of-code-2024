private fun evalMulExpression(mulExpression: String): Long {
    val numbers = Regex("""mul\((\d+),(\d+)\)""").find(mulExpression)?.groupValues ?: listOf("0", "0")

    return if (numbers.size == 3) {
        numbers[1].toLong() * numbers[2].toLong()
    } else {
        0L
    }
}


fun main() {
    val day = "03"

    fun part1(input: List<String>): Long {
        val mulPattern = Regex("""mul\(\d+,\d+\)""")
        return mulPattern
            .findAll(input.joinToString(" "))
            .map { evalMulExpression(it.value) }
            .sum()
    }

    fun part2(input: List<String>): Long {
        val mulPattern = Regex("""(mul\(\d+,\d+\))|(don't\(\))|(do\(\))""")
        val expressions = mulPattern
            .findAll(input.joinToString(" "))
            .map { it.value }
            .toList()

        var doing = true
        var result = 0L
        expressions.forEach { expression ->
            if (expression.startsWith("mul") && doing) {
                result += evalMulExpression(expression)
            } else if (expression.startsWith("don't")) {
                doing = false
            } else if (expression.startsWith("do(")) {
                doing = true
            } // else ignore
        }

        return result
    }

    // Or read a large test input from the `src/Day01_test.txt` file:
    val testInput = readInput("Day${day}_test")
    check(part1(testInput) == 161L)

    // Read the input from the `src/Day01.txt` file.
    val input = readInput("Day${day}")
    part1(input).println()

    // this one has different test input for part 2
    val testInput2 = readInput("Day${day}_test2")
    check(part2(testInput2) == 48L)
    part2(input).println()
}
