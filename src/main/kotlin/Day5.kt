fun day5(): Int {
    return input("day5")
        .split("\n")
        .map { Seat(it).id }
        .maxOrNull()!!
}

class Seat(private val input: String) {

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
