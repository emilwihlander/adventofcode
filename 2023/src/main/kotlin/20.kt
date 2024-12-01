val day20 = Solution { path ->
    val lines = readAsList(path)
    val modules = lines.map { CommunicationModule.parse(it) }

    modules.forEach {
        if (it is ConjunctionModule) it.initialize(modules)
    }

    val nullModule = BroadcasterModule(emptyList())

    val moduleMap = modules.associateBy { it.name }

    var highPulses = 0L
    var lowPulses = 0L

    for (i in 1..1000) {
        var signals = listOf(Signal("button", "broadcaster", Signal.Type.LOW))
        while (signals.isNotEmpty()) {
            signals.forEach { when (it.type) { Signal.Type.LOW -> lowPulses++; Signal.Type.HIGH -> highPulses++ } }

            signals = signals.flatMap { signal ->
                moduleMap.getOrDefault(signal.target, nullModule).receive(signal)
            }
        }
    }

    println("Silver: ${highPulses * lowPulses}")

    if (!modules.any { "rx" in it.targets })
        return@Solution

    var i = 1L

    while (true) {
        var signals = listOf(Signal("button", "broadcaster", Signal.Type.LOW))
        while (signals.isNotEmpty()) {
            if (signals.any { it.target == "rx" && it.type == Signal.Type.LOW }) {
                println("Gold: $i")
                return@Solution
            }
            signals = signals.flatMap { signal ->
                moduleMap.getOrDefault(signal.target, nullModule).receive(signal)
            }
        }
        i++
    }
}

data class Signal(
    val source: String,
    val target: String,
    val type: Type,
) {
    enum class Type { LOW, HIGH }
}

abstract class CommunicationModule(
    val name: String,
    val targets: List<String>,
) {
    abstract fun receive(signal: Signal): List<Signal>

    companion object {
        fun parse(line: String): CommunicationModule {
            val (name, targetString) = line.split(" -> ")

            val targets = targetString.trim().split(", ")
            return if (name == "broadcaster") {
                BroadcasterModule(targets)
            } else if (name.startsWith("%")) {
                FlipFlopModule(name.drop(1), targets)
            } else if (name.startsWith("&")) {
                ConjunctionModule(name.drop(1), targets)
            } else {
                throw IllegalStateException()
            }
        }
    }
}

class BroadcasterModule(targets: List<String>): CommunicationModule("broadcaster", targets) {
    override fun receive(signal: Signal): List<Signal> {
        return targets.map { Signal(name, it, Signal.Type.LOW) }
    }
}
class FlipFlopModule(name: String, targets: List<String>): CommunicationModule(name, targets) {
    var on = false
    override fun receive(signal: Signal): List<Signal> {
        if (signal.type == Signal.Type.HIGH)
            return emptyList()

        on = !on
        return targets.map { Signal(name, it, if (on) Signal.Type.HIGH else Signal.Type.LOW) }
    }
}
class ConjunctionModule(name: String, targets: List<String>): CommunicationModule(name, targets) {
    val memory = mutableMapOf<String, Signal.Type>()

    fun initialize(modules: List<CommunicationModule>) {
        modules.filter { name in it.targets }.forEach {
            memory[it.name] = Signal.Type.LOW
        }
    }

    override fun receive(signal: Signal): List<Signal> {
        memory[signal.source] = signal.type

        if (name == "ls" && memory.filter { it.value == Signal.Type.HIGH }.size >= 2) {
            println(memory)
        }

        val type = if (memory.values.all { it == Signal.Type.HIGH }) {
            Signal.Type.LOW
        } else {
            Signal.Type.HIGH
        }

        return targets.map { Signal(name, it, type) }
    }
}

