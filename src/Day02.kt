import kotlin.math.abs

private fun levelsFromReport(report: String): List<Int> {
    return report.split(Regex("\\s+")).map { it.toInt() }
}

private fun reportIsSafe(report: String): Boolean {
    return reportIsSafe(levelsFromReport(report))
}

private fun reportIsSafe(levels: List<Int>) : Boolean {
    fun notInRange(value: Int): Boolean = abs(value) < 1 || abs(value) > 3

    var lastDiff = levels[0] - levels[1]
    if (notInRange(lastDiff)) {
        return false
    }

    for (i in 2 until levels.size) {
        val diff = levels[i-1] - levels[i]
        if ( (diff > 0 && lastDiff < 0) || (diff < 0 && lastDiff > 0) || notInRange(diff)  ) {
            return false
        }

        lastDiff = diff
    }

    return true
}


fun main() {
    val day = "02"

    fun part1(input: List<String>): Int {
        return input.count(::reportIsSafe)
    }

    fun part2(input: List<String>): Int {
        return input.fold(0) { acc, report ->
            val levels = levelsFromReport(report)
            if (reportIsSafe(levels)) {
                acc + 1
            } else {
                // remove one element at a time and test again
                if (levels.indices.any {
                        val dampenedLevels = ArrayList(levels).apply<ArrayList<Int>> { removeAt(it) }
                        reportIsSafe(dampenedLevels)
                    }) {
                    acc + 1 // found at least one safe variant
                } else {
                    acc // no safe variants found
                }
            }
        }
    }

    // Or read a large test input from the `src/Day01_test.txt` file:
    val testInput = readInput("Day${day}_test")
    check(part1(testInput) == 2)

    // Read the input from the `src/Day01.txt` file.
    val input = readInput("Day${day}")
    part1(input).println()


    check(part2(testInput) == 4)
    part2(input).println()
}