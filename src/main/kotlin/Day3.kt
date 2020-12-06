import Area.SQUARE
import Area.TREE

fun day3Part1(): Int {
    return getMap().moving(Move(right = 3, down = 1))
        .filter { it == TREE }
        .count()
}

fun day3Part2(): Int {
    val moves = setOf(1 to 1, 3 to 1, 5 to 1, 7 to 1, 1 to 2).map { (r, d) -> Move(r, d) }
    return getMap()
        .let { map -> moves.map { map.moving(it) } }
        .map { it.filter { it == TREE }.count() }
        .fold(1) { acc, i -> acc * i }
}

private fun getMap(): Map {
    return input("day3")
        .split("\n")
        .filterNot { it.isBlank() }
        .map { it.toRow() }
        .let { Map(it) }
}

class Move(val right: Int, val down: Int)
private class Map(val rows: List<Row>) {

    private val maxAreaIndex by lazy { rows.last().areas.size }

    tailrec fun moving(
        move: Move,
        rowIndex: Int = 0,
        areaIndex: Int = 0,
        encounters: MutableList<Area> = mutableListOf()
    ): List<Area> {
        val moveDown = rowIndex + move.down
        val moveRight = (areaIndex + move.right) % maxAreaIndex
        return if (moveDown >= rows.size) encounters else {
            val currentRow = rows[moveDown]
            val destination = currentRow.areas[moveRight]
            moving(move, moveDown, moveRight, encounters.apply { add(destination) })
        }
    }
}

class Row(val areas: List<Area>)

fun String.toRow() = Row(toCharArray().map { it.toArea() })

enum class Area { SQUARE, TREE }

fun Char.toArea() = when (this) {
    '.' -> SQUARE
    '#' -> TREE
    else -> throw IllegalStateException("oups")
}
