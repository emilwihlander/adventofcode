import java.math.BigInteger

/**
 * I understood that the common loop was needed to be found.
 * I don't really understand why I don't need to take the offsets into consideration.
 */

val day8 = Solution { path ->
    val lines = readAsList(path)
    val instructions = lines[0].map { it }

    val nodes = lines.drop(2).map { Node.parse(it) }.associateBy { it.name }

    var steps = 0L
    //var current = nodes["AAA"]!!
    //val target = nodes["ZZZ"]!!

    //while (current != target) {
    //    val instruction = instructions[steps % instructions.size]
    //    current = nodes[if (instruction == 'L') current.children.first else current.children.second]!!
    //    steps++
    //}

    println("Silver: $steps")

    steps = 0L

    val numberOfInstrucations = instructions.size

    var currents = nodes.entries.filter { it.key[2] == 'A' }.map { it.value }
    val visitedNodes = currents.map { mutableMapOf<Pair<String, Char>, Long>() }
    val loops: MutableList<Pair<Long, Long>?> = currents.map { null }.toMutableList()
    val ends = currents.map { mutableListOf<Long>() }

    while (!currents.all { it.name[2] == 'Z' } && loops.any { it == null }) {
        val instruction = instructions[(steps % instructions.size).toInt()]
        currents.forEachIndexed { index, node ->
            if (loops[index] == null) {
                if (node.name[2] == 'Z') {
                    ends[index].add(steps)
                }
                val value = visitedNodes[index][Pair(node.name, instruction)]
                if (value != null) {
                    if ((steps - value) % numberOfInstrucations == 0L) {
                        loops[index] = Pair(value, steps)
                        println("Loop found (i $index): ${value} ${steps - value}")
                    }
                } else {
                    visitedNodes[index].put(Pair(node.name, instruction), steps)
                }
            }
        }
        currents = currents.map { node ->
            nodes[if (instruction == 'L') node.children.first else node.children.second]!!
        }

        steps += 1L
    }

    println("Loops: $loops")
    println("Ends: $ends")

    val lcm2 = ends.map { it[0].toBigInteger() }.reduce { acc, i -> findLCM(acc, i) }

    println("Gold: $lcm2")
}

data class Node(
    val name: String,
    val children: Pair<String, String>,
) {
    companion object {
        fun parse(line: String): Node {
            val (name, children) = line.split(" = ")
            return Node(name, children.filter { it != '(' && it != ')' }.split(", ").let { Pair(it[0], it[1]) })
        }
    }
}

val BI_0 = 0.toBigInteger()

fun findLCM(a: BigInteger, b: BigInteger): BigInteger {
    val larger = if (a > b) a else b
    val maxLcm = a * b
    var lcm = larger
    while (lcm <= maxLcm) {
        if (lcm % a == BI_0 && lcm % b == BI_0) {
            return lcm
        }
        lcm += larger
    }
    return maxLcm
}

