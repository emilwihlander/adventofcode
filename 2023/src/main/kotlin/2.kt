private const val RED_MAX = 12
private const val GREEN_MAX = 13
private const val BLUE_MAX = 14

val day2 = Solution { path ->

    val lines = readAsList(path)

    val games = lines.map { Game.fromString(it) }

    val possibleSum = games
        .filter { game ->
            game.draws.all { draw ->
                draw.all { (color, number) ->
                    when (color) {
                        "red" -> number <= RED_MAX
                        "green" -> number <= GREEN_MAX
                        "blue" -> number <= BLUE_MAX
                        else -> false
                    }
                }
            }
        }
        .map { it.number }
        .sum()

    println("Silver: $possibleSum")

    val powerSum = games
        .map { game ->
            val allCubes = game.draws.flatten()
            val redMax = allCubes.filter { it.first == "red" }.maxOf { it.second }
            val greenMax = allCubes.filter { it.first == "green" }.maxOf { it.second }
            val blueMax = allCubes.filter { it.first == "blue" }.maxOf { it.second }
            redMax * greenMax * blueMax
        }
        .sum()

    println("Gold: $powerSum")
}

private data class Game(
    val number: Int,
    val draws: List<List<Pair<String, Int>>>
) {
    companion object {
        fun fromString(string: String): Game {
            val (game, drawsString) = string.split(":")
            val draws = drawsString.split(";").map {
                it.split(",").map {
                    val (number, color) = it.split(" ").filter { it.isNotEmpty() }
                    color to number.toInt()
                }
            }
            return Game(game.split(" ")[1].toInt(), draws)
        }
    }
}
