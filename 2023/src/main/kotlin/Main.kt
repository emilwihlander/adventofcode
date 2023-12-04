import java.time.LocalDate

val solutions = listOf(
    day1,
    day2,
    day3,
    day4,
)

fun readAsString(path: String): String =
    object {}.javaClass.getResource(path)!!.readText()

fun readAsList(path: String): List<String> =
    object {}.javaClass.getResource(path)!!.readText().split("\n").dropLast(1)

fun main(args: Array<String>) {
    val day = LocalDate.now().dayOfMonth
    val solution = solutions[day - 1]

    println("Day $day")
    println("------TEST------")
    solution.solve("$day.test")
    println("------MAIN------")
    solution.solve("$day.input")
}
