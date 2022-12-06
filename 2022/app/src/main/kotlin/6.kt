fun day6() {
    val buffer = readAsString("6.txt")

    var windows = buffer.windowed(4)
    for (index in windows.indices) {
        if (windows[index].toSet().size == 4) {
            println(index+4)
            break
        }
    }

    windows = buffer.windowed(14)
    for (index in windows.indices) {
        if (windows[index].toSet().size == 14) {
            println(index+14)
            break
        }
    }
}
