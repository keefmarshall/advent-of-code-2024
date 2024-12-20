
private fun findStartPos(grid: List<String>): Point {
    for (y in grid.indices) {
        val x = grid[y].indexOf('^')
        if (x >= 0) {
            return Point(x, y)
        }
    }

    throw Exception("Can't find start position!")
}

fun main() {
    val day = "06"

    fun part1(input: List<String>): Int {
        var direction = Direction.UP
        var position = findStartPos(input)
        val visited = mutableSetOf<Point>()
        while(true) {
            visited.add(position)
            val nextPos = position.moveBy(direction.move)
            when {
                nextPos.outOfBounds(input) -> // we're done
                    return visited.size
                input.at(nextPos) == '#' -> // turn
                    direction = turnRight(direction)
                else -> // move
                    position = nextPos
            }
        }
    }

    fun part2(input: List<String>): Int {
        data class VectorPos(val pos: Point, val dir: Direction)

        var direction: Direction
        var position: Point
        var loops = 0
        val visited = mutableSetOf<VectorPos>()

        val startPos = findStartPos(input)
        val grid = ArrayList(input) // mutable copy

        for (y in input.indices) {
            for (x in input[0].indices) {
                if (grid.at(Point(x,y)) != '.') {
                    continue
                }

                val tmp = StringBuilder(grid[y])
                tmp.setCharAt(x,'#')
                grid[y] = tmp.toString()

                position = startPos
                direction = Direction.UP
                visited.clear()

                while (true) {
                    visited.add(VectorPos(position, direction))
                    val nextPos = position.moveBy(direction.move)
                    when {
                        VectorPos(nextPos, direction) in visited -> { // it's a loop
                            loops++
                            break
                        }

                        nextPos.outOfBounds(input) -> // not a loop
                            break

                        grid.at(nextPos) == '#' -> // turn
                            direction = turnRight(direction)

                        else -> // move
                            position = nextPos
                    }
                }

                tmp.setCharAt(x,'.')
                grid[y] = tmp.toString()
            }
        }

        return loops
    }

    // Or read a large test input from the `src/Day01_test.txt` file:
    val testInput = readInput("Day${day}_test")
    check(part1(testInput) == 41)

    // Read the input from the `src/Day01.txt` file.
    val input = readInput("Day${day}")
    part1(input).println()


    check(part2(testInput) == 6)
    part2(input).println()
}
