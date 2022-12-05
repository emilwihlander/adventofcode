fun parseRange(s: String): IntRange {
    val values = s.split("-").map { it.toInt() }
    return values[0]..values[1]
}
fun day4() {
    val lines = readAsList("4.txt")
    val sectionPairs = lines
        .map { it.split(",") }
        .map { parseRange(it[0]) to parseRange(it[1]) }

    val enclosed = sectionPairs.count { (a, b) ->
        val intersect = a.intersect(b)
        a.toSet() == intersect || b.toSet() == intersect
    }

    println(enclosed)

    val overlapping = sectionPairs.count { (a, b) ->
        a.intersect(b).isNotEmpty()
    }

    println(overlapping)
}
