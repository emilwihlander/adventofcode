fun priority(c: Char): Int {
    if (c in 'A'..'Z') {
        return 27 + (c - 'A')
    }
    return c - 'a' + 1
}
fun day3() {
    val lines = readAsString("3.txt")
    val rucksacks = lines
        .split("\n")
        .filter { it != "" }
        .map {
            val middle = it.length / 2
            it.subSequence(0, middle) to it.subSequence(middle, it.length)
        }

    val same = rucksacks
        .map { (l, r) -> l.toSet().intersect(r.toSet()) }
        .mapIndexed { i, l -> if (l.size == 1) l.first() else { println(i); throw Error() } }
        .map { priority(it) }
        .sum()

    println(same)

    val groups = lines
        .split("\n")
        .filter { it != "" }
        .map { it.toCharArray().toSet() }
        .chunked(3)
        .map { it[0].intersect(it[1]).intersect(it[2]) }
        .mapIndexed { i, l -> if (l.size == 1) l.first() else { println(i); throw Error() } }
        .sumOf { priority(it) }

    println(groups)
}
