val day4 = Solution {  path ->
    val lines = readAsList(path)
    val scratchcards = lines.map { parseLine(it) }

    val worth = scratchcards
        .map { it.numbersYouHave.intersect(it.winningNumbers).size }
        .map { numberOfWinningsToPoints(it) }
        .sum()

    println("Silver: $worth")

    val numberOfCards = mutableMapOf<Int, Int>()
    scratchcards.forEach { numberOfCards[it.number] = 1 }

    for (i in scratchcards.map { it.number }) {
        val currentNumber = numberOfCards[i]!!
        val currentCard = scratchcards.find { it.number == i }!!
        val numberOfWinnings = currentCard.numbersYouHave.intersect(currentCard.winningNumbers).size
        if (numberOfWinnings > 0) {
            (i + 1..i + numberOfWinnings).forEach { j ->
                numberOfCards[j] = (numberOfCards[j] ?: 0) + currentNumber
            }
        }
    }

    val totalNumberOfCards = numberOfCards.values.sum()
    println("Gold: $totalNumberOfCards")
}

private data class Scratchcard(
    val number: Int,
    val winningNumbers: Set<Int>,
    val numbersYouHave: Set<Int>,
)

private fun numberOfWinningsToPoints(numberOfWinnings: Int): Int {
    if (numberOfWinnings == 0) return 0
    var points = 1
    for (i in 2..numberOfWinnings) {
        points *= 2
    }
    return points
}

private fun parseLine(line: String): Scratchcard {
    val lineRegex = Regex("""Card[^0-9]+(\d+): ([^|]+)\|(.+)""")
    val match = lineRegex.matchEntire(line)

    if (match == null)
        throw Exception("Invalid line: $line")
    val (number, winningNumbers, numbersYouHave) = match.destructured
    return Scratchcard(
        number.toInt(),
        winningNumbers.split(" ").filter { it.isNotEmpty() }.map { it.toInt() }.toSet(),
        numbersYouHave.split(" ").filter { it.isNotEmpty() }.map { it.toInt() }.toSet(),
    )
}
