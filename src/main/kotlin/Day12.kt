import kotlin.math.abs

fun day12(): Int {
    return input("day12")
        .split("\n")
        .map { ShipAction(it) }
        .fold(Ship()) { ship, move -> ship.move(move) }
        .manhattanDistance()
}

private data class Ship(val orientation: Direction = Direction.E, val east: Int = 0, val north: Int = 0) {

    fun move(action: ShipAction) = action.run { move?.moveToward(action.value) ?: turn() ?: forward() }

    private fun Direction.moveToward(by: Int) = when (this) {
        Direction.N -> copy(north = north + by)
        Direction.S -> copy(north = north - by)
        Direction.E -> copy(east = east + by)
        Direction.W -> copy(east = east - by)
    }

    private fun ShipAction.turn() = turn?.let { copy(orientation = orientation.turn(it, value)) }

    private fun ShipAction.forward() = orientation.moveToward(value)

    fun manhattanDistance() = abs(east) + abs(north)
}

private class ShipAction(val input: String) {
    val direction get() = input.first()
    val value get() = input.drop(1).toInt()
    val move get() = direction.runCatching { Direction.valueOf(toString()) }.getOrNull()
    val turn get() = direction.runCatching { Turn.valueOf(toString()) }.getOrNull()
}

internal enum class Direction {
    N, E, S, W;

    fun turn(toward: Turn, degree: Int): Direction {
        val times = degree / 90
        return when (toward) {
            Turn.L -> left(times)
            Turn.R -> right(times)
        }
    }

    private val size get() = values().size
    private fun right(times: Int) = values()[(ordinal + times) % size]
    private fun left(times: Int) = values()[(size + ordinal - times) % size]
}

internal enum class Turn { L, R }
