val day1 = Solution { path ->
    val lines = readAsList(path)
    val sum = lines
        .map {
            val firstDigit = it.find { c -> c.isDigit() } ?: '0'
            val lastDigit = it.findLast { c -> c.isDigit() } ?: '0'
            firstDigit.digitToInt() to lastDigit.digitToInt()
        }
        .map { (first, last) -> first * 10 + last }
        .sum()
    println("Silver: $sum")

    // Regex that matches a digit or a digit spelled out
    val regex = Regex("""(?=((\d)|one|two|three|four|five|six|seven|eight|nine))""")

    val sum2 = lines
        .map { line ->
            val matches = regex.findAll(line).toList().map { it.groupValues[1] }
            val firstDigit = matches.first()
            val lastDigit = matches.last()
            convertDigit(firstDigit) to convertDigit(lastDigit)
        }
        .map { (first, last) -> first * 10 + last }
        .sum()

    println("Gold: $sum2")
}

private fun convertDigit(digit: String): Int = when (digit) {
    "one" -> 1
    "two" -> 2
    "three" -> 3
    "four" -> 4
    "five" -> 5
    "six" -> 6
    "seven" -> 7
    "eight" -> 8
    "nine" -> 9
    else -> digit.toInt()
}
