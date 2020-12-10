const val PREAMBLE_SIZE = 25

private fun xmas() = input("day9").split("\n").mapNotNull { it.toIntOrNull() }

fun day9Part1(): Int {
    val xmas = xmas()
    return xmas
        .filterIndexed { index, _ -> index >= PREAMBLE_SIZE }
        .mapIndexed { index, it -> index + PREAMBLE_SIZE to it }
        .map { (index, it) -> xmas.preamble(index) to it }
        .first { (preamble, sum) -> preamble.isValid(sum).not() }
        .second
}

fun day9Part2(): Int? {
    val xmas = xmas()
    val weak = day9Part1()
    return xmas
        .mapIndexed { index, _ -> xmas.weakness(index, weak) }
        .filterNotNull()
        .firstOrNull()
}

private fun List<Int>.weakness(fromIndex: Int, sum: Int): Int? {
    val candidates = drop(fromIndex)
    val range = candidates
        .runningFold(0) { acc, i -> acc + i }
        .indexOfFirst { it == sum }
        .takeUnless { it == -1 }
        ?.let { candidates.take(it) }
    val (smallest, highest) = range?.minOrNull() to range?.maxOrNull()
    return smallest?.let { s -> highest?.let { h -> s + h } }
}

private fun List<Int>.preamble(index: Int) = take(index).takeLast(PREAMBLE_SIZE)

private fun List<Int>.isValid(sum: Int): Boolean {
    return mapIndexed { index, it -> index to it }
        .any { (index, it) -> (sum - it) in filterIndexed { idx, _ -> idx != index } }
}