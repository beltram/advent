private fun instructions() = input("day8").split("\n").map { Instruction(it) }

fun day8Part1() = instructions().traverse()

fun day8Part2(): Int {
    val instructions = instructions()
    return instructions.indexesUntilLoop()
        .filter { instructions[it].isJmpOrNop() }
        .map { instructions.mapIndexed { index, ins -> if (index == it) ins.toggle() else ins } }
        .first { it.finishes() }
        .traverse()
}

private tailrec fun List<Instruction>.traverse(index: Int = 0, acc: Int = 0, visited: Set<Int> = mutableSetOf()): Int {
    return if (isFinished(index)) acc else {
        val current = this[index]
        val next = current.next(index)
        if (next !in visited) traverse(next, current + acc, visited + index)
        else acc
    }
}

private tailrec fun List<Instruction>.finishes(index: Int = 0, visited: Set<Int> = mutableSetOf()): Boolean {
    return isFinished(index) || this[index].next(index)
        .takeUnless { it in visited }
        ?.let { finishes(it, visited + index) } ?: false
}

private tailrec fun List<Instruction>.indexesUntilLoop(index: Int = 0, visited: Set<Int> = mutableSetOf()): Set<Int> {
    return if (isFinished(index)) visited else {
        val next = this[index].next(index)
        if (next !in visited) indexesUntilLoop(next, visited + index)
        else visited
    }
}

private fun Collection<Any>.isFinished(index: Int) = index >= size || index < 0

private data class Instruction(val input: String) {
    val parts get() = input.split(' ')
    val value get() = parts.last().toInt()
    val action get() = parts.first()
    fun isJmpOrNop() = action == "nop" || action == "jmp"
    fun toggle() = Instruction("${toggleAction()} $value")
    private fun toggleAction() = if (action == "jmp") "nop" else "jmp"
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