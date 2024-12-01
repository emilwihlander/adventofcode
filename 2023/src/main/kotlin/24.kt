import java.math.BigDecimal

val day24 = Solution { path ->
    val lines = readAsList(path)

    val hailstones = lines.map { Hailstone.parse(it) }

    val testArea = 200000000000000.0 to 400000000000000.0

    fun inTestArea(point: Pair<Double, Double>): Boolean {
        return point.first >= testArea.first && point.first <= testArea.second &&
                point.second >= testArea.first && point.second <= testArea.second
    }

    var count = 0

    for (i in 0..<hailstones.size-1) {
        for (j in i+1..<hailstones.size) {
            val intersect = hailstones[i].intersect2d(hailstones[j])
            if (intersect != null && inTestArea(intersect)) {
                count++
            }
        }
    }

    println("Silver: $count")

}

private data class Hailstone(
    val x: Long,
    val y: Long,
    val z: Long,
    val dx: Long,
    val dy: Long,
    val dz: Long,
) {
    fun intersect2d(other: Hailstone): Pair<Double, Double>? {
        val x1 = x
        val x2 = x + dx
        val x3 = other.x
        val x4 = other.x + other.dx
        val y1 = y
        val y2 = y + dy
        val y3 = other.y
        val y4 = other.y + other.dy

        val denominator = ((x1 - x2)*(y3 - y4) - (y1 - y2)*(x3 - x4)).toDouble()
        if (denominator == 0.0)
            return null

        val px = ((x1*y2 - y1*x2)*(x3 - x4) - (x1 - x2)*(x3*y4 - y3*x4)) / denominator
        val py = ((x1*y2 - y1*x2)*(y3 - y4) - (y1 - y2)*(x3*y4 - y3*x4)) / denominator

        return if (isForward(px, py) && other.isForward(px, py)) {
            px to py
        } else {
            null
        }
    }

    private fun isForward(x: Double, y: Double): Boolean {
        val xIsForward = (this.dx > 0 && this.x <= x) || (this.dx < 0 && this.x >= x)
        val yIsForward = (this.dy > 0 && this.y <= y) || (this.dy < 0 && this.y >= y)
        return xIsForward && yIsForward
    }
    companion object {
        fun parse(line: String): Hailstone {
            val numberRegex = Regex("-?\\d+")
            val matches = numberRegex.findAll(line).map { it.value.toLong() }.toList()
            return Hailstone(
                matches[0],
                matches[1],
                matches[2],
                matches[3],
                matches[4],
                matches[5],
            )
        }
    }
}