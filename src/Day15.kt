private data class Warehouse(val map: MutableList<MutableList<Char>>, val moves: String)

private fun directions(dir: Char) = when(dir) {
    '^' -> PointVector(0, -1)
    '>' -> PointVector(1, 0)
    '<' -> PointVector(-1, 0)
    'v' -> PointVector(0, 1)
    else -> throw Exception("Unknown direction $dir")
}

private val upAndDownDirections = setOf(directions('^'), directions('v'))

private fun parseInput(lines: List<String>): Warehouse {
    val map = mutableListOf<MutableList<Char>>()
    var i = 0

    // grid
    while(lines[i].isNotEmpty()) {
        map.add(lines[i].toMutableList())
        i++
    }

    // gap
    while(lines[i].isEmpty()) {
        i++
    }

    // moves
    val moveLines = mutableListOf<String>()
    while(i < lines.size && lines[i].isNotEmpty()) {
        moveLines.add(lines[i].trim())
        i++
    }

    return Warehouse(map, moveLines.joinToString(""))
}

private fun findRobot(warehouse: Warehouse): Point {
    for (y in warehouse.map.indices) {
        for (x in warehouse.map[y].indices) {
            if (warehouse.map[y][x] == '@') {
                return Point(x, y)
            }
        }
    }

    throw Exception("404: Robot not found")
}

private fun sumBoxes(warehouse: Warehouse, boxChar: Char = 'O') = warehouse.map.indices.sumOf { y ->
    warehouse.map[y].indices
        .filter { warehouse.map[y][it] == boxChar }
        .sumOf { x -> (100 * y) + x}
}

private data class ObjectMove(val from: Point, val to: Point, val value: Char)
private class BlockedException : Exception()

private fun doMoves(warehouse: Warehouse) {

    val seen = mutableListOf<Point>()

    fun predictObjectMove(from: Point, direction: PointVector): List<ObjectMove> {
        if (from in seen) {
            return emptyList()
        }

        seen.add(from)

        val to = from.moveBy(direction)
        val fromValue = warehouse.map[from.y][from.x]
        val toValue = warehouse.map[to.y][to.x]

        if (toValue == '#') {
            throw BlockedException() // no move possible
        }

        // if we're here, the move is possible (for now - might get blocked further down)
        val predictedMoves = mutableListOf<ObjectMove>(ObjectMove(from, to, fromValue))

        // if we're moving up or down, and this is a large box, we need to consider the other half of the box
        if (direction in upAndDownDirections) {
            if (fromValue == '[') {
                val otherSide = from.moveBy(directions('>'))
                predictedMoves.addAll(predictObjectMove(otherSide, direction))
            } else if (fromValue == ']') {
                val otherSide = from.moveBy(directions('<'))
                predictedMoves.addAll(predictObjectMove(otherSide, direction))
            }
        }

        if (toValue != '.') {
            predictedMoves.addAll(predictObjectMove(to, direction))
        }

        return predictedMoves
    }

    fun executeMoves(objectMoves: List<ObjectMove>) {
        // Do this in two passes to avoid overwriting
        // first set all affected 'from' squares to '.'
        objectMoves.forEach { warehouse.map[it.from.y][it.from.x] = '.' }
        // now set 'to' to the value:
        objectMoves.forEach { warehouse.map[it.to.y][it.to.x] = it.value }
    }

    var robotPos = findRobot(warehouse)
    warehouse.moves.forEach {
        seen.clear()
        val direction = directions(it)
        try {
            val predictedMoves = predictObjectMove(robotPos, direction)
            executeMoves(predictedMoves)
            robotPos = robotPos.moveBy(direction)
        } catch (e: BlockedException) {
            // continue the loop without moving
        }
    }
}

private fun expandWarehouse(oldHouse: Warehouse): Warehouse {
    return Warehouse(
        oldHouse.map.map { row ->
            row.flatMap {
                when (it) {
                    '@' -> listOf('@', '.')
                    'O' -> listOf('[', ']')
                    else -> listOf(it, it)
                }
            }.toMutableList()
        }.toMutableList(),
        oldHouse.moves
    )
}

fun main() {
    val day = "15"

    fun part1(input: List<String>): Int {
        val warehouse = parseInput(input)
        doMoves(warehouse)
        val sum = sumBoxes(warehouse)
        return sum
    }

    fun part2(input: List<String>): Int {
        val warehouse = expandWarehouse(parseInput(input))
        doMoves(warehouse)
        val sum = sumBoxes(warehouse, '[')
        return sum
    }

    // Or read a large test input from the `src/Day01_test.txt` file:
    val testInput = readInput("Day${day}_test")
    check(part1(testInput) == 2028)
    val testInput2 = readInput("Day${day}_test2")
    check(part1(testInput2) == 10092)

    // Read the input from the `src/Day01.txt` file.
    val input = readInput("Day${day}")
    part1(input).println()


    check(part2(testInput2) == 9021)
    part2(input).println()
}
