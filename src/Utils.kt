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


/** UTIL CLASSES BELOW HERE **/
data class Point(val x: Int, val y: Int) {
    fun moveBy(p: Point) = Point(x + p.x, y + p.y)
    fun moveBackBy(p: Point) = Point(x - p.x, y - p.y)
    fun outOfBounds(grid: List<String>) = x < 0 || x >= grid[0].length || y < 0 || y >= grid.size
}

internal fun List<String>.at(p: Point) = this[p.y][p.x]
