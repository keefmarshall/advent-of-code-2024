import java.math.BigInteger

// using BigInteger as not sure how big these numbers will get
// - potentially multiplying by 2024 each time?

private fun String.splitInHalf(): List<String> =
    listOf(
        this.substring(0 until this.length/2),
        this.substring(this.length/2 until this.length)
    )

private fun String.removeLeadingZeroes(): String = BigInteger(this).toString()

private fun blinkOneStone(stone: String): List<String> =
    when {
        stone == "0" ->
            listOf("1")
        stone.length.isEven() ->
            stone.splitInHalf().map(String::removeLeadingZeroes)
        else ->
            listOf((BigInteger(stone) * 2024).toString())
    }

private fun blink(stones: List<String>): List<String> {
    return stones.flatMap(::blinkOneStone)
}

// key == (stone_value, times), value is the number of stones produced
private val cache = mutableMapOf<Pair<String, Int>, Long>()

private fun blinkIncrementally(stone: String, times: Int): Long {
    // We only need to know the number, so can do this piece-wise
    if (times == 0) {
        return 1
    }

    if (cache.contains(Pair(stone, times))) {
        return cache[Pair(stone, times)]!!
    }

    return blinkOneStone(stone).sumOf {
        val result = blinkIncrementally(it, times - 1)
        cache[Pair(it, times - 1)] = result
        result
    }
}

fun main() {
    val day = "11"

    fun part1(input: List<String>): Int {
        var stones = input[0].split(Regex("\\s+"))
        repeat(25) {
            stones = blink(stones)
        }
        return stones.size
    }

    fun part2(input: List<String>): Long {
        val stones = input[0].split(Regex("\\s+"))
        return stones.sumOf{ blinkIncrementally(it, 75) }
    }

    // Or read a large test input from the `src/Day01_test.txt` file:
    val testInput = readInput("Day${day}_test")
    check(part1(testInput) == 55312)

    // Read the input from the `src/Day01.txt` file.
    val input = readInput("Day${day}")
    part1(input).println()


//    check(part2(testInput) == 31)
    part2(input).println()
}
