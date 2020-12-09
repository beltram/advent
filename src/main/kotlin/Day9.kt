const val PREAMBLE_SIZE = 25

private fun xmas() = input("day9").split("\n").mapNotNull { it.toIntOrNull() }

fun day9(): Int {
    val xmas = xmas()
    return xmas
        .filterIndexed { index, _ -> index >= PREAMBLE_SIZE }
        .mapIndexed { index, it -> index + PREAMBLE_SIZE to it }
        .map { (index, it) -> xmas.preamble(index) to it }
        .first { (preamble, sum) -> preamble.isValid(sum).not() }
        .second
}

private fun List<Int>.preamble(index: Int) = take(index).takeLast(PREAMBLE_SIZE)

private fun List<Int>.isValid(sum: Int): Boolean {
    return mapIndexed { index, it -> index to it }
        .any { (index, it) -> (sum - it) in filterIndexed { idx, _ -> idx != index } }
}