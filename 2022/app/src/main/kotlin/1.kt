fun day1() {
    val lines = read("1.txt")

    val elves = lines
        .split("\n\n")
        .map { it.split("\n").filter { it != "" }.sumOf { it.toInt() } }

    println(elves.maxOrNull())
    println(elves.sortedDescending().subList(0,3).sum())
}
