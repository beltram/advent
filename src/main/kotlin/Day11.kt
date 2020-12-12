private fun waitingArea(): WaitingArea {
    return input("day11")
        .split("\n")
        .map { it.map { s -> Seat(s) } }
        .map { SeatRow(it) }
        .let { WaitingArea(it) }
}

fun day11Part1() = waitingArea().stabilizedP1().nbOccupied()

private tailrec fun WaitingArea.stabilizedP1(): WaitingArea {
    return evolve(4, ::adjacents).let { next -> if (this != next) next.stabilizedP1() else this }
}

fun day11Part2() = waitingArea().stabilizedP2().nbOccupied()

private tailrec fun WaitingArea.stabilizedP2(): WaitingArea {
    return evolve(5, ::seesOccupied).let { next -> if (this != next) next.stabilizedP2() else this }
}

internal typealias Observer = (WaitingArea.SeatIter) -> Int

internal data class WaitingArea(val rows: List<SeatRow>) {
    fun evolve(threshold: Int, observer: Observer): WaitingArea {
        return rows
            .mapIndexed { rowIdx, row -> row.seats.mapIndexed { seatIdx, seat -> SeatIter(seat, rowIdx, seatIdx) } }
            .map { it.map { iter -> observer(iter).let { iter.applyRules(it, threshold) } } }
            .map { seats -> SeatRow(seats.map { it.seat }) }
            .let { WaitingArea(it) }
    }

    internal data class SeatIter(val seat: Seat, val rowIdx: Int, val seatIdx: Int) {

        fun applyRules(nb: Int, threshold: Int) = rule1(nb).rule2(nb, threshold)

        private fun rule1(nbOccupied: Int): SeatIter {
            return takeUnless { seat.isEmpty && nbOccupied == 0 } ?: SeatIter(Seat.occupied, rowIdx, seatIdx)
        }

        private fun rule2(nbOccupied: Int, threshold: Int): SeatIter {
            return takeUnless { seat.isOccupied && nbOccupied >= threshold } ?: SeatIter(Seat.empty, rowIdx, seatIdx)
        }
    }

    fun adjacents(iter: SeatIter): Int = iter.run {
        val currentRow = rows.getOrNull(rowIdx)
        val right = currentRow?.seats?.getOrNull(seatIdx + 1)
        val left = currentRow?.seats?.getOrNull(seatIdx - 1)
        val previousRow = rows.getOrNull(rowIdx - 1)
        val topLeft = previousRow?.seats?.getOrNull(seatIdx - 1)
        val top = previousRow?.seats?.getOrNull(seatIdx)
        val topRight = previousRow?.seats?.getOrNull(seatIdx + 1)
        val nextRow = rows.getOrNull(rowIdx + 1)
        val bottomLeft = nextRow?.seats?.getOrNull(seatIdx - 1)
        val bottom = nextRow?.seats?.getOrNull(seatIdx)
        val bottomRight = nextRow?.seats?.getOrNull(seatIdx + 1)
        return listOfNotNull(topLeft, top, topRight, right, bottomRight, bottom, bottomLeft, left)
            .count { it.isOccupied }
    }

    internal data class Pos(val rowIdx: Int, val seatIdx: Int) {
        fun next(bottom: Int, right: Int) = Pos(rowIdx + bottom, seatIdx + right)
    }

    private val Pos.currentRow get() = rows.getOrNull(rowIdx)
    private val Pos.currentSeat get() = currentRow?.seats?.getOrNull(seatIdx)
    private fun Pos.nextSeat(incrBottom: Int, incrRight: Int): Boolean {
        return currentSeat?.let {
            if (it.isSeat) it.isOccupied || it.isSeat.not()
            else find(incrBottom, incrRight)
        } ?: false
    }

    private fun Pos.find(bottom: Int, right: Int) = next(bottom, right).nextSeat(bottom, right)

    private val range get() = -1..1
    private val matrix get() = range.flatMap { l -> range.map { r -> l to r } }.filterNot { (l, r) -> l == 0 && r == 0 }
    fun seesOccupied(iter: SeatIter): Int = iter.run {
        return Pos(rowIdx, seatIdx)
            .run { matrix.map { (bottom, right) -> find(bottom, right) } }
            .count { it }
    }

    fun nbOccupied() = rows.flatMap { it.seats }.count { it.isOccupied }
}

internal data class SeatRow(val seats: List<Seat>)

internal data class Seat(val input: Char) {

    companion object {
        val occupied = Seat('#')
        val empty = Seat('L')
    }

    val isEmpty get() = input == 'L'
    val isOccupied get() = input == '#'
    val isSeat get() = isEmpty || isOccupied
}