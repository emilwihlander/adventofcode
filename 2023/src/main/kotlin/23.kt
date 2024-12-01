val day23 = Solution { path ->
    val lines = readAsList(path)

    val map = HikingTrailMap.parse(lines)

    var current = listOf(map.start to 0)
    val goals = mutableListOf<Int>()

    while (current.isNotEmpty()) {
        val tmp = current.flatMap { (pos, steps) -> map.possiblePaths(pos).map { it to (steps + 1) } }
        goals.addAll(tmp.filter { it.first == map.goal }.map { it.second })
        current = tmp.filter { it.first != map.goal }
    }

    println("Silver: ${goals.max()}")
}

class HikingTrailMap(
    val map: List<List<Tile>>
) {

    val goal = Pos(
        map.last().indexOfFirst { it == Tile.PATH },
        map.indices.last,
        Direction.SOUTH
    )

    val start = Pos(
        map[0].indexOfFirst { it == Tile.PATH },
        0,
        Direction.SOUTH
    )

    fun possiblePaths(pos: Pos): List<Pos> {
        return pos.next().filter { validPath(it) }
    }

    fun validPath(pos: Pos): Boolean {
        if (pos.y !in map.indices || pos.x !in map[0].indices)
            return false

        return when (map[pos.y][pos.x]) {
            Tile.PATH -> true
            Tile.FOREST -> false
            Tile.SLOPE_NORTH -> pos.dir == Direction.NORTH
            Tile.SLOPE_WEST -> pos.dir == Direction.WEST
            Tile.SLOPE_SOUTH -> pos.dir == Direction.SOUTH
            Tile.SLOPE_EAST -> pos.dir == Direction.EAST
        }
    }

    enum class Direction {
        NORTH, WEST, SOUTH, EAST;
    }

    data class Pos(
        val x: Int,
        val y: Int,
        val dir: Direction,
    ) {
        fun next(): List<Pos> {
            return listOfNotNull(
                if (dir == Direction.SOUTH) null else copy(y = y - 1, dir = Direction.NORTH),
                if (dir == Direction.EAST) null else copy(x = x - 1, dir = Direction.WEST),
                if (dir == Direction.NORTH) null else copy(y = y + 1, dir = Direction.SOUTH),
                if (dir == Direction.WEST) null else copy(x = x + 1, dir = Direction.EAST),
            )
        }
    }

    enum class Tile {
        PATH,
        FOREST,
        SLOPE_NORTH,
        SLOPE_WEST,
        SLOPE_SOUTH,
        SLOPE_EAST;
    }

    companion object {
        fun parse(lines: List<String>): HikingTrailMap {
            return HikingTrailMap(lines.map { it.map { parseTile(it) } })
        }

        private fun parseTile(c: Char): Tile {
            return when (c) {
                '.' -> Tile.PATH
                '#' -> Tile.FOREST
                '^' -> Tile.SLOPE_NORTH
                '<' -> Tile.SLOPE_WEST
                'v' -> Tile.SLOPE_SOUTH
                '>' -> Tile.SLOPE_EAST
                else -> throw IllegalStateException()
            }
        }
    }
}