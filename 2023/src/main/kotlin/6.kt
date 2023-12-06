val day6 = Solution { path ->
    val lines = readAsList(path)
    val times = lines[0].split(":")[1].split(" ").filter { it.isNotEmpty() }.map { it.toInt() }
    val distances = lines[1].split(":")[1].split(" ").filter { it.isNotEmpty() }.map { it.toInt() }

    val races = times.zip(distances)

    val possibleSolutions = mutableListOf<List<Int>>()
    races.forEach { (time, record) ->
        val solutions = mutableListOf<Int>()
        for (i in 1..time) {
            if ((time - i) * i > record) {
                solutions.add(i)
            }
        }
        possibleSolutions.add(solutions)
    }

    println("Silver: ${possibleSolutions.map { it.size }.fold(1) { acc, i -> acc * i }}")

    val time = lines[0].split(":")[1].filter { it != ' ' }.toBigInteger()
    val distance = lines[1].split(":")[1].filter { it != ' ' }.toBigInteger()

    var solutions = 0.toBigInteger()
    var i = 1.toBigInteger()
    while (i < time) {
        if ((time - i) * i > distance) {
            solutions += 1.toBigInteger()
        }
        i += 1.toBigInteger()
    }

    println("Gold: $solutions")
}
