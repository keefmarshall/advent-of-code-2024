
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

//// PART 2

private data class Block(val content: Int?, val size: Int)

private fun parseInput2(input: List<String>): List<Block> {
    val blocks = mutableListOf<Block>()

    input[0].indices.forEach {
        val size = input[0][it].toString().toInt()
        if (it % 2 == 0) { // file blocks
            blocks.add(Block(it / 2, size))
        } else { // free space
            blocks.add(Block(null, size))
        }
    }

    return blocks
}


private fun defrag2(blocks: List<Block>): List<Block> {
    var targetList: MutableList<Block> = ArrayList(blocks) // take a copy, make it mutable
    blocks.reversed().forEachIndexed { i, block ->
        if (block.content != null) { // && i < blocks.size - 1) {
            for (index in 1 until targetList.indexOf(block)) {
                val targetBlock = targetList[index]
                if (targetBlock.content != null || targetBlock.size < block.size) {
                    continue
                }

                // if we're here, we've found an empty block big enough.
                // remember to replace the source block in the target with space
                // (or the end game sums won't add up right)
                val oldPos = targetList.indexOf(block)
                targetList[oldPos] = Block(null, block.size)

                // now the actual block, replacing the empty slice:
                targetList[index] = block

                // if the empty slice was bigger, need to insert a smaller empty block after.
                if (targetBlock.size > block.size) {
                    targetList =
                        ArrayList(
                            targetList.slice(0 until index + 1)
                                    + Block(null, targetBlock.size - block.size)
                                    + targetList.slice(index + 1 until targetList.size)
                        )
                }

                break
            }
        }
    }

    return targetList
}

private fun createView2(blocks: List<Block>): List<Int?> {
    val view = mutableListOf<Int?>()

    blocks.forEach {
        if (it.size > 0) {
            for (i in 0 until it.size) {
                view.add(it.content)
            }
        }
    }

    return view
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

    fun part2(input: List<String>): Long {
        val blocks = parseInput2(input)
        val defragged = defrag2(blocks)
        val view = createView2(defragged)

        return view.indices.sumOf {
            if (view[it] != null) {
                it.toLong() * view[it]!!.toLong()
            } else {
                0
            }
        }
    }

    // Or read a large test input from the `src/Day01_test.txt` file:
    val testInput = readInput("Day${day}_test")
    check(part1(testInput) == 1928L)

    // Read the input from the `src/Day01.txt` file.
    val input = readInput("Day${day}")
    part1(input).println()


    check(part2(testInput) == 2858L)
    part2(input).println()
}
