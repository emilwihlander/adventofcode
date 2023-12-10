val day9 = Solution { path ->
    val lines = readAsList(path)
    val sequences = lines.map { it.split(" ").map { it.toInt() } }

    var sumForward = 0
    for (s in sequences) {
        val differences = mutableListOf(s)
        while (differences.last().distinct().size > 1) {
            differences.add(differences.last().windowed(2).map { it[1] - it[0] })
        }
        val predictions = mutableListOf(0)
        for (i in differences.size - 1 downTo 0) {
            val difference = differences[i]
            val prediction = predictions.last() + difference.last()
            predictions.add(prediction)
        }
        sumForward += predictions.last()
    }

    println("Silver: $sumForward")

    var sumBackwards = 0
    for (s in sequences) {
        val differences = mutableListOf(s)
        while (differences.last().distinct().size > 1) {
            differences.add(differences.last().windowed(2).map { it[1] - it[0] })
        }
        val predictions = mutableListOf(0)
        for (i in differences.size - 1 downTo 0) {
            val difference = differences[i]
            val prediction = difference.first() - predictions.last()
            predictions.add(prediction)
        }
        sumBackwards += predictions.last()
    }

    println("Gold: $sumBackwards")
}
