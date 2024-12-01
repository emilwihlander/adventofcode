val day22 = Solution { path ->
    val lines = readAsList(path)

    val bricks = lines.map { Brick.parse(it) }

    val fallenBricks = applyGravity(bricks)

    val canBeRemoved = canBeRemoved(fallenBricks)

    println("Silver: ${canBeRemoved.size}")

    val numberOfFalls = numberWouldFall(fallenBricks)

    println("Gold: ${numberOfFalls.sum()}")
}

fun numberWouldFall(bricks: List<Brick>): List<Int> {
    val wouldFall = mutableMapOf<Brick, Set<Brick>>()
    wouldFall.putAll(bricks.filter { it.z.first == 1 }.map { it to emptySet() })
    for (i in bricks.minOf { it.z.last }.. bricks.maxOf { it.z.last }) {
        val bases = bricks.filter { it.z.last == i }
        val resting = bricks.filter { it.z.first == i + 1 }
        resting.map { it to it.intersects(bases) }.forEach { (rest, intersects) ->
            wouldFall[rest] = intersects.toSet()
        }
    }

    val numberOfFalls = mutableListOf<Set<Brick>>()

    for (i in bricks.indices) {
        val fallen = mutableSetOf(bricks[i])

        for (j in (i + 1)..< bricks.size) {
            val needed = wouldFall[bricks[j]]!!
            if (needed.isNotEmpty() && needed.all { it in fallen }) {
                fallen.add(bricks[j])
            }
        }
        numberOfFalls.add(fallen - bricks[i])
    }

    return numberOfFalls.map { it.size }
}

fun canBeRemoved(bricks: List<Brick>): List<Brick> {
    val canBeRemoved = mutableSetOf<Brick>()
    for (i in bricks.minOf { it.z.last }.. bricks.maxOf { it.z.last }) {
        val bases = bricks.filter { it.z.last == i }
        val resting = bricks.filter { it.z.first == i + 1 }
        bases.map { it to it.intersects(resting) }.forEach { (base, intersects) ->
            if (intersects.size == 0 || intersects.all { it.intersects(bases).size > 1 }) {
                canBeRemoved.add(base)
            }
        }
    }

    return canBeRemoved.toList()
}

fun applyGravity(bricks: List<Brick>): List<Brick> {
    val sorted = bricks.sortedBy { it.z.first }

    return sorted.fold(listOf()) { acc, next ->
        val collisions = next.intersects(acc)
        if (collisions.isEmpty()) {
            acc + next.moveTo(1)
        } else {
            acc + next.moveTo(collisions.maxOf { it.z.last } + 1)
        }
    }
}

data class Pos3D(
    val x: Int,
    val y: Int,
    val z: Int,
) {
    companion object {
        fun parse(line: String): Pos3D {
            val (x, y, z) = line.split(",").map { it.toInt() }
            return Pos3D(x, y, z)
        }
    }
}

data class Brick(
    val x: IntRange,
    val y: IntRange,
    val z: IntRange,
) {
    fun moveTo(newLowestZ: Int): Brick {
        if (newLowestZ == z.first) return this
        return copy(
            z = newLowestZ..(z.last - z.first + newLowestZ)
        )
    }
    fun intersects(bricks: List<Brick>): List<Brick> {
        return bricks.filter {
            it.x.intersect(x).isNotEmpty() && it.y.intersect(y).isNotEmpty()
        }
    }
    companion object {
        fun parse(line: String): Brick {
            val (pos1, pos2) = line.split("~").map { it.split(",").map { it.toInt() } }
            val x = if (pos1[0] < pos2[0]) pos1[0]..pos2[0] else pos2[0]..pos1[0]
            val y = if (pos1[1] < pos2[1]) pos1[1]..pos2[1] else pos2[1]..pos1[1]
            val z = if (pos1[2] < pos2[2]) pos1[2]..pos2[2] else pos2[2]..pos1[2]
            return Brick(x, y, z)
        }
    }
}