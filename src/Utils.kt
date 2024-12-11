import java.math.BigDecimal
import java.math.BigInteger
import java.security.MessageDigest
import kotlin.io.path.Path
import kotlin.io.path.readText

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = Path("src/$name.txt").readText().trim().lines()

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

/**
 * The cleaner shorthand for printing output.
 */
fun Any?.println() = println(this)


/* UTIL CLASSES BELOW HERE **/

/** represents a point on a two-dimensional grid */
data class Point(val x: Int, val y: Int) {
    fun moveBy(v: PointVector) = Point(x + v.x, y + v.y)
    fun moveBackBy(v: PointVector) = Point(x - v.x, y - v.y)
    fun outOfBounds(grid: List<String>) = x < 0 || x >= grid[0].length || y < 0 || y >= grid.size
}

/** very similar to a Point but represents the vector between two points e.g. (-1, 0)
  Making it a separate class makes it clearer what different methods expect */
data class PointVector(val x: Int, val y: Int)

/** returns the value at this point in the grid */
internal fun List<String>.at(p: Point) = this[p.y][p.x]
/** returns the vector needed to translate p1 to p2 - e.g. p1.moveBy(<result>) == p2 */
internal fun vectorBetween(p1: Point, p2: Point) = PointVector(p2.x - p1.x, p2.y - p1.y)


internal fun Int.isEven() = this % 2 == 0
internal fun Long.isEven() = this % 2 == 0L
internal fun BigInteger.isEven() = this % BigInteger.valueOf(2) == BigInteger.valueOf(0)
internal fun Int.isOdd() = !this.isEven()
internal fun Long.isOdd() = !this.isEven()
internal fun BigInteger.isOdd() = !this.isEven()


internal operator fun BigInteger.times(n: Long) = this * BigInteger.valueOf(n)
internal operator fun BigInteger.times(n: Int) = this * n.toLong()
