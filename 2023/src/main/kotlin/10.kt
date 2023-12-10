const val RESET = "\u001B[0m"
const val RED = "\u001B[31m"
const val BLUE = "\u001B[34m"

val day10 = Solution { path ->
    val lines = readAsList(path)

    val matrix = lines.map { it.map { it } }
    val sPosition = findSPosition(matrix)
    val (aPath, bPath) = findStartPositions(matrix, sPosition)

    var (aPosition, aDirection) = aPath
    var (bPosition, bDirection) = bPath

    var distance = 1

    val loopPositions = mutableSetOf(sPosition, aPosition.copy(), bPosition.copy())

    while (aPosition != bPosition) {
        aPosition = aDirection.step(aPosition)
        bPosition = bDirection.step(bPosition)
        loopPositions.add(aPosition.copy())
        loopPositions.add(bPosition.copy())
        aDirection = nextDirection(matrix, aDirection, aPosition.first, aPosition.second)
        bDirection = nextDirection(matrix, bDirection, bPosition.first, bPosition.second)
        distance++
    }

    println("Silver: $distance")

    val enclosedPositions = mutableSetOf<Pair<Int, Int>>()

    for (y in matrix.indices) {
        val rowLoops = loopPositions.filter { it.second == y }.map { it.first }.sorted()
        val passingPipes = rowLoops
            .filter { matrix[y][it] != '-' }
            .windowed(2, partialWindows = true)
            .filter {if (it.size == 1) matrix[y][it[0]] == '|' else
                matrix[y][it[0]] == '|'
                        || matrix[y][it[0]] == 'L' && matrix[y][it[1]] == '7'
                        || matrix[y][it[0]] == 'F' && matrix[y][it[1]] == 'J'

            }
            .map { it[0] }
        for (x in matrix[0].indices) {
            if (x !in rowLoops && passingPipes.filter { it < x }.size % 2 == 1) {
                enclosedPositions += x to y
            }
        }
    }

    printMatrix(matrix, loopPositions, enclosedPositions)

    println("Gold: ${enclosedPositions.size}")
}

fun printMatrix(matrix: List<List<Char>>, loopPositions: Set<Pair<Int, Int>>, enclosedPositions: Set<Pair<Int, Int>>) {
    for (y in matrix.indices) {
        for (x in matrix[0].indices) {

            if (x to y in loopPositions) {
                print(RED + matrix[y][x] + RESET)
            } else if (x to y in enclosedPositions) {
                print(BLUE + matrix[y][x] + RESET)
            } else {
                print(matrix[y][x])
            }
        }
        println()
    }
    println()
}

fun findSPosition(matrix: List<List<Char>>): Pair<Int, Int> =
    matrix.find { it.contains('S') }!!.indexOf('S') to matrix.indexOf(matrix.find { it.contains('S') }!!)

fun findStartPositions(matrix: List<List<Char>>, sPosition: Pair<Int, Int>): List<Pair<Pair<Int, Int>, Direction>> {
    val positions = mutableListOf<Pair<Pair<Int, Int>, Direction>>()
    if (sPosition.first > 0) {
        val direction = when (matrix[sPosition.second][sPosition.first - 1]) {
            '-' -> Direction.WEST
            'F' -> Direction.SOUTH
            'L' -> Direction.NORTH
            else -> null
        }
        if (direction != null)
            positions.add((sPosition.first - 1 to sPosition.second) to direction)
    }
    if (sPosition.first < matrix[0].size - 1) {
        val direction = when (matrix[sPosition.second][sPosition.first + 1]) {
            '-' -> Direction.EAST
            '7' -> Direction.SOUTH
            'J' -> Direction.NORTH
            else -> null
        }
        if (direction != null)
            positions.add((sPosition.first + 1 to sPosition.second) to direction)
    }
    if (sPosition.second > 0) {
        val direction = when (matrix[sPosition.second - 1][sPosition.first]) {
            '|' -> Direction.NORTH
            '7' -> Direction.WEST
            'F' -> Direction.EAST
            else -> null
        }
        if (direction != null)
            positions.add((sPosition.first to sPosition.second - 1) to direction)
    }
    if (sPosition.second < matrix.size - 1) {
        val direction = when (matrix[sPosition.second + 1][sPosition.first]) {
            '|' -> Direction.SOUTH
            'L' -> Direction.EAST
            'J' -> Direction.WEST
            else -> null
        }
        if (direction != null)
            positions.add((sPosition.first to sPosition.second + 1) to direction)
    }

    return positions
}


enum class Direction(val step: (Pair<Int, Int>) -> Pair<Int, Int>) {
    NORTH({ (x, y) -> Pair(x, y - 1) }),
    SOUTH({ (x, y) -> Pair(x, y + 1) }),
    EAST({ (x, y) -> Pair(x + 1, y) }),
    WEST({ (x, y) -> Pair(x - 1, y) }),
}

fun nextDirection(matrix: List<List<Char>>, inDirection: Direction, x: Int, y: Int): Direction {
    val pipe = matrix[y][x]
    return when (pipe) {
        '|' -> inDirection
        '-' -> inDirection
        'L' -> if (inDirection == Direction.SOUTH) Direction.EAST else Direction.NORTH
        'J' -> if (inDirection == Direction.SOUTH) Direction.WEST else Direction.NORTH
        '7' -> if (inDirection == Direction.NORTH) Direction.WEST else Direction.SOUTH
        'F' -> if (inDirection == Direction.NORTH) Direction.EAST else Direction.SOUTH
        else -> throw Exception("Unknown pipe: $pipe")
    }
}
