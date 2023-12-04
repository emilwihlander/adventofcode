val day3 = Solution { path ->
    val lines = readAsList(path)

    val numberRegex = Regex("""\d+""")

    val partNumbers = mutableListOf<Int>()

    for (i in lines.indices) {
        val line = lines[i]
        val numbers = numberRegex.findAll(line)
        numbers
            .filter { hasAdjacentSymbol(lines, i, it) }
            .map { it.value.toInt() }
            .forEach { partNumbers.add(it) }
    }

    println("Silver: ${partNumbers.sum()}")

    val gearRatios = mutableListOf<Int>()

    val gearRegex = Regex("""\*""")

    for (i in lines.indices) {
        val line = lines[i]
        val gears = gearRegex.findAll(line)
        gears
            .map { adjacentNumbers(lines, i, it) }
            .filter { it.size == 2 }
            .forEach { gearRatios.add(it[0] * it[1]) }
    }

    println(gearRatios)

    println("Gold: ${gearRatios.sum()}")
}

fun adjacentNumbers(lines: List<String>, i: Int, match: MatchResult): List<Int> {
    val adjacentNumbers = mutableListOf<Int>()
    val indicesToTest = listOf(
        match.range.first - 1,
        match.range.first,
        match.range.first + 1,
    )
    if (i > 0) {
        val numbersAbove = Regex("""\d+""").findAll(lines[i - 1])
        numbersAbove.forEach { numberMatch ->
            if (indicesToTest.any { it in numberMatch.range }) {
                adjacentNumbers.add(numberMatch.value.toInt())
            }
        }
    }
    val numbers = Regex("""\d+""").findAll(lines[i])
    numbers.forEach { numberMatch ->
        if (indicesToTest.any { it in numberMatch.range }) {
            adjacentNumbers.add(numberMatch.value.toInt())
        }
    }
    if (i < lines.size - 1) {
        val numbersBelow = Regex("""\d+""").findAll(lines[i + 1])
        numbersBelow.forEach { numberMatch ->
            if (indicesToTest.any { it in numberMatch.range }) {
                adjacentNumbers.add(numberMatch.value.toInt())
            }
        }
    }
    return adjacentNumbers
}

fun hasAdjacentSymbol(lines: List<String>, i: Int, match: MatchResult): Boolean {
    val symbolRegex = Regex("""[^.0-9]""")
    return match.range.any {
        val positionsToTest = listOf(
            i to it - 1,
            i to it + 1,
            i - 1 to it,
            i + 1 to it,
            i - 1 to it - 1,
            i - 1 to it + 1,
            i + 1 to it - 1,
            i + 1 to it + 1,
        )
        positionsToTest.any { (x, y) ->
            x >= 0 && y >= 0 && x < lines.size && y < lines[0].length && symbolRegex.matches(lines[x][y].toString())
        }
    }
}
