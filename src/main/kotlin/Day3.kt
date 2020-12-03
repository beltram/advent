import Area.SQUARE
import Area.TREE

fun day3(): Int {
    return input("day3")
        .split("\n")
        .filterNot { it.isBlank() }
        .map { it.toRow() }
        .let { Map(it).moving(Move(right = 3, down = 1)) }
        .filter { it == TREE }
        .count()
}

class Move(val right: Int, val down: Int)
class Map(val rows: List<Row>) {

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
