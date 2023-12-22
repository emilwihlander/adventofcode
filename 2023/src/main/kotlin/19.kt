import java.math.BigInteger

val day19 = Solution { path ->
    val lines = readAsList(path)

    val workflows = lines.takeWhile { it.isNotEmpty() }.map { Workflow.parse(it) }.associateBy { it.name }

    val parts = lines.takeLastWhile { it.isNotEmpty() }.map { parsePart(it) }

    val currents = parts.map { "in" to it }.toMutableList()
    val accepted = mutableListOf<Part>()

    while (currents.isNotEmpty()) {
        val (workflowName, part) = currents.removeFirst()
        val action = workflows[workflowName]!!.apply(part)

        if (action is Workflow.Accept) {
            accepted.add(part)
        } else if (action is Workflow.Goto) {
            currents.add(action.workflow to part)
        }
    }

    val silver = accepted.map { it.values.sum() }.sum()

    println("Silver: $silver")

    val currentRanges = mutableListOf(
        "in" to mapOf(
            'x' to 1..4000,
            'm' to 1..4000,
            'a' to 1..4000,
            's' to 1..4000,
        )
    )

    val acceptedRanges = mutableListOf<PartRange>()

    while (currentRanges.isNotEmpty()) {
        val (workflowName, partRange) = currentRanges.removeFirst()
        val paths = workflows[workflowName]!!.applyRange(partRange)

        for ((action, range) in paths) {
            if (action is Workflow.Accept) {
                acceptedRanges.add(range)
            } else if (action is Workflow.Goto) {
                currentRanges.add(action.workflow to range)
            }
        }
    }

    val gold = acceptedRanges.map {
        it.getSize('x') * it.getSize('m') * it.getSize('a') * it.getSize('s')
    }.reduce { a, b -> a + b }

    println("Gold: $gold")
}
typealias Part = Map<Char, Long>
typealias PartRange = Map<Char, IntRange>

fun PartRange.getSize(c: Char): BigInteger = (get(c)!!.last - get(c)!!.first + 1).toBigInteger()

data class Workflow(
    val name: String,
    val steps: List<Pair<Condition, Action>>,
    val elseStep: Action,
) {
    fun applyRange(partRange: PartRange): List<Pair<Action, PartRange>> {
        val ranges = mutableListOf<Pair<Action, PartRange>>()

        var remaining = partRange

        for ((cond, action) in steps) {
            val (matching, rem) = cond.split(remaining)
            if (matching != null) {
                ranges.add(action to matching)
            }
            if (rem == null) {
                return ranges
            }
            remaining = rem
        }
        ranges.add(elseStep to remaining)

        return ranges
    }

    fun apply(part: Part): Action {
        for ((cond, action) in steps) {
            if (cond.matches(part)) return action
        }
        return elseStep
    }
    data class Condition(
        val target: Char,
        val op: Char,
        val value: Int,
    ) {
        fun matches(part: Part): Boolean {
            return when (op) {
                '<' -> part[target]!! < value
                '>' -> part[target]!! > value
                else -> throw IllegalStateException()
            }
        }

        fun split(partRange: PartRange): Pair<PartRange?, PartRange?> {
            val targetRange = partRange[target]!!

            val (matching, remaining) = when (op) {
                '<' -> targetRange.first..(value - 1) to value..targetRange.last
                '>' -> (value + 1)..targetRange.last to targetRange.first..value
                else -> throw IllegalStateException()
            }

            return Pair(
                matching.let { if (it.first <= it.last) partRange.copy(target, it) else null },
                remaining.let { if (it.first <= it.last) partRange.copy(target, it) else null },
            )
        }

        private fun PartRange.copy(target: Char, range: IntRange): PartRange {
            return entries.map { if (it.key == target) target to range else it.key to it.value }.toMap()
        }
    }

    sealed interface Action
    class Goto(val workflow: String) : Action
    class Accept : Action
    class Reject : Action

    companion object {
        fun parse(line: String): Workflow {
            val (name, conds) = line.split('{', '}')

            val allSteps = conds.split(',')

            val steps = allSteps.subList(0, allSteps.size - 1).map {
                val (cond, action) = it.split(':')
                parseCondition(cond) to parseAction(action)
            }

            return Workflow(
                name,
                steps,
                parseAction(allSteps.last())
            )
        }

        private fun parseCondition(cond: String): Condition {
            return Condition(
                cond[0],
                cond[1],
                cond.substring(2).toInt()
            )
        }

        private fun parseAction(action: String): Action {
            return when (action) {
                "A" -> Accept()
                "R" -> Reject()
                else -> Goto(action)
            }
        }
    }
}

private fun parsePart(part: String): Part {
    val numberRegex = Regex("\\d+")
    val keys = listOf('x', 'm', 'a', 's')
    val values = numberRegex.findAll(part).map { it.value.toLong() }.toList()
    return keys.zip(values).toMap()
}
