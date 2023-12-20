import kotlin.math.abs

val day18 = Solution { path ->
    val lines = readAsList(path)
    val instructions = lines.map { Instruction.parse(it) }

    fun getArea(ins: List<Instruction>): Long {

        val corners = mutableListOf(Point(0, 0))
        var circumference = 0L

        var pos = Point(0, 0)

        ins.forEach { instruction ->
            circumference += instruction.distance
            pos = pos.to(instruction.direction.delta, instruction.distance)
            corners += pos
        }


        return abs(corners.windowed(2).map { (a, b) -> a.x * b.y - a.y * b.x }.sum() / 2) + circumference/2 + 1
    }

    val area = getArea(instructions)

    println("Silver: $area")

    val newInstructions = lines.map { Instruction.parse2(it) }

    val newArea = getArea(newInstructions)

    println("Gold: $newArea")
}

data class Point(val x: Long, val y: Long) {
    fun to(delta: Pair<Long, Long>, distance: Long) = Point(x + (delta.first * distance), y + (distance * delta.second))
}

data class Instruction(
    val direction: Direction,
    val distance: Long,
) {
    enum class Direction(val delta: Pair<Long, Long>) {
        UP(0L to -1L),
        DOWN(0L to 1L),
        LEFT(-1L to 0L),
        RIGHT(1L to 0L);
    }

    companion object {
        fun parse(line: String): Instruction {
            val (dir, dist) = line.split(' ')
            val direction = when (dir) {
                "U" -> Direction.UP
                "D" -> Direction.DOWN
                "L" -> Direction.LEFT
                "R" -> Direction.RIGHT
                else -> throw IllegalArgumentException("Unknown direction: ${line[0]}")
            }
            val distance = dist.toLong()
            return Instruction(direction, distance)
        }

        fun parse2(line: String): Instruction {
            val (_, _, hex) = line.split(' ')
            val distance = hex.substring(2, 7).toLong(16)
            val direction = when (hex[7]) {
                '0' -> Direction.RIGHT
                '1' -> Direction.DOWN
                '2' -> Direction.LEFT
                '3' -> Direction.UP
                else -> throw IllegalArgumentException("Unknown direction: ${line[0]}")
            }

            return Instruction(
                direction,
                distance,
            )
        }
    }
}
