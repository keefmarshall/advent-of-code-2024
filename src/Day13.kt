import kotlin.math.round

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
    for (m in grid.indices step 4) {
        val buttonA = parseButton(grid[m])
        val buttonB = parseButton(grid[m + 1])
        val prize = parsePrize(grid[m + 2])
        val machine = Machine(buttonA, buttonB, prize)
        machines.add(machine)
    }

    return machines
}

private fun cheapestPrize(machine: Machine): Int {
    repeat(100 ) { b ->
        repeat(100) { a ->
            val pos = Point(
                machine.buttonA.x * a + machine.buttonB.x * b,
                machine.buttonA.y * a + machine.buttonB.y * b
            )
            if (pos == machine.prize) {
                return 3 * a + b
            }
        }
    }

    return 0
}

private fun cheapestPrice2(machine: Machine, extra: Long = 0): Long {

    val adjustedPrizeX = machine.prize.x + extra
    val adjustedPrizeY = machine.prize.y + extra

    // This is a geometry thing - intersection of two straight lines.
    // Line A is y = ax where a = buttonA.y / buttonA.x - there's no offset as it passes through (0,0)
    // Line B needs a linear offset to pass through the target point so:
    // y = bx + d where b = buttonB.y / buttonB.x and d is calculated:
    //  d = prize.y - (b * prize.x)

    val a = machine.buttonA.y.toDouble() / machine.buttonA.x.toDouble()
    val b = machine.buttonB.y.toDouble() / machine.buttonB.x.toDouble()
    val d = adjustedPrizeY.toDouble() - (b * adjustedPrizeX.toDouble())

    // for the intersection see: https://en.wikipedia.org/wiki/Line%E2%80%93line_intersection#Given_two_line_equations
    // noting that 'c' is 0 in our case
    // x = ((d - c) / (a - b))
    // NB if a == b the lines are parallel so the lines can never meet
    // [technically there's an edge case here if one button can reach the prize by itself with
    // zero presses of the other.. even if they're parallel .. but doesn't happen with my input]
    if (a - b == 0.0) {
        return 0
    }

    val intersectX = round(d / (a - b)).toLong()
    val intersectY = round((b * intersectX) + d).toLong()

    // If the intersection is not between (0,0) and adjustedPrize then it can't be done:
    if (intersectX < 0 || intersectX > adjustedPrizeX || intersectY < 0 || intersectY > adjustedPrizeY) {
        return 0
    }

    // OK now we have to figure out how many A moves and how many B moves to calculate the cost
    // Also we've been working in floats but the machine works in integers so let's double check
    val xdeltaA = intersectX
    val xdeltaB = adjustedPrizeX - intersectX
    val ydeltaA = intersectY
    val ydeltaB = adjustedPrizeY - intersectY
    if (xdeltaA % machine.buttonA.x != 0L || xdeltaB % machine.buttonB.x != 0L ||
        ydeltaA % machine.buttonA.y != 0L || ydeltaB % machine.buttonB.y != 0L) {
        // it's not a whole number of steps, so can't be done
        return 0
    }

    return ((xdeltaA / machine.buttonA.x) * 3) + (xdeltaB / machine.buttonB.x)
}

fun main() {
    val day = "13"

    fun part1(input: List<String>): Int {
        val machines = parseInput(input)
        val result = machines.sumOf { cheapestPrize(it) }
        return result
    }

    fun part2(input: List<String>, extra: Long = 0): Long {
        val machines = parseInput(input)
        val result = machines.sumOf { cheapestPrice2(it, extra) }
        return result
    }

    // Or read a large test input from the `src/Day01_test.txt` file:
    val testInput = readInput("Day${day}_test")
    check(part1(testInput) == 480)

    // Read the input from the `src/Day01.txt` file.
    val input = readInput("Day${day}")
    part1(input).println()


    check(part2(testInput, 0L) == 480L)
    check(part2(input, 0L) == 35574L) // NB this is my answer for part 1, change to your own
    part2(input, 10000000000000L).println()
}
