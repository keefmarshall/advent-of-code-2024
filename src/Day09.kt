
private fun parseInput(input: List<String>): List<Int?> {
    // empty slot is null in the output
    // NB input should only have one line
    val blocks = mutableListOf<Int?>()

    fun addBlocks(value: Int?, size: Int) {
        for (i in 0 until size) {
            blocks.add(value)
        }
    }

    input[0].indices.forEach {
        val size = input[0][it].toString().toInt()
        if (it % 2 == 0) { // file blocks
            addBlocks(it / 2, size)
        } else { // free space
            addBlocks(null, size)
        }
    }

    return blocks
}

private fun defrag(blocks: List<Int?>): List<Int?> {
    val defragged = ArrayList(blocks) // take copy so we can edit it
    while (defragged.contains(null)) {
        val blockToMove = defragged.removeLast() ?: continue
        val firstFree = defragged.indexOf(null)
        if (firstFree >= 0) {
            defragged[firstFree] = blockToMove
        }
    }

    return defragged
}

fun main() {
    val day = "09"

    fun part1(input: List<String>): Long {
        val blocks = parseInput(input)
        val defragged = defrag(blocks)
        return defragged.indices.sumOf {
            it.toLong() * defragged[it]!!.toLong()
        }
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // Or read a large test input from the `src/Day01_test.txt` file:
    val testInput = readInput("Day${day}_test")
    check(part1(testInput) == 1928L)

    // Read the input from the `src/Day01.txt` file.
    val input = readInput("Day${day}")
    part1(input).println()


    check(part2(testInput) == 31)
    part2(input).println()
}
