private data class Grid(val width: Int, val height: Int)

private data class Robot(val start: Point, val velocity: PointVector) {
    var position = start

    fun wrap(pos: Int, max: Int) =
        if (pos >= max) {
            pos - max
        } else if (pos < 0) {
            pos + max
        } else {
            pos
        }

    fun move(grid: Grid) {
        val newX = wrap(position.x + velocity.x, grid.width)
        val newY = wrap(position.y + velocity.y, grid.height)
        position = Point(newX, newY)
    }
}

private fun parseInput(input: List<String>): List<Robot> =
    input.mapNotNull { line ->
        val matchResult = Regex("p=(\\d+),(\\d+) v=(-?\\d+),(-?\\d+)").find(line)
        if (matchResult != null) {
            val (sx, sy, vx, vy) = matchResult.destructured
            Robot(Point(sx.toInt(), sy.toInt()), PointVector(vx.toInt(), vy.toInt()))
        } else {
            null
        }
    }


private fun safetyFactor(robots: List<Robot>, grid: Grid): Long {
    data class Quadrant(val start: Point, val end: Point) {
        fun contains(p: Point): Boolean =
            p.x >= start.x && p.y >= start.y && p.x < end.x && p.y < end.y
    }

    val quadrants = listOf(
        Quadrant(Point(0,0), Point(grid.width / 2, grid.height / 2)),
        Quadrant(Point(0,grid.height/2 + 1), Point(grid.width / 2, grid.height)),
        Quadrant(Point(grid.width / 2 + 1,0), Point(grid.width, grid.height / 2)),
        Quadrant(Point(grid.width / 2 + 1,grid.height / 2 + 1), Point(grid.width, grid.height)),
    )

    return quadrants
        .map { q ->
            robots.sumOf { (if (q.contains(it.position)) 1 else 0).toLong() }
        }
        .fold(1, Long::times)
}

private fun printRobots(robots: List<Robot>, grid: Grid) =
    repeat(grid.height) { y ->
        repeat(grid.width) { x->
            val num = robots.sumOf { if (it.position == Point(x, y)) 1L else 0L }
            print(if (num == 0L) "." else num % 10)
        }
        println()
    }

private fun findLineOfRobots(robots: List<Robot>, grid: Grid): Int {
    var max = 0
    repeat(grid.height) { y ->
        var count = 0
        repeat(grid.width) { x ->
            val found = robots.any { it.position == Point(x, y) }
            if (found) {
                count++
                if (count > max) max = count
            } else {
                count = 0
            }
        }
    }

    return max
}

private fun waitForKey(): Boolean {
    val line = readln()
    return line != "q"
}

fun main() {
    val day = "14"

    fun part1(input: List<String>, grid: Grid): Long {
        val robots = parseInput(input)
        repeat(100) {
            robots.forEach { it.move(grid) }
        }
        return safetyFactor(robots, grid)
    }

    fun part2(input: List<String>, grid: Grid) {
        val robots = parseInput(input)
        printRobots(robots, grid)
        var count = 1L
        var cont = true
        while (cont) {
            robots.forEach { it.move(grid) }
            if (findLineOfRobots(robots, grid) > 10) {
                printRobots(robots, grid)
                println(count)
                cont = waitForKey()
            }
            count ++
            if (count % 1000 == 0L) println("Running, $count")
        }

    }

    // Or read a large test input from the `src/Day01_test.txt` file:
    val testInput = readInput("Day${day}_test")
    val testGrid = Grid(11, 7)
    check(part1(testInput, testGrid) == 12L)

    // Read the input from the `src/Day01.txt` file.
    val input = readInput("Day${day}")
    val realGrid = Grid(101, 103)
    part1(input, realGrid).println()


//    part2(testInput, testGrid)
    part2(input, realGrid)
}
