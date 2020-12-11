private fun adapters() = input("day10").split("\n").mapNotNull { it.toIntOrNull() }.distinct()

private fun candidates() = adapters().toMutableList().apply { add(0); add(deviceJoltage()) }.sorted()

fun day10Part1(): Int {
    val candidates = candidates()
    val (diff1, diff3) = candidates
        .mapIndexed { index, _ -> candidates.adjacentDiff(index) }
        .filterNotNull()
        .groupBy { it }
        .values
        .map { it.size }
    return diff1 * diff3
}

fun day10Part2() = candidates().run { indices.map { nextIndexes(it) }.traverse() }

private tailrec fun List<List<Int>>.traverse(index: Int = 0, cache: MutableMap<Int, Long> = mutableMapOf()): Long {
    return elementAtOrNull(index)
        ?.takeUnless { it.isEmpty() }
        ?.sumOf { idx -> cache[idx] ?: traverse(idx, cache).also { cache[idx] = it } }
        ?: 1
}

private fun List<Int>.adjacentDiff(index: Int) = getOrNull(index + 1)?.minus(this[index])

private fun List<Int>.nextIndexes(index: Int): List<Int> {
    return elementAtOrNull(index)
        ?.let { current ->
            drop(index + 1)
                .mapIndexed { idx, i -> idx to i }
                .takeWhile { (_, it) -> it - current <= 3 }
                .map { (idx, _) -> idx + index + 1 }
        }.orEmpty()
}

private fun List<Int>.deviceJoltage() = maxOrNull()?.plus(3) ?: 3