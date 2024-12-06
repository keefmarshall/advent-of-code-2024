import kotlin.math.abs

fun main() {
    fun part1(input: List<String>): Int {
        val list1 = mutableListOf<Int>()
        val list2 = mutableListOf<Int>()
        for (line in input) {
            val parts = line.split(Regex("\\s+"), 2).map { it.trim().toInt() }
            list1.add(parts[0])
            list2.add(parts[1])
        }

        list1.sort()
        list2.sort()

        var sum = 0
        for (i in list1.indices) {
            sum += abs (list1[i] - list2[i])
        }
        return sum
    }

    fun part2(input: List<String>): Long {
        val list1 = mutableListOf<Int>()
        val list2Counts = HashMap<Int, Int>()
        for (line in input) {
            val parts = line.split(Regex("\\s+"), 2).map { it.trim().toInt() }
            list1.add(parts[0])
            list2Counts[parts[1]] = list2Counts.getOrDefault(parts[1], 0) + 1
        }

        var sum = 0L
        for (num in list1) {
            sum += num * list2Counts.getOrDefault(num, 0)
        }

        return sum
    }

    // Or read a large test input from the `src/Day01_test.txt` file:
    val testInput = readInput("Day01_test")
    check(part1(testInput) == 11)

    // Read the input from the `src/Day01.txt` file.
    val input = readInput("Day01")
    part1(input).println()


    check(part2(testInput) == 31L)
    part2(input).println()
}
