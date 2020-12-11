private fun seats() = input("day5").split("\n").map { Seeat(it) }

fun day5Part1() = seats().map { it.id }.maxOrNull()!!

fun day5Part2(): Int {
    val allSeats: List<Int> = (1 until 127)
        .flatMap { r -> (0..7).map { c -> r to c } }
        .map { (r, c) -> (r * 8) + c }
    val actualSeats = seats().map { it.id }
    val candidates = allSeats.filter { it !in actualSeats }
    return candidates
        .filterIndexed { idx, i -> candidates.hasContiguousNeighbours(idx, i).not() }
        .first()
}

private fun List<Int>.hasContiguousNeighbours(index: Int, value: Int): Boolean {
    return isContiguous(index - 1, value - 1) || isContiguous(index + 1, value + 1)
}

private fun List<Int>.isContiguous(index: Int, value: Int) = getOrNull(index)?.let { it == value } ?: false

private class Seeat(private val input: String) {

    val id: Int get() = (row * 8) + column
    private val row: Int get() = rowMoves.select((0..127).toList()).first()
    private val column: Int get() = columnMoves.select((0..7).toList()).first()
    private val rowMoves get() = input.take(7).mapNotNull { it.toMove() }
    private val columnMoves get() = input.drop(7).mapNotNull { it.toMove() }

    private tailrec fun List<Boolean>.select(candidates: List<Int>): List<Int> {
        return if (isNotEmpty()) drop(1).select(candidates.halve(first()))
        else candidates
    }

    private fun List<Int>.halve(takeUpper: Boolean) = if (takeUpper) drop(size / 2) else take(size / 2)

    private fun Char.toMove() = when (this) {
        'B', 'R' -> true
        'F', 'L' -> false
        else -> null
    }
}
