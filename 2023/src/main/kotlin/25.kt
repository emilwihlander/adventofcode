val day25 = Solution { path ->
    val lines = readAsList(path)
    val connections = lines.flatMap {
        val c = it.split(": ")
        c[1].split(" ").map { c[0] to it }
    }

    fun findGroups(connections: Map<String, Set<String>>): Pair<Int, Int>? {
        val groups = mutableListOf<MutableSet<String>>()

        val remainingComponents = connections.keys.toMutableSet()


        while (remainingComponents.isNotEmpty()) {
            groups.add(mutableSetOf())
            if (groups.size > 2)
                return null
            val queue = mutableListOf(remainingComponents.first())
            while (queue.isNotEmpty()) {
                val component = queue.removeFirst()
                remainingComponents.remove(component)
                groups.last() += component
                queue.addAll(connections[component]!! - groups.last())
            }
        }

        if (groups.size != 2)
            return null
        return groups[0].size to groups[1].size
    }

    fun toConnectionMap(connections: List<Pair<String, String>>): Map<String, Set<String>> {
        val map = mutableMapOf<String, MutableSet<String>>()

        for ((a, b) in connections) {
            if (a !in map) map[a] = mutableSetOf()
            if (b !in map) map[b] = mutableSetOf()

            map[a]!! += b
            map[b]!! += a
        }

        return map
    }

    fun stillConnected(connections: Map<String, Set<String>>, removedConnection: Pair<String, String>): Boolean {
        val visited = mutableSetOf<String>()
        val queue = mutableSetOf(removedConnection.first)

        while (queue.isNotEmpty()) {
            val component = queue.first()
            if (component == removedConnection.second)
                return true
            queue.remove(component)
            visited += component
            queue += (connections[component]!! - visited)
        }
        return false
    }

    fun numberOfConnectedWays(connections: Map<String, Set<String>>, connection: Pair<String, String>): Int {
        
    }
    val fullConnectionsMap = toConnectionMap(connections)

    fun findSplit(): Pair<Int, Int> {
        for (a in 0..<connections.size - 2) {
            if (numberOfConnectedWays(fullConnectionsMap, connections[a]))
            for (b in a..connections.size - 1) {
                for (c in b..<connections.size) {
                    val cs = connections.filterIndexed { index, _ -> index !in listOf(a, b, c) }
                    val map = toConnectionMap(cs)

                    if (stillConnected(map, connections[a]) || stillConnected(map, connections[b]) || stillConnected(map, connections[c]))
                        continue

                    val groups = findGroups(map)
                    if (groups != null)
                        return groups
                }
            }
        }
        throw IllegalStateException()
    }

    val (a, b) = findSplit()

    println("Silver: ${a*b}")

}