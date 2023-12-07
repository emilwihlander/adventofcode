import kotlin.math.sign

val day7 = Solution { path ->
    val lines = readAsList(path)

    val hands = lines.map { Hand.parse(it) }

    val sorted = hands.sortedWith(HandComparator()).reversed()

    val value = sorted.mapIndexed { index, hand -> (index + 1) * hand.id }.sum()

    println("Silver: $value")

    val jokerSorted = hands.sortedWith(JokerHandComparator()).reversed()

    val jokerValue = jokerSorted.mapIndexed { index, hand -> (index + 1) * hand.id }.sum()

    println("Gold: $jokerValue")
}

class HandComparator: Comparator<Hand> {
    override fun compare(a: Hand, b: Hand): Int {
        val aRanking = a.ranking()
        val bRanking = b.ranking()
        if (aRanking != bRanking) {
            return aRanking.compareTo(bRanking)
        } else {
            return if (a.hasHigherFirstCardThan(b)) -1 else 1
        }

    }
}

class JokerHandComparator: Comparator<Hand> {
    override fun compare(a: Hand, b: Hand): Int {
        val aRanking = a.ranking(true)
        val bRanking = b.ranking(true)
        if (aRanking != bRanking) {
            return aRanking.compareTo(bRanking)
        } else {
            return if (a.hasHigherFirstCardThan(b, true)) -1 else 1
        }

    }
}

data class Hand(
    val id: Int,
    val cards: List<Char>,
) {
    enum class Category {
        FIVE_OF_A_KIND,
        FOUR_OF_A_KIND,
        FULL_HOUSE,
        THREE_OF_A_KIND,
        TWO_PAIRS,
        PAIR,
        HIGH_CARD,
    }
    fun ranking(treatAsJoker: Boolean = false): Category {
        if (hasFiveOfAKind(treatAsJoker)) return Category.FIVE_OF_A_KIND
        if (hasFourOfAKind(treatAsJoker)) return Category.FOUR_OF_A_KIND
        if (hasFullHouse(treatAsJoker)) return Category.FULL_HOUSE
        if (hasThreeOfAKind(treatAsJoker)) return Category.THREE_OF_A_KIND
        if (hasTwoPairs(treatAsJoker)) return Category.TWO_PAIRS
        if (hasPair(treatAsJoker)) return Category.PAIR
        return Category.HIGH_CARD
    }
    fun hasHigherFirstCardThan(other: Hand, treatAsJoker: Boolean = false): Boolean {
        for (i in 0 until cards.size) {
            if (cardToInt(cards[i], treatAsJoker) > cardToInt(other.cards[i], treatAsJoker)) {
                return true
            } else if (cardToInt(cards[i], treatAsJoker) < cardToInt(other.cards[i], treatAsJoker)) {
                return false
            }
        }
        return false
    }
    fun hasFiveOfAKind(treatAsJoker: Boolean): Boolean {
        return if (treatAsJoker) {
            val jokers = cards.filter { it == 'J' }.size
            jokers == 5 || cards.filter { it != 'J' }.groupBy { it }.values.any { it.size == 5 - jokers }
        } else {
            cards.groupBy { it }.values.any { it.size == 5 }
        }
    }
    fun hasFourOfAKind(treatAsJoker: Boolean): Boolean {
        return if (treatAsJoker) {
            val jokers = cards.filter { it == 'J' }.size
            cards.filter { it != 'J' }.groupBy { it }.values.any { it.size == 4 - jokers }
        } else {
            cards.groupBy { it }.values.any { it.size == 4 }
        }
    }
    fun hasFullHouse(treatAsJoker: Boolean): Boolean {
        return if (treatAsJoker) {
            val jokers = cards.filter { it == 'J' }.size
            hasFullHouse(false) || jokers >= 1 && (hasTwoPairs(false) || hasThreeOfAKind(false))
        } else {
            cards.groupBy { it }.values.any { it.size == 3 } && cards.groupBy { it }.values.any { it.size == 2 }
        }
    }
    fun hasThreeOfAKind(treatAsJoker: Boolean): Boolean {
        return if (treatAsJoker) {
            val jokers = cards.filter { it == 'J' }.size
            cards.filter { it != 'J' }.groupBy { it }.values.any { it.size == 3 - jokers }
        } else {
            cards.groupBy { it }.values.any { it.size == 3 }
        }

    }
    fun hasTwoPairs(treatAsJoker: Boolean): Boolean {
        return if (treatAsJoker) {
            val jokers = cards.filter { it == 'J' }.size
            cards.filter { it != 'J' }.groupBy { it }.values.filter { it.size == 2 }.size == 2 - jokers
        } else {
            cards.groupBy { it }.values.filter { it.size == 2 }.size == 2
        }
    }
    fun hasPair(treatAsJoker: Boolean): Boolean {
        return if (treatAsJoker) {
            val jokers = cards.filter { it == 'J' }.size
            return jokers >= 1 || cards.filter { it != 'J' }.groupBy { it }.values.any { it.size == 2 }
        } else {
            cards.groupBy { it }.values.any { it.size == 2 }
        }
    }

    companion object {
        fun parse(line: String): Hand {
            val (handString, idString) = line.split(" ")
            return Hand(idString.toInt(), handString.map { it })
        }
    }
}

fun cardToInt(card: Char, treatAsJoker: Boolean): Int {
    if (treatAsJoker && card == 'J') return 1
    return when (card) {
        'A' -> 14
        'K' -> 13
        'Q' -> 12
        'J' -> 11
        'T' -> 10
        else -> card.toString().toInt()
    }
}
