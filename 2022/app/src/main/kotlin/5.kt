/*
[N]     [C]                 [Q]
[W]     [J] [L]             [J] [V]
[F]     [N] [D]     [L]     [S] [W]
[R] [S] [F] [G]     [R]     [V] [Z]
[Z] [G] [Q] [C]     [W] [C] [F] [G]
[S] [Q] [V] [P] [S] [F] [D] [R] [S]
[M] [P] [R] [Z] [P] [D] [N] [N] [M]
[D] [W] [W] [F] [T] [H] [Z] [W] [R]
 1   2   3   4   5   6   7   8   9
 */

fun initTestStacks() = listOf(
    ArrayDeque(listOf('Z', 'N')),
    ArrayDeque(listOf('M', 'C', 'D')),
    ArrayDeque(listOf('P')),
)

fun initStacks() = listOf(
    ArrayDeque(listOf('D','M','S','Z','R','F','W','N')),
    ArrayDeque(listOf('W','P','Q','G','S')),
    ArrayDeque(listOf('W','R','V','Q','F','N','J','C')),
    ArrayDeque(listOf('F','Z','P','C','G','D','L')),
    ArrayDeque(listOf('T','P','S')),
    ArrayDeque(listOf('H','D','F','W','R','L')),
    ArrayDeque(listOf('Z','N','D','C')),
    ArrayDeque(listOf('W','N','R','F','V','S','J','Q')),
    ArrayDeque(listOf('R','M','S','G','Z','W','V')),
)


fun day5() {
    val lines = readAsList("5.txt")
    val commands = lines
        .map { it.split(" ") }
        .map { Triple(it[1].toInt(), it[3].toInt(), it[5].toInt()) }

    var stacks = initStacks()
    for ((nbr, from, to) in commands) {
        for (i in 1..nbr) {
            stacks[to-1].addLast(stacks[from-1].removeLast())
        }
    }

    println(stacks.map { it.last() }.joinToString(""))

    stacks = initStacks()
    for ((nbr, from, to) in commands) {
        val buffer = ArrayDeque<Char>()
        for (i in 1..nbr) {
            buffer.addFirst(stacks[from-1].removeLast())
        }
        for (c in buffer) {
            stacks[to-1].addLast(c)
        }
    }

    println(stacks.map { it.last() }.joinToString(""))
}
