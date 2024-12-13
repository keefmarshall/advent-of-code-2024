
private data class Machine(
    val buttonA: PointVector,
    val buttonB: PointVector,
    val prize: Point
)


private fun parseInput(grid: List<String>): List<Machine> {

    fun parseButton(line: String): PointVector {
        // Button A: X+94, Y+34
        val matchResult = Regex("Button [AB]: X\\+(\\d+), Y\\+(\\d+)\\s*").find(line)
        val (x, y) = matchResult!!.destructured

        return PointVector(x.toInt(), y.toInt())
    }

    fun parsePrize(line: String): Point {
        // Prize: X=12748, Y=12176
        val matchResult = Regex("Prize: X=(\\d+), Y=(\\d+)\\s*").find(line)
        val (x, y) = matchResult!!.destructured

        return Point(x.toInt(), y.toInt())
    }

    val machines = mutableListOf<Machine>()
    for (m in 0 until grid.size step 4) {
        val buttonA = parseButton(grid[m])
        val buttonB = parseButton(grid[m + 1])
        val prize = parsePrize(grid[m + 2])
        val machine = Machine(buttonA, buttonB, prize)
        machines.add(machine)
    }

    return machines
}

const val MAX_COST = 1000 // higher than can be reached

private fun cheapestPrize(machine: Machine): Int {
    var cost = MAX_COST
    repeat(100 ) { a ->
        repeat(100) { b ->
            val pos = Point(
                machine.buttonA.x * a + machine.buttonB.x * b,
                machine.buttonA.y * a +  machine.buttonB.y * b
            )
            if (pos == machine.prize) {
                cost = minOf(cost, 3 * a + b )
            }
        }
    }

    return if (cost == MAX_COST) 0 else cost
}


fun main() {
    val day = "13"

    fun part1(input: List<String>): Int {
        val machines = parseInput(input)
        val result = machines.sumOf { cheapestPrize(it) }
        return result
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // Or read a large test input from the `src/Day01_test.txt` file:
    val testInput = readInput("Day${day}_test")
    check(part1(testInput) == 480)

    // Read the input from the `src/Day01.txt` file.
    val input = readInput("Day${day}")
    part1(input).println()


    check(part2(testInput) == 31)
    part2(input).println()
}
