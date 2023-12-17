val day17 = Solution { path ->
    val lines = readAsList(path)
    val blocks = lines.map { it.map { it.digitToInt() } }


    var visitedBlocks = mutableMapOf<Pair<Day17.Position, Day17.Direction>, Int>()
    var nodes = listOf(
        Day17.Node(Day17.Position(0, 0), Day17.Right(0), 0),
        Day17.Node(Day17.Position(0, 0), Day17.Down(0), 0),
    )

    while (nodes.isNotEmpty()) {
        val vertices = nodes.flatMap { node -> node.nextSteps(blocks) }
        nodes = vertices
            .mapNotNull { vertex ->
                val visitedBefore = visitedBlocks[vertex.position to vertex.direction]
                if (visitedBefore != null && visitedBefore <= vertex.heatLoss) {
                    return@mapNotNull null
                }
                visitedBlocks[vertex.position to vertex.direction] = vertex.heatLoss
                vertex
            }
    }

    val silver = visitedBlocks.filterKeys { it.first == Day17.Position(blocks[0].size - 1, blocks.size - 1) }
        .minBy { it.value }

    println("Silver: ${silver.value}")

    visitedBlocks = mutableMapOf()
    nodes = listOf(
        Day17.Node(Day17.Position(0, 0), Day17.Right(0), 0),
        Day17.Node(Day17.Position(0, 0), Day17.Down(0), 0),
    )

    while (nodes.isNotEmpty()) {
        val vertices = nodes.flatMap { node -> node.nextStep2(blocks) }
        nodes = vertices
            .mapNotNull { vertex ->
                val visitedBefore = visitedBlocks[vertex.position to vertex.direction]
                if (visitedBefore != null && visitedBefore <= vertex.heatLoss) {
                    return@mapNotNull null
                }
                visitedBlocks[vertex.position to vertex.direction] = vertex.heatLoss
                vertex
            }
    }

    val gold = visitedBlocks.filterKeys { it.first == Day17.Position(blocks[0].size - 1, blocks.size - 1) }
        .minBy { it.value }

    println("Gold: ${gold.value}")
}

object Day17 {
    sealed class Direction {
        abstract val repeated: Int
    }

    data class Up(override val repeated: Int) : Direction()
    data class Down(override val repeated: Int) : Direction()
    data class Left(override val repeated: Int) : Direction()
    data class Right(override val repeated: Int) : Direction()

    fun Direction.sameDirection(other: Direction): Boolean {
        return when (this) {
            is Up -> other is Up
            is Down -> other is Down
            is Left -> other is Left
            is Right -> other is Right
        }
    }

    data class Position(
        val x: Int,
        val y: Int,
    )

    data class Node(
        val position: Position,
        val direction: Direction,
        val heatLoss: Int,
    ) {
        fun nextSteps(blocks: List<List<Int>>): List<Node> {
            return when (direction) {
                is Up -> listOfNotNull(stepLeft(blocks), stepRight(blocks), if (direction.repeated == 3) null else stepUp(blocks))
                is Down -> listOfNotNull(stepLeft(blocks), stepRight(blocks), if (direction.repeated == 3) null else stepDown(blocks))
                is Left -> listOfNotNull(stepUp(blocks), stepDown(blocks), if (direction.repeated == 3) null else stepLeft(blocks))
                is Right -> listOfNotNull(stepUp(blocks), stepDown(blocks), if (direction.repeated == 3) null else stepRight(blocks))
            }
        }

        private fun stepUp(blocks: List<List<Int>>): Node? {
            if (position.y == 0) return null
            return Node(
                Position(position.x, position.y - 1),
                Up(if (direction is Up) direction.repeated + 1 else 1),
                heatLoss + blocks[position.y - 1][position.x],
            )
        }

        private fun stepDown(blocks: List<List<Int>>): Node? {
            if (position.y == blocks.size - 1) return null
            return Node(
                Position(position.x, position.y + 1),
                Down(if (direction is Down) direction.repeated + 1 else 1),
                heatLoss + blocks[position.y + 1][position.x],
            )
        }

        private fun stepLeft(blocks: List<List<Int>>): Node? {
            if (position.x == 0) return null
            return Node(
                Position(position.x - 1, position.y),
                Left(if (direction is Left) direction.repeated + 1 else 1),
                heatLoss + blocks[position.y][position.x - 1],
            )
        }

        private fun stepRight(blocks: List<List<Int>>): Node? {
            if (position.x == blocks[0].size - 1) return null
            return Node(
                Position(position.x + 1, position.y),
                Right(if (direction is Right) direction.repeated + 1 else 1),
                heatLoss + blocks[position.y][position.x + 1],
            )
        }

        fun nextStep2(blocks: List<List<Int>>): List<Node> {
            return if (direction.repeated < 4) {
                listOfNotNull(stepForward(blocks))
            } else if (direction.repeated < 10) {
                listOfNotNull(stepForward(blocks), stepCW(blocks), stepCCW(blocks))
            } else {
                listOfNotNull(stepCW(blocks), stepCCW(blocks))
            }
        }

        private fun stepForward(blocks: List<List<Int>>): Node? {
            return when (direction) {
                is Up -> stepUp(blocks)
                is Down -> stepDown(blocks)
                is Left -> stepLeft(blocks)
                is Right -> stepRight(blocks)
            }
        }

        private fun stepCW(blocks: List<List<Int>>): Node? {
            return when (direction) {
                is Up -> stepRight(blocks)
                is Down -> stepLeft(blocks)
                is Left -> stepUp(blocks)
                is Right -> stepDown(blocks)
            }
        }

        private fun stepCCW(blocks: List<List<Int>>): Node? {
            return when (direction) {
                is Up -> stepLeft(blocks)
                is Down -> stepRight(blocks)
                is Left -> stepDown(blocks)
                is Right -> stepUp(blocks)
            }
        }
    }
}
