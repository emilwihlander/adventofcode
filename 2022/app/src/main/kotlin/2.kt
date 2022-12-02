val convert = mapOf("A" to "X", "B" to "Y", "C" to "Z")

fun day2() {
    val lines = read("2.txt")

    val strats = lines.split("\n").filter { it != "" }.map { it.split(" ") }

    val scores = strats.map { (elf, you) ->
        val score = when (elf) {
            "A" -> if (you == "X") 3 else if (you == "Y") 6 else 0
            "B" -> if (you == "X") 0 else if (you == "Y") 3 else 6
            "C" -> if (you == "X") 6 else if (you == "Y") 0 else 3
            else -> throw IllegalStateException()
        }
        score + (you[0] - 'X') + 1
    }
    println(scores.sum())

    val scores2 = strats
        .map { (elf, state) ->
            val move = when (elf) {
                "A" -> if (state == "X") "Z" else if (state == "Y") "X" else "Y"
                "B" -> if (state == "X") "X" else if (state == "Y") "Y" else "Z"
                "C" -> if (state == "X") "Y" else if (state == "Y") "Z" else "X"
                else -> throw IllegalStateException()
            }
            elf to move
        }
        .map { (elf, you) ->
            val score = when (elf) {
                "A" -> if (you == "X") 3 else if (you == "Y") 6 else 0
                "B" -> if (you == "X") 0 else if (you == "Y") 3 else 6
                "C" -> if (you == "X") 6 else if (you == "Y") 0 else 3
                else -> throw IllegalStateException()
            }
            score + (you[0] - 'X') + 1
        }
    println(scores2.sum())
}
