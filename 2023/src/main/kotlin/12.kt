val day12 = Solution { path ->
    val lines = readAsList(path)

    val springs = lines.map {
        val (springs, groups) = it.split(" ")
        springs to groups.split(",").map { it.toInt() }
    }

    val variations = mutableListOf<List<String>>()

    val sum = springs.mapIndexed { index, (springs, groups) ->
        testSprings2(springs, groups, mutableMapOf())
    }.sum()

    springs.forEach { (springs, groups) ->
        val variationsForSpring = testSprings(springs, groups)
        variations.add(variationsForSpring)
    }

    println("Silver: ${variations.sumOf { it.size }} $sum")

    val unfoldedSprings = lines.map {
        val (springs, groups) = it.split(" ")
        (1..5).map { springs }.joinToString("?") to (1..5).flatMap { groups.split(",").map { it.toInt() } }
    }

    val unfoldedSum = unfoldedSprings.mapIndexed { index, (springs, groups) ->
        // println(index)
        testSprings2(springs, groups, mutableMapOf())
    }.sum()

    //val unfoldedVariations = mutableListOf<List<String>>()

    //unfoldedSprings.forEachIndexed { index, (springs, groups) ->
    //    val variationsForSpring = testSprings(springs, groups)
    //    println(index)
    //    unfoldedVariations.add(variationsForSpring)
    //}

    println("Gold: $unfoldedSum")
}

fun testSprings(spring: String, groups: List<Int>): List<String> {

    if ('?' !in spring) {
        return if (matches(spring, groups)) listOf(spring) else emptyList()
    }
    if (!partialMatch(spring, groups)) {
        return emptyList()
    }
    return testSprings(spring.replaceFirst('?', '.'), groups) +
        testSprings(spring.replaceFirst('?', '#'), groups)
}

fun matches(spring: String, groups: List<Int>): Boolean {
    //val regex = Regex("""^\\.*${groups.map { "#{$it}" }.joinToString("\\.+")}\\.*$""")
    //return regex.matches(spring)
    var i = 0
    var j = 0
    var currentGroup: Int? = null
    while (i < spring.length) {
        if (spring[i] == '#') {
            if (currentGroup == null) {
                if (j == groups.size) {
                    return false
                }
                currentGroup = groups[j]
                j++
            }
            if (currentGroup == 0) {
                return false
            }
            currentGroup--
        } else if (spring[i] == '.') {
            if (currentGroup != null && currentGroup > 0) {
                return false
            }
            currentGroup = null
        }
        i++
    }
    return j == groups.size && (currentGroup == null || currentGroup == 0)
}

fun partialMatch(spring: String, groups: List<Int>): Boolean {
    val indexFirstQuestionMark = spring.indexOf('?')
    val partialSpring = spring.substring(0, indexFirstQuestionMark)

    var i = 0
    var j = 0
    var currentGroup: Int? = null
    while (i < partialSpring.length) {
        if (spring[i] == '#') {
            if (currentGroup == null) {
                if (j == groups.size) {
                    return false
                }
                currentGroup = groups[j]
                j++
            }
            if (currentGroup == 0) {
                return false
            }
            currentGroup--
        } else if (spring[i] == '.') {
            if (currentGroup != null && currentGroup > 0) {
                return false
            }
            currentGroup = null
        }
        i++
    }
    return true
}

fun testSprings2(spring: String, groups: List<Int>, mem: MutableMap<Pair<String, List<Int>>, Long>): Long {
    if (mem[spring to groups] != null) {
        return mem[spring to groups]!!
    }
    val totalSize = spring.length
    val groupsMinSize = groups.sum() + groups.size - 1
    if (totalSize < groupsMinSize) {
        return 0
    }
    var matches = 0L
    for (i in 0 .. totalSize - groupsMinSize) {
        val partialSprings = ".".repeat(i) + "#".repeat(groups[0]) + "."
        if (couldMatch(partialSprings, spring)) {
            if (groups.size == 1) {
                if (partialSprings.length - 1 == spring.length || spring.substring(partialSprings.length).all { it == '.' || it == '?' })
                    matches += 1L
            } else {
                val remainingGroups = groups.subList(1, groups.size)
                val remainingSpring = spring.substring(partialSprings.length)
                matches += testSprings2(remainingSpring, remainingGroups, mem)
            }
        }
    }
    mem[spring to groups] = matches
    return matches
}

fun couldMatch(partialSpring: String, spring: String): Boolean {
    for (i in partialSpring.indices) {
        if (i >= spring.length) {
            return partialSpring[i] == '.'
        }
        val b = spring[i]
        if (b == '?')
            continue
        val a = partialSpring[i]
        if (a != b)
            return false
    }
    return true
}
