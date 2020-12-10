private fun adapters() = input("day10").split("\n").mapNotNull { it.toIntOrNull() }.distinct()

fun day10(): Int {
    val adapters = adapters().toMutableList()
    val candidates = adapters.apply { add(0); add(adapters.deviceJoltage()) }.sorted()
    val (diff1, diff3) = candidates
        .mapIndexed { index, _ -> candidates.adjacentDiff(index) }
        .filterNotNull()
        .groupBy { it }
        .values
        .map { it.size }
    return diff1 * diff3
}

private fun List<Int>.adjacentDiff(index: Int) = getOrNull(index + 1)?.minus(this[index])

private fun List<Int>.deviceJoltage() = maxOrNull()?.plus(3) ?: 3