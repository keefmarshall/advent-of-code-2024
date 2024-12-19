private val directions = listOf(
    PointVector(0, -1),
    PointVector(1, 0),
    PointVector(0, 1),
    PointVector(-1, 0)
)

private fun parseInput(input: List<String>): List<Point> =
    input.map { line ->
        val parts = line.split(",")
        Point(parts[0].trim().toInt(), parts[1].trim().toInt())
    }

private fun initialiseMemorySpace(width: Int, height: Int, byteCoords: List<Point>) =
    List(height + 1) { y ->
        MutableList(width + 1) { x->
            when {
                (Point(x,y) in byteCoords) -> '#'
                else -> '.'
            }
        }.joinToString("")
    }

private fun findMinPath(memory: List<String>): Int {

    val start = Point(0,0)
    val goal = Point(memory[0].length - 1, memory.size - 1)

    data class Step(val pos: Point, val distance: Int)

    val nextSteps = ArrayDeque<Step>()
    val seen = mutableSetOf<Point>()

    nextSteps.add(Step(start, 0))
    while (nextSteps.isNotEmpty()) {
        val (pos, distance) = nextSteps.removeFirst()
        if (pos == goal) {
            return distance
        }

        if (pos.outOfBounds(memory) || memory.at(pos) == '#' || pos in seen) {
            continue
        }

        seen.add(pos)
        directions.forEach { dir ->
            nextSteps.add(Step(pos.moveBy(dir), distance + 1))
        }
    }

    throw Exception("No path found")
}

fun main() {
    val day = "18"

    fun part1(input: List<String>, width: Int, height: Int, max: Int): Int {
        val byteCoords = parseInput(input)
        val memory = initialiseMemorySpace(width, height, byteCoords.take(max))
        val result = findMinPath(memory)
        return result
    }

    fun part2(input: List<String>, width: Int, height: Int): Point {
        val byteCoords = parseInput(input)
        for (i in byteCoords.indices) {
            val memory = initialiseMemorySpace(width, height, byteCoords.take(i + 1))
            try {
                val result = findMinPath(memory)
            } catch (e: Exception) {
                return byteCoords[i]
            }
        }

        throw Exception("didn't find result")
    }

    // Or read a large test input from the `src/Day01_test.txt` file:
    val testInput = readInput("Day${day}_test")
    check(part1(testInput, 6, 6, 12) == 22)

    // Read the input from the `src/Day01.txt` file.
    val input = readInput("Day${day}")
    part1(input, 70, 70, 1024).println()


    check(part2(testInput, 6, 6) == Point(6,1))
    part2(input, 70, 70).println()
}
