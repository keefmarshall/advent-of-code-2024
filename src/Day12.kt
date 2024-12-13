
private val directions = listOf(
    PointVector(0, -1),
    PointVector(1, 0),
    PointVector(0, 1),
    PointVector(-1, 0)
)

private data class Edge(val pt1: Point, val pt2: Point) : Comparable<Edge> {
    private var contiguousEdges = mutableSetOf<Edge>()

    fun contiguousEdges(): Set<Edge> {
        if (contiguousEdges.isEmpty()) {
            contiguousEdges.addAll(directions
                .map { dir -> Edge(pt1.moveBy(dir), pt2.moveBy(dir)) }
                .filterNot { it.pt2 == pt1 || it.pt1 == pt2 }
            )
        }

        return contiguousEdges
    }

    fun contiguousWith(edge: Edge) = edge in contiguousEdges()

    fun minPoint() = Point(minOf(pt1.x, pt2.x), minOf(pt1.y, pt2.y))

    override fun compareTo(other: Edge): Int {
        val thisMin = minPoint()
        val otherMin = other.minPoint()
        if (thisMin.x == otherMin.x) {
            return thisMin.y.compareTo(otherMin.y)
        } else {
            return thisMin.x.compareTo(otherMin.x)
        }
    }
}

private class Cluster(val plant: Char) {
    val members = mutableSetOf<Point>()
    val edges = mutableListOf<Edge>()
}

private fun findClusters(grid: List<String>): List<Cluster> {
    val assigned = mutableSetOf<Point>()
    val clusters = mutableListOf<Cluster>()

    fun buildClusterFromPoint(pt: Point, from: Point?, cluster: Cluster) {
        if (pt in cluster.members) {
            return // nothing to do
        }

        if (pt.outOfBounds(grid) || grid.at(pt) != cluster.plant) {
            cluster.edges.add(Edge(from!!, pt))
            return
        }

        cluster.members.add(pt)
        assigned.add(pt)

        directions.forEach { direction ->
            buildClusterFromPoint(pt.moveBy(direction), pt, cluster)
        }
    }

    for (y in grid.indices) {
        for (x in grid[y].indices) {
            val pt = Point(x,y)
            if (pt in assigned) continue // already processed

            val cluster = Cluster(grid.at(pt))
            clusters.add(cluster)
            buildClusterFromPoint(pt, null, cluster)
        }
    }

    return clusters
}


fun main() {
    val day = "12"

    fun part1(input: List<String>): Int {
        val clusters = findClusters(input)
        val result = clusters.sumOf { c -> c.members.size * c.edges.size }
        return result
    }

    fun part2(input: List<String>): Long {
        val clusters = findClusters(input)
        val result = clusters.sumOf { c ->
            val seen = mutableSetOf<Edge>()
            c.edges.sorted().sumOf { edge ->
                seen.add(edge)
                if ((seen.any { it.contiguousWith(edge) })) {
                    0L // we've already counted this fence
                } else {
                    1L // this is a new fence
                }
            } * c.members.size
        }

        return result
    }

    // Or read a large test input from the `src/Day01_test.txt` file:
    val testInput = readInput("Day${day}_test")
    check(part1(testInput) == 1930)

    // Read the input from the `src/Day01.txt` file.
    val input = readInput("Day${day}")
    part1(input).println()


    check(part2(testInput) == 1206L)
    part2(input).println()
}
