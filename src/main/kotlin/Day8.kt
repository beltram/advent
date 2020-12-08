private fun instructions() = input("day8").split("\n").map { Instruction(it) }

fun day8() = instructions().traverse()

private tailrec fun List<Instruction>.traverse(index: Int = 0, acc: Int = 0, visited: Set<Int> = mutableSetOf()): Int {
    return if (index >= size || index < 0) acc else {
        val current = this[index]
        val next = current.next(index)
        if (next !in visited) traverse(next, current + acc, visited + index)
        else acc
    }
}

private data class Instruction(val input: String) {
    val parts get() = input.split(' ')
    val value get() = parts.last().toInt()
    val action get() = parts.first()
    fun next(index: Int): Int = when (action) {
        "acc", "nop" -> index + 1
        "jmp" -> index + value
        else -> oups()
    }

    operator fun plus(acc: Int): Int = when (action) {
        "acc" -> acc + value
        "jmp", "nop" -> acc
        else -> oups()
    }

    private fun oups(): Nothing = throw IllegalStateException("oups")
}