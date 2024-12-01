val day21 = Solution {  path ->
    val lines = readAsList(path)
    val garden = Garden.parse(lines)

    var tiles = setOf(garden.start)

    for (i in 1..64) {
        tiles = tiles.flatMap { garden.legalSteps(it) }.toSet()
    }

    println("Silver: ${tiles.size}")

    var goldTiles = setOf(garden.start)

    for (i in 1..5000) {
        goldTiles = goldTiles.flatMap { garden.legalSteps2(it) }.toSet()
    }

    println("Gold: ${goldTiles.size}")
}

class Garden(
    val start: Pair<Int, Int>,
    val field: List<List<Tile>>,
) {
    fun legalSteps(pos: Pair<Int, Int>): List<Pair<Int, Int>> {
        val positions = listOf(
            pos.first to pos.second - 1,
            pos.first to pos.second + 1,
            pos.first - 1 to pos.second,
            pos.first + 1 to pos.second,
        )
        return positions.filter { legalPos(it) }
    }

    fun legalSteps2(pos: Pair<Int, Int>): List<Pair<Int, Int>> {
        val positions = listOf(
            pos.first to pos.second - 1,
            pos.first to pos.second + 1,
            pos.first - 1 to pos.second,
            pos.first + 1 to pos.second,
        )
        return positions.filter { legalPos(wrap(it)) }
    }

    private fun wrap(pos: Pair<Int, Int>): Pair<Int, Int> {
        return Pair(
            ((pos.first % field[0].size) + field[0].size) % field[0].size,
            ((pos.second % field.size) + field.size) % field.size,
        )
    }

    private fun legalPos(pos: Pair<Int, Int>): Boolean {
        return pos.first in field[0].indices
                && pos.second in field.indices
                && field[pos.second][pos.first] == Tile.GARDEN
    }
    companion object {
        fun parse(lines: List<String>): Garden {
            val y = lines.indexOfFirst { 'S' in it }
            val x = lines[y].indexOfFirst { 'S' == it }

            return Garden(
                x to y,
                lines.map { it.map { Tile.parse(it) } },
            )
        }
    }
}

enum class Tile {
    ROCK, GARDEN;

    companion object {
        fun parse(c: Char): Tile {
            return when (c) {
                '.' -> GARDEN
                '#' -> ROCK
                'S' -> GARDEN
                else -> throw IllegalStateException()
            }
        }
    }
}