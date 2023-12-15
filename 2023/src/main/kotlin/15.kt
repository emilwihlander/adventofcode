val day15 = Solution { path ->
    val lines = readAsList(path)
    val commands = lines[0].split(",").map { it.trim() }.filter { it.isNotEmpty() }.map { Command(it) }

    val sum = commands.map { it.commandHash }.sum()

    println("Silver: $sum")

    val hashMap = mutableMapOf<Int, MutableList<Pair<String, Int>>>()

    (0..255).forEach {
        hashMap[it] = mutableListOf()
    }

    commands.forEach { c ->
        val l = hashMap[c.labelHash]!!
        if (c.operation == '=') {
            val alreadyExists = l.indexOfFirst { it.first == c.label }
            if (alreadyExists != -1) {
                l[alreadyExists] = Pair(c.label, c.focalLength)
            } else {
                l.add(Pair(c.label, c.focalLength))
            }
        } else {
            l.indexOfFirst { it.first == c.label }.let {
                if (it != -1) {
                    l.removeAt(it)
                }
            }
        }
    }

    val sum2 = hashMap.map { (box, lenses) ->
        lenses.mapIndexed { index, (label, focalLength) -> (box + 1) * (index + 1) * focalLength }.sum()
    }.sum()

    println("Gold: $sum2")
}

fun hash(command: String): Int {
    var hash = 0
    command.forEach {
        hash += it.code
        hash *= 17
        hash %= 256
    }
    return hash
}

data class Command(val command: String) {
    val operation = if ('=' in command) '=' else '-'
    val label = command.split(operation)[0]
    val focalLength = if ('=' in command) command.split("=")[1].toInt() else 0

    val commandHash = hash(command)
    val labelHash = hash(label)
}
