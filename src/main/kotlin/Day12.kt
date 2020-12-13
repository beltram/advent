import kotlin.math.abs

private fun shipActions() = input("day12").split("\n").map { Action(it) }

fun day12Part1(): Int = shipActions().fold(Ship()) { ship, move -> ship.move(move) }.manhattanDistance()
fun day12Part2(): Int = shipActions().fold(WayPoint()) { wayPoint, move -> wayPoint.move(move) }.manhattanDistance()

private data class WayPoint(val ship: Ship = Ship(), val x: Int = 10, val y: Int = 1) {

    fun move(action: Action) = action.run { move?.moveToward(action.value) ?: turn() ?: forward() }

    private fun Direction.moveToward(by: Int) = when (this) {
        Direction.N -> copy(y = y + by)
        Direction.S -> copy(y = y - by)
        Direction.E -> copy(x = x + by)
        Direction.W -> copy(x = x - by)
    }

    private fun Action.turn(): WayPoint? = toTurnRadian()?.let { (e, n) -> copy(x = e, y = n) }

    private fun Action.toTurnRadian(): Pair<Int, Int>? = turn?.let { turn ->
        when (turn) {
            Turn.R -> value.right()
            Turn.L -> value.let { 360 - it }.right()
        }
    }

    private fun Int.right() = when (this) {
        90 -> y to -x
        180 -> -x to -y
        270 -> -y to x
        else -> throw IllegalStateException("oups")
    }

    private fun Action.forward(): WayPoint {
        val nextX = ship.east + (x * value)
        val nextY = ship.north + (y * value)
        return copy(ship = Ship(east = nextX, north = nextY))
    }

    fun manhattanDistance() = ship.manhattanDistance()
}

private data class Ship(val orientation: Direction = Direction.E, val east: Int = 0, val north: Int = 0) {

    fun move(action: Action) = action.run { move?.moveToward(action.value) ?: turn() ?: forward() }

    private fun Direction.moveToward(by: Int) = when (this) {
        Direction.N -> copy(north = north + by)
        Direction.S -> copy(north = north - by)
        Direction.E -> copy(east = east + by)
        Direction.W -> copy(east = east - by)
    }

    private fun Action.turn() = turn?.let { copy(orientation = orientation.turn(it, value)) }

    private fun Action.forward() = orientation.moveToward(value)

    fun manhattanDistance() = abs(east) + abs(north)
}

private class Action(val input: String) {
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