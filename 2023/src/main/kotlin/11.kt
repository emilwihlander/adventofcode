import kotlin.math.abs

val day11 = Solution { path ->
    val lines = readAsList(path)
    val matrix = lines.map { it.map { it } }
    val expandedMatrix = expandMatrix(matrix)
    val expandedGalaxies = mutableListOf<Pair<Int, Int>>()
    expandedMatrix.forEachIndexed { y, chars ->
        chars.forEachIndexed { x, c ->
            if (c == '#')
                expandedGalaxies.add(x to y)
        }
    }
    var totalDistance = 0L
    val distances = mutableListOf<Pair<Pair<Int, Int>, Int>>()

    for (i in 0 until expandedGalaxies.size) {
        for (j in i until expandedGalaxies.size) {
            val dx = abs(expandedGalaxies[i].first - expandedGalaxies[j].first)
            val dy = abs(expandedGalaxies[i].second - expandedGalaxies[j].second)
            distances.add((i to j) to dx + dy)
            totalDistance += dx + dy
        }
    }

    println("Silver: $totalDistance")

    totalDistance = 0L
    val (yFactors, xFactors) = factors(matrix)

    val galaxies = mutableListOf<Pair<Int, Int>>()
    matrix.forEachIndexed { y, chars ->
        chars.forEachIndexed { x, c ->
            if (c == '#')
                galaxies.add(x to y)
        }
    }

    val distances2 = mutableListOf<Pair<Pair<Int, Int>, Int>>()

    for (i in 0 until galaxies.size) {
        for (j in i until galaxies.size) {
            val xi = range(galaxies[i].first, galaxies[j].first).map { xFactors[it] }.sum() - 1
            val yi = range(galaxies[i].second, galaxies[j].second).map { yFactors[it] }.sum() - 1
            distances2.add((i to j) to xi + yi)
            totalDistance += xi + yi
        }
    }

    println("Gold: $totalDistance")
}

fun expandMatrix(matrix: List<List<Char>>): List<List<Char>> {
    return matrix
        .flatMap { if (it.all { it == '.' }) listOf(it, it) else listOf(it) }
        .transpose()
        .flatMap { if (it.all { it == '.' }) listOf(it, it) else listOf(it) }
        .transpose()
}

fun range(a: Int, b: Int): IntRange {
    return if (a > b) {
        b..a
    } else {
        a..b
    }
}

const val EXPANSION_FACTOR = 1000000
fun factors(matrix: List<List<Char>>): Pair<List<Int>, List<Int>> {
    return matrix.map { if (it.all { it == '.' }) EXPANSION_FACTOR else 1 } to
            matrix.transpose().map { if (it.all { it == '.' }) EXPANSION_FACTOR else 1 }
}

fun List<List<Char>>.transpose() = this[0].indices.map { col -> this.map { row -> row[col] } }
