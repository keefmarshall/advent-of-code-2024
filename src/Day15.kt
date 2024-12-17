
private data class Warehouse(val map: MutableList<MutableList<Char>>, val moves: String)

private fun directions(dir: Char) = when(dir) {
    '^' -> PointVector(0, -1)
    '>' -> PointVector(1, 0)
    '<' -> PointVector(-1, 0)
    'v' -> PointVector(0, 1)
    else -> throw Exception("Unknown direction $dir")
}


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

private fun doMoves(warehouse: Warehouse) {
    var robotPos = findRobot(warehouse)

    fun List<List<Char>>.at(p: Point) = this[p.y][p.x]

    // This mutates the warehouse map accordingly
    fun doMove(move: Char) {
        val direction = directions(move)

        // check if move can happen (look for empty square before wall)
        var content = warehouse.map.at(robotPos)
        var currentPos = robotPos
        while(true) {
            currentPos = currentPos.moveBy(direction)
            if (warehouse.map.at(currentPos) == '#') {
                return // no move possible
            } else if (warehouse.map.at(currentPos) == '.') {
                break // we can move
            }
        }

        var lastPos = currentPos
        while(currentPos != robotPos) {
            currentPos = currentPos.moveBackBy(direction)
            warehouse.map[lastPos.y][lastPos.x] = warehouse.map[currentPos.y][currentPos.x]
            lastPos = currentPos
        }

        warehouse.map[currentPos.y][currentPos.x] = '.'
        robotPos = robotPos.moveBy(direction)
    }

    warehouse.moves.forEach { doMove(it) }
}

private fun sumBoxes(warehouse: Warehouse) = warehouse.map.indices.sumOf { y ->
    warehouse.map[y].indices
        .filter { warehouse.map[y][it] == 'O' }
        .sumOf { x -> (100 * y) + x}
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
        return input.size
    }

    // Or read a large test input from the `src/Day01_test.txt` file:
    val testInput = readInput("Day${day}_test")
    check(part1(testInput) == 2028)
    val testInput2 = readInput("Day${day}_test2")
    check(part1(testInput2) == 10092)

    // Read the input from the `src/Day01.txt` file.
    val input = readInput("Day${day}")
    part1(input).println()


    check(part2(testInput) == 31)
    part2(input).println()
}
