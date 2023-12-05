val day5 = Solution { path ->
    val lines = readAsList(path)

    val seeds = lines[0].split(":")[1].split(" ").filter { it.isNotEmpty() }.map { it.toLong() }

    val seedToSoil = mutableMapOf<LongRange, Long>()
    val soilToFertilizer = mutableMapOf<LongRange, Long>()
    val fertilizerToWater = mutableMapOf<LongRange, Long>()
    val waterToLight = mutableMapOf<LongRange, Long>()
    val lightToTemperature = mutableMapOf<LongRange, Long>()
    val temperatureToHumidity = mutableMapOf<LongRange, Long>()
    val humidityToLocation = mutableMapOf<LongRange, Long>()

    var i = 0

    i = lines.indexOf("seed-to-soil map:") + 1
    while (lines[i].isNotEmpty()) {
        parseLine(lines[i], seedToSoil)
        i++
    }

    i = lines.indexOf("soil-to-fertilizer map:") + 1
    while (lines[i].isNotEmpty()) {
        parseLine(lines[i], soilToFertilizer)
        i++
    }

    i = lines.indexOf("fertilizer-to-water map:") + 1
    while (lines[i].isNotEmpty()) {
        parseLine(lines[i], fertilizerToWater)
        i++
    }

    i = lines.indexOf("water-to-light map:") + 1
    while (lines[i].isNotEmpty()) {
        parseLine(lines[i], waterToLight)
        i++
    }

    i = lines.indexOf("light-to-temperature map:") + 1
    while (lines[i].isNotEmpty()) {
        parseLine(lines[i], lightToTemperature)
        i++
    }

    i = lines.indexOf("temperature-to-humidity map:") + 1
    while (lines[i].isNotEmpty()) {
        parseLine(lines[i], temperatureToHumidity)
        i++
    }

    i = lines.indexOf("humidity-to-location map:") + 1
    while (i < lines.size) {
        parseLine(lines[i], humidityToLocation)
        i++
    }

    val locations = seeds
        .map { mapCategory(it, seedToSoil) }
        .map { mapCategory(it, soilToFertilizer) }
        .map { mapCategory(it, fertilizerToWater) }
        .map { mapCategory(it, waterToLight) }
        .map { mapCategory(it, lightToTemperature) }
        .map { mapCategory(it, temperatureToHumidity) }
        .map { mapCategory(it, humidityToLocation) }

    println("Silver: ${locations.min()}")

    gold(lines)
}

fun gold(lines: List<String>) {
    val seedRanges = lines[0].split(":")[1]
        .split(" ")
        .filter { it.isNotEmpty() }
        .map { it.toLong() }
        .chunked(2)
        .map { (start, range) -> start..start + range }

    val seedToSoil = mutableMapOf<LongRange, Long>()
    val soilToFertilizer = mutableMapOf<LongRange, Long>()
    val fertilizerToWater = mutableMapOf<LongRange, Long>()
    val waterToLight = mutableMapOf<LongRange, Long>()
    val lightToTemperature = mutableMapOf<LongRange, Long>()
    val temperatureToHumidity = mutableMapOf<LongRange, Long>()
    val humidityToLocation = mutableMapOf<LongRange, Long>()

    val locationToHumidity = mutableMapOf<LongRange, Long>()
    val humidityToTemperature = mutableMapOf<LongRange, Long>()
    val temperatureToLight = mutableMapOf<LongRange, Long>()
    val lightToWater = mutableMapOf<LongRange, Long>()
    val waterToFertilizer = mutableMapOf<LongRange, Long>()
    val fertilizerToSoil = mutableMapOf<LongRange, Long>()
    val soilToSeed = mutableMapOf<LongRange, Long>()


    var i = 0

    i = lines.indexOf("seed-to-soil map:") + 1
    while (lines[i].isNotEmpty()) {
        parseLine2(lines[i], soilToSeed)
        i++
    }

    i = lines.indexOf("soil-to-fertilizer map:") + 1
    while (lines[i].isNotEmpty()) {
        parseLine2(lines[i], fertilizerToSoil)
        i++
    }

    i = lines.indexOf("fertilizer-to-water map:") + 1
    while (lines[i].isNotEmpty()) {
        parseLine2(lines[i], waterToFertilizer)
        i++
    }

    i = lines.indexOf("water-to-light map:") + 1
    while (lines[i].isNotEmpty()) {
        parseLine2(lines[i], lightToWater)
        i++
    }

    i = lines.indexOf("light-to-temperature map:") + 1
    while (lines[i].isNotEmpty()) {
        parseLine2(lines[i], temperatureToLight)
        i++
    }

    i = lines.indexOf("temperature-to-humidity map:") + 1
    while (lines[i].isNotEmpty()) {
        parseLine2(lines[i], humidityToTemperature)
        i++
    }

    i = lines.indexOf("humidity-to-location map:") + 1
    while (i < lines.size) {
        parseLine2(lines[i], locationToHumidity)
        i++
    }

    var location = 0L

    while (true) {
        val seed = location
            .let { mapCategory(it, locationToHumidity) }
            .let { mapCategory(it, humidityToTemperature) }
            .let { mapCategory(it, temperatureToLight) }
            .let { mapCategory(it, lightToWater) }
            .let { mapCategory(it, waterToFertilizer) }
            .let { mapCategory(it, fertilizerToSoil) }
            .let { mapCategory(it, soilToSeed) }

        if (seedRanges.any { seed in it }) {
            break
        } else {
            location++
        }
    }

    println("Gold: $location")
}

fun parseLine(line: String, map: MutableMap<LongRange, Long>) {
    val (destination, source, range) = line.split(" ").filter { it.isNotEmpty() }.map { it.toLong() }

    map[source..< source + range] = destination
}

fun mapCategory(value: Long, map: MutableMap<LongRange, Long>): Long {
    val mapper = map.entries.find { value in it.key }
    return if (mapper != null) {
        mapper.value + (value - mapper.key.first)
    } else {
        value
    }
}

fun parseLine2(line: String, map: MutableMap<LongRange, Long>) {
    val (destination, source, range) = line.split(" ").filter { it.isNotEmpty() }.map { it.toLong() }

    map[destination..< destination + range] = source
}

fun mapCategoryRanges(ranges: List<LongRange>, map: MutableMap<LongRange, Long>): List<LongRange> {
    return ranges
        .flatMap { range ->
            val mapRanges = map.entries.filter { range.intersect(it.key).isNotEmpty() }

            val mappedRanges = mapRanges.map { mapRange ->
                val start = mapRange.value + (range.first - mapRange.key.first)
                val end = mapRange.value + (range.last - mapRange.key.first)
                start..end
            }
            mappedRanges + excludeRangesFromRange(range, mapRanges.map { it.key })
        }
}

fun excludeRangesFromRange(range: LongRange, toExclude: List<LongRange>): List<LongRange> {
    val ranges = mutableListOf<LongRange>()
    var i = range.first
    while (i <= range.last) {
        val excluded = toExclude.filter { i in it }
        if (excluded.isEmpty()) {
            val newLast = toExclude.filter { it.first > i }.ifEmpty { listOf(range.last + 1..range.last + 1) }.minBy { it.first }.last
            ranges.add(i..toExclude.filter { it.first > i }.ifEmpty { listOf(range.last + 1..range.last + 1) }.minBy { it.first }.last)
            i = newLast + 1
        } else {
            val nextStep = excluded.map { it.last }.max()
            i = nextStep + 1
        }
    }
    return ranges
}
