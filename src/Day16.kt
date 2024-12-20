

private fun find(item: Char, grid: List<String>): Point {
    for (y in grid.indices) {
        for (x in grid[y].indices) {
            if (grid[y][x] == item) {
                return Point(x, y)
            }
        }
    }

    throw Exception("404 '$item' not found")
}

private data class Result(val scores: List<Int>, val positionsForScore: Map<Int, Set<Point>>)

private fun findAllPaths(grid: List<String>): Result {
    val start = find('S', grid)
    val end = find('E', grid)

    val startDirection = Direction.RIGHT
    val startScore = 0

    data class Vector(val point: Point, val dir: Direction)
    val minScoresAtVector = mutableMapOf<Vector, Int>()

    val positionsForScores = mutableMapOf<Int, MutableSet<Point>>()
    val successfulScores = mutableListOf<Int>()

    data class Step(val pos: Point, val dir: Direction, val score: Int, val been: Set<Point>)

    val nextSteps = ArrayDeque<Step>()
    nextSteps.add(Step(start, startDirection, startScore, emptySet()))

    while (nextSteps.isNotEmpty()) {
        val (pos, dir, score, been) = nextSteps.removeFirst()

        if (pos == end) {
            positionsForScores.getOrPut(score, { mutableSetOf() }).addAll(been + pos)
            successfulScores.add(score)
            continue
        }

        if (pos in been || grid.at(pos) == '#') {
            continue
        }

        if (Vector(pos, dir) in minScoresAtVector) {
            if (minScoresAtVector[Vector(pos, dir)]!! < score) // we already got here more efficiently
                continue
        }

        minScoresAtVector[Vector(pos, dir)] = score

        // forward:
        nextSteps.add(Step(pos.moveBy(dir.move), dir, score + 1, been + pos))
        // turn right:
        nextSteps.add(Step(pos.moveBy(turnRight(dir).move), turnRight(dir), score + 1001, been + pos))
        // turn left:
        nextSteps.add(Step(pos.moveBy(turnLeft(dir).move), turnLeft(dir), score + 1001, been + pos))
    }

    return Result(successfulScores, positionsForScores)
}

fun main() {
    val day = "16"

    fun part1(input: List<String>): Int {
        val result = findAllPaths(input)
        return result.scores.min()
    }

    fun part2(input: List<String>): Int {
        val result = findAllPaths(input)
        val minScore = result.scores.min()
        return result.positionsForScore[minScore]?.size ?: 0
    }

    // Or read a large test input from the `src/Day01_test.txt` file:
    val testInput = readInput("Day${day}_test")
    check(part1(testInput) == 7036)
    val testInput2 = readInput("Day${day}_test2")
    check(part1(testInput2) == 11048)

    // Read the input from the `src/Day01.txt` file.
    val input = readInput("Day${day}")
    part1(input).println()


    check(part2(testInput) == 45)
    check(part2(testInput2) == 64)
    part2(input).println()
}
