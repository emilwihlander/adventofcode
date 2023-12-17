val day16 = Solution { path ->
    val lines = readAsList(path)
    val contraption = lines.map { it.map { it } }
    var beams = listOf(Light(0, 0, Light.Direction.RIGHT))

    val pastBeams = mutableSetOf(beams[0])

    while (beams.isNotEmpty()) {
        beams = beams.flatMap {
            val next = it.move(contraption)
            val nextPositions = next.filter { it !in pastBeams }
            pastBeams.addAll(nextPositions)
            nextPositions
        }
    }

    val positions = pastBeams.map { it.x to it.y }.toSet()

    println("Silver: ${positions.size}")

    val starts =
        (0 until contraption.size).flatMap { y ->
            listOf(Light(0, y, Light.Direction.RIGHT), Light(contraption[0].size - 1, y, Light.Direction.LEFT))
        } + (0 until contraption[0].size).flatMap { x ->
            listOf(Light(x, 0, Light.Direction.DOWN), Light(x, contraption.size - 1, Light.Direction.UP))
        }

    val values = starts.map { start ->
        var beams = listOf(start)
        val pastBeams = mutableSetOf(start)
        while (beams.isNotEmpty()) {
            beams = beams.flatMap {
                val next = it.move(contraption)
                val nextPositions = next.filter { it !in pastBeams }
                pastBeams.addAll(nextPositions)
                nextPositions
            }
        }
        val positions = pastBeams.map { it.x to it.y }.toSet()
        positions.size
    }

    println("Gold: ${values.max()}")
}

data class Light(
    val x: Int,
    val y: Int,
    val direction: Direction,
) {
    enum class Direction {
        UP, DOWN, LEFT, RIGHT
    }

    fun move(contraption: List<List<Char>>): List<Light> {
        val spot = contraption[y][x]
        return (when (direction) {
            Direction.UP -> moveUp(spot)
            Direction.DOWN -> moveDown(spot)
            Direction.LEFT -> moveLeft(spot)
            Direction.RIGHT -> moveRight(spot)
        }).filter { it.x >= 0 && it.y >= 0 && it.x < contraption[0].size && it.y < contraption.size }
    }

    private fun moveUp(spot: Char): List<Light> {
        return when (spot) {
            '.' -> listOf(Light(x, y - 1, Direction.UP))
            '|' -> listOf(Light(x, y - 1, Direction.UP))
            '-' -> listOf(Light(x - 1, y, Direction.LEFT), Light(x + 1, y, Direction.RIGHT))
            '/' -> listOf(Light(x + 1, y, Direction.RIGHT))
            '\\' -> listOf(Light(x - 1, y, Direction.LEFT))
            else -> throw IllegalStateException("Unknown spot: $spot")
        }
    }

    private fun moveDown(spot: Char): List<Light> {
        return when (spot) {
            '.' -> listOf(Light(x, y + 1, Direction.DOWN))
            '|' -> listOf(Light(x, y + 1, Direction.DOWN))
            '-' -> listOf(Light(x - 1, y, Direction.LEFT), Light(x + 1, y, Direction.RIGHT))
            '/' -> listOf(Light(x - 1, y, Direction.LEFT))
            '\\' -> listOf(Light(x + 1, y, Direction.RIGHT))
            else -> throw IllegalStateException("Unknown spot: $spot")
        }
    }

    private fun moveLeft(spot: Char): List<Light> {
        return when (spot) {
            '.' -> listOf(Light(x - 1, y, Direction.LEFT))
            '-' -> listOf(Light(x - 1, y, Direction.LEFT))
            '|' -> listOf(Light(x, y - 1, Direction.UP), Light(x, y + 1, Direction.DOWN))
            '/' -> listOf(Light(x, y + 1, Direction.DOWN))
            '\\' -> listOf(Light(x, y - 1, Direction.UP))
            else -> throw IllegalStateException("Unknown spot: $spot")
        }
    }

    private fun moveRight(spot: Char): List<Light> {
        return when (spot) {
            '.' -> listOf(Light(x + 1, y, Direction.RIGHT))
            '-' -> listOf(Light(x + 1, y, Direction.RIGHT))
            '|' -> listOf(Light(x, y - 1, Direction.UP), Light(x, y + 1, Direction.DOWN))
            '/' -> listOf(Light(x, y - 1, Direction.UP))
            '\\' -> listOf(Light(x, y + 1, Direction.DOWN))
            else -> throw IllegalStateException("Unknown spot: $spot")
        }
    }
}

