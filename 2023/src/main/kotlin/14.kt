val day14 = Solution { path ->
    val lines = readAsList(path)
    val platform = lines.map { it.map { it }.toMutableList() }.toMutableList()

    var changes: Int

    do {
        changes = 0
        for (i in 1 until platform.size) {
            for (j in 0 until platform[i].size) {
                val current = platform[i][j]
                if (current != 'O') {
                    continue
                }

                val up = platform[i - 1][j]

                if (up == '.') {
                    platform[i - 1][j] = 'O'
                    platform[i][j] = '.'
                    changes++
                }
            }
        }

    } while (changes > 0)

    val sum = platform.mapIndexed { index, chars -> chars.filter { it == 'O' }.size * (platform.size - index) }.sum()

    println("Silver: ${sum}")

    var cycle = 1
    val mem = mutableMapOf<String, List<List<Char>>>()
    val appeared = mutableMapOf<String, Int>()

    while (cycle <= 1000000000) {

        val key = key(platform)

        if (key in appeared) {
            val diff = cycle - appeared[key]!!
            val remaining = 1000000000 - cycle
            val cycles = remaining / diff
            if (cycles > 0) {
                cycle += cycles * diff
                continue
            }
        }

        if (key in mem) {
            mem[key]!!.forEachIndexed { i, chars ->
                chars.forEachIndexed { j, c -> platform[i][j] = c }
            }
            cycle++
            continue
        }

        do {
            changes = 0
            for (i in 1 until platform.size) {
                for (j in 0 until platform[i].size) {
                    val current = platform[i][j]
                    if (current != 'O') {
                        continue
                    }

                    val up = platform[i - 1][j]

                    if (up == '.') {
                        platform[i - 1][j] = 'O'
                        platform[i][j] = '.'
                        changes++
                    }
                }
            }
        } while (changes > 0)

        do {
            changes = 0
            for (j in 1 until platform[0].size) {
                for (i in 0 until platform.size) {
                    val current = platform[i][j]
                    if (current != 'O') {
                        continue
                    }

                    val west = platform[i][j - 1]

                    if (west == '.') {
                        platform[i][j - 1] = 'O'
                        platform[i][j] = '.'
                        changes++
                    }
                }
            }
        } while (changes > 0)

        // South
        do {
            changes = 0
            for (i in 0 until platform.size - 1) {
                for (j in 0 until platform[i].size) {
                    val current = platform[i][j]
                    if (current != 'O') {
                        continue
                    }

                    val down = platform[i + 1][j]

                    if (down == '.') {
                        platform[i + 1][j] = 'O'
                        platform[i][j] = '.'
                        changes++
                    }
                }
            }
        } while (changes > 0)

        // East
        do {
            changes = 0
            for (j in 0 until platform[0].size - 1) {
                for (i in 0 until platform.size) {
                    val current = platform[i][j]
                    if (current != 'O') {
                        continue
                    }

                    val east = platform[i][j + 1]

                    if (east == '.') {
                        platform[i][j + 1] = 'O'
                        platform[i][j] = '.'
                        changes++
                    }
                }
            }
        } while (changes > 0)

        mem[key] = platform.map { it.map { it } }
        appeared[key] = cycle
        cycle++
    }

    val sum2 = platform.mapIndexed { index, chars -> chars.filter { it == 'O' }.size * (platform.size - index) }.sum()

    println("Gold: ${sum2}")
}

fun key(platform: List<List<Char>>): String {
    return platform.map { it.joinToString("") }.joinToString("")
}
