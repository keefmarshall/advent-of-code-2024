
private data class Puzzle(val towels: List<String>, val patterns: List<String>)

private fun parseInput(input: List<String>) = Puzzle (
    input[0].split(", "),
    input.subList(2, input.size)
)

private val cache = mutableMapOf<String, Long>()

private fun makePattern(pattern: String, towels: List<String>): Long {
    if (pattern.isEmpty()) {
        return 1L // we made it!
    }

    if (pattern in cache) {
        return cache[pattern]!!
    }

    val result = towels.sumOf { towel ->
        if (pattern.startsWith(towel)) {
            makePattern(pattern.substring(towel.length, pattern.length), towels)
        } else {
            0L
        }
    }

    cache[pattern] = result
    return result
}

fun main() {
    val day = "19"

    fun part1(input: List<String>): Int {
        cache.clear()
        val puzzle = parseInput(input)
        val count = puzzle.patterns.sumOf {
            if (makePattern(it, puzzle.towels) > 0) 1 as Int else 0 as Int
        }
        return count
    }

    fun part2(input: List<String>): Long {
        cache.clear()
        val puzzle = parseInput(input)
        val count = puzzle.patterns.sumOf {
            makePattern(it, puzzle.towels)
        }
        return count
    }

    // Or read a large test input from the `src/Day01_test.txt` file:
    val testInput = readInput("Day${day}_test")
    check(part1(testInput) == 6)

    // Read the input from the `src/Day01.txt` file.
    val input = readInput("Day${day}")
    part1(input).println()


    check(part2(testInput) == 16L)
    part2(input).println()
}
