val day13 = Solution { path ->
    val lines = readAsList(path)
    val patterns = lines.fold(mutableListOf<MutableList<String>>(mutableListOf())) { acc, line ->
        if (line.isEmpty()) {
            acc.add(mutableListOf())
        } else {
            acc.last().add(line)
        }
        acc
    }

    val mirrors = patterns.map {
        val reflection = findReflection(it)
        if (reflection != 0) {
            return@map reflection * 100
        }

        val transposed = transpose(it)
        val reflection2 = findReflection(transposed)
        if (reflection2 != 0) {
            return@map reflection2
        }

        throw Exception("No reflection found")
    }

    println("Silver: ${mirrors.sum()}")

    val smudgedMirrors = patterns.map {
        val reflection = findSmudgedReflection(it)
        if (reflection != 0) {
            return@map reflection * 100
        }

        val transposed = transpose(it)
        val reflection2 = findSmudgedReflection(transposed)
        if (reflection2 != 0) {
            return@map reflection2
        }

        throw Exception("No reflection found")
    }

    println("Gold: ${smudgedMirrors.sum()}")
}

fun findReflection(pattern: List<String>): Int {
    for (i in 1 until pattern.size) {
        val left = pattern.subList(0, i)
        val right = pattern.subList(i, pattern.size)
        if (left.reversed().zip(right).all { (l, r) -> l == r }) {
            return i
        }
    }
    return 0
}

fun findSmudgedReflection(pattern: List<String>): Int {
    for (i in 1 until pattern.size) {
        val left = pattern.subList(0, i)
        val right = pattern.subList(i, pattern.size)
        val pairs = left.reversed().zip(right)

        if (smudgeMatch(pairs)) {
            return i
        }
    }
    return 0
}

fun smudgeMatch(pairs: List<Pair<String, String>>): Boolean {
    var smudges = 0
    for ((l, r) in pairs) {
        for (j in 0 until l.length) {
            if (l[j] != r[j]) {
                smudges++
            }
            if (smudges > 1) {
                return false
            }
        }
    }
    return smudges == 1
}

fun transpose(pattern: List<String>): List<String> {
    val result = mutableListOf<String>()
    for (i in pattern[0].indices) {
        result.add(pattern.map { it[i] }.joinToString(""))
    }
    return result
}
