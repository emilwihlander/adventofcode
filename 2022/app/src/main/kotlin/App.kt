/*
 * This Kotlin source file was generated by the Gradle 'init' task.
 */

fun readAsString(path: String): String =
    object {}.javaClass.getResource(path)!!.readText()

fun readAsList(path: String): List<String> =
    object {}.javaClass.getResource(path)!!.readText().split("\n").dropLast(1)

fun main() {
    day6()
}
