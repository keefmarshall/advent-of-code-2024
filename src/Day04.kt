private val WORD = "XMAS"
private val DIRECTIONS = arrayOf(
    PointVector(1, 0),
    PointVector(1, 1),
    PointVector(0, 1),
    PointVector(-1, 1),
    PointVector(-1, 0),
    PointVector(-1, -1),
    PointVector(0, -1),
    PointVector(1, -1)
)

private val DIAGONALS = arrayOf(
    PointVector(1, 1),
    PointVector(-1, 1),
    PointVector(-1, -1),
    PointVector(1, -1)
)

private fun checkForWord(start: Point, grid: List<String>): Int {
    var found = 0
    outer@ for (dir in DIRECTIONS) {
        var next = start
        for (i in 1 until WORD.length) {
            next = next.moveBy(dir)
            if (next.outOfBounds(grid) || grid[next.y][next.x] != WORD[i]) {
                continue@outer
            }
        }

        found ++
    }

    return found
}

private fun checkForX(start: Point, grid: List<String>): Boolean {
    // must match 2, or it isn't a cross. Can't possibly match more than 2.
    var found = 0
    for (dir in DIAGONALS) {
        val forward = start.moveBy(dir)
        if (forward.outOfBounds(grid) || grid[forward.y][forward.x] != 'S') {
            continue
        }

        val backward = start.moveBackBy(dir)
        if (backward.outOfBounds(grid) || grid[backward.y][backward.x] != 'M') {
            continue
        }
        found ++
    }

    return found == 2
}


fun main() {
    val day = "04"

    fun part1(input: List<String>): Int {
        val width = input[0].length
        val height = input.size
        var count = 0
        for (x in 0 until width) {
            for (y in 0 until height) {
                if (input[y][x] == WORD[0]) {
                    count += checkForWord(Point(x, y), input)
                }
            }
        }

        return count
    }

    fun part2(input: List<String>): Int {
        val width = input[0].length
        val height = input.size
        var count = 0
        for (x in 0 until width) {
            for (y in 0 until height) {
                if (input[y][x] == 'A') {
                    if (checkForX(Point(x, y), input)) {
                        count ++
                    }
                }
            }
        }

        return count
    }

    // Or read a large test input from the `src/Day01_test.txt` file:
    val testInput = readInput("Day${day}_test")
    check(part1(testInput) == 18)

    // Read the input from the `src/Day01.txt` file.
    val input = readInput("Day${day}")
    part1(input).println()


    check(part2(testInput) == 9)
    part2(input).println()
}
