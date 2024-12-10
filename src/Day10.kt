
private val directions = listOf(
    Point(0, -1),
    Point(1, 0),
    Point(0, 1),
    Point(-1, 0)
)


private fun findTrailheads(grid: List<String>): List<Point> {
    val trailheads = mutableListOf<Point>()
    for (y in grid.indices) {
        for (x in grid[y].indices) {
            if (grid[y][x] == '0') {
                trailheads.add(Point(x,y))
            }
        }
    }

    return trailheads
}

// returns set of summits visited
private fun findTrails(grid: List<String>, start: Point): List<Point> {
    if (grid.at(start) == '9') {
        return listOf(start)
    }

    return directions
        .map { start.moveBy(it) }
        .filterNot { it.outOfBounds(grid) }
        .filter { grid.at(it) == grid.at(start) + 1 }
        .flatMap { findTrails(grid, it) }
}

fun main() {
    val day = "10"

    fun part1(input: List<String>): Int {
        val trailheads = findTrailheads(input)
        val result = trailheads.sumOf { trailhead ->
            findTrails(input, trailhead).toSet().size
        }
        return result
    }

    fun part2(input: List<String>): Int {
        val trailheads = findTrailheads(input)
        val result = trailheads.sumOf { trailhead ->
            findTrails(input, trailhead).size
        }
        return result
    }

    // Or read a large test input from the `src/Day01_test.txt` file:
    val testInput = readInput("Day${day}_test")
    check(part1(testInput) == 36)

    // Read the input from the `src/Day01.txt` file.
    val input = readInput("Day${day}")
    part1(input).println()


    check(part2(testInput) == 81)
    part2(input).println()
}
