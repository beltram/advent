private fun waitingArea(): WaitingArea {
    return input("day11")
        .split("\n")
        .map { it.map { s -> Seat(s) } }
        .map { SeatRow(it) }
        .let { WaitingArea(it) }
}

fun day11() = waitingArea().stabilized().nbOccupied()

private tailrec fun WaitingArea.stabilized(): WaitingArea {
    return evolve().let { next ->
        if (this != next) next.stabilized()
        else this
    }
}

private data class WaitingArea(val rows: List<SeatRow>) {
    fun evolve(): WaitingArea {
        return rows
            .mapIndexed { rowIdx, row -> row.seats.mapIndexed { seatIdx, seat -> SeatIter(seat, seatIdx, rowIdx) } }
            .map { row -> row.map { iter -> adjacents(iter.rowIdx, iter.seatIdx).let { iter.rule1(it).rule2(it) } } }
            .map { seats -> SeatRow(seats.map { it.seat }) }
            .let { WaitingArea(it) }
    }

    private data class SeatIter(val seat: Seat, val seatIdx: Int, val rowIdx: Int) {

        fun rule1(adjacents: List<Seat>): SeatIter {
            return takeUnless { seat.isEmpty && adjacents.none { it.isOccupied } }
                ?: SeatIter(Seat.occupied, seatIdx, rowIdx)
        }

        fun rule2(adjacents: List<Seat>): SeatIter {
            return takeUnless { seat.isOccupied && adjacents.count { it.isOccupied } >= 4 }
                ?: SeatIter(Seat.empty, seatIdx, rowIdx)
        }
    }

    private fun adjacents(rowIdx: Int, seatIdx: Int): List<Seat> {
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
    }

    fun nbOccupied() = rows.flatMap { it.seats }.count { it.isOccupied }

    override fun toString() = rows.joinToString("\n")
}

private data class SeatRow(val seats: List<Seat>) {
    override fun toString() = seats.joinToString("")
}

private data class Seat(val input: Char) {

    companion object {
        val occupied = Seat('#')
        val empty = Seat('L')
    }

    val isEmpty get() = input == 'L'
    val isOccupied get() = input == '#'
    override fun toString() = input.toString()
}