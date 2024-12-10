
private fun findAntennae(grid: List<String>): Map<Char, List<Point>> {
    val frequencyMap = mutableMapOf<Char, MutableList<Point>>()
    for (y in grid.indices) {
        for (x in grid[y].indices) {
            val freq = grid[y][x]
            if (freq != '.') {
                frequencyMap.getOrPut(freq) { mutableListOf() }.add(Point(x,y))
            }
        }
    }

    return frequencyMap
}

private fun findAntiNodes(nodes: List<Point>, grid: List<String>): List<Point> {
    val l = nodes.size - 1
    val antiNodes = mutableListOf<Point>()
    for (i in 0 until l) {
        for (j in i + 1 .. l) {
            val vector = vectorBetween(nodes[i], nodes[j])
            // we'll filter out of bounds ones at the end
            antiNodes.add(nodes[i].moveBackBy(vector))
            antiNodes.add(nodes[j].moveBy(vector))
        }
    }

    return antiNodes.filterNot { it.outOfBounds(grid) }
}

private fun findAntiNodes2(nodes: List<Point>, grid: List<String>): List<Point> {
    val l = nodes.size - 1
    val antiNodes = mutableListOf<Point>()
    for (i in 0 until l) {
        for (j in i + 1 .. l) {
            val vector = vectorBetween(nodes[i], nodes[j])
            var backNode = nodes[i]
            while (!backNode.outOfBounds(grid)) {
                antiNodes.add(backNode)
                backNode = backNode.moveBackBy(vector)
            }

            var forwardNode = nodes[j]
            while (!forwardNode.outOfBounds(grid)) {
                antiNodes.add(forwardNode)
                forwardNode = forwardNode.moveBy(vector)
            }
        }
    }

    return antiNodes
}


fun main() {
    val day = "08"

    fun part1(input: List<String>): Int {
        val frequencyMap = findAntennae(input)
        return frequencyMap.flatMap { (freq, nodes) ->
            findAntiNodes(nodes, input)
        }.distinct().size
    }

    fun part2(input: List<String>): Int {
        val frequencyMap = findAntennae(input)
        return frequencyMap.flatMap { (freq, nodes) ->
            findAntiNodes2(nodes, input)
        }.distinct().size
    }

    // Or read a large test input from the `src/Day01_test.txt` file:
    val testInput = readInput("Day${day}_test")
    check(part1(testInput) == 14)

    // Read the input from the `src/Day01.txt` file.
    val input = readInput("Day${day}")
    part1(input).println()


    check(part2(testInput) == 34)
    part2(input).println()
}
