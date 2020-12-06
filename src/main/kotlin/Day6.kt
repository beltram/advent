import Group1.Companion.toGroup1
import Group2.Companion.toGroup2

private fun day6Input() = input("day6").split("\n\n")

fun day6Part1() = day6Input().map { it.toGroup1().count() }.sum()
fun day6Part2() = day6Input().map { it.toGroup2().count() }.sum()

private class Group1(val input: String) {
    companion object {
        fun String.toGroup1() = Group1(replace("\n", ""))
    }

    fun count() = input.toCharArray().distinct().count()
}

private class Group2(val inputs: List<String>) {
    companion object {
        fun String.toGroup2() = Group2(split("\n"))
    }

    fun count(): Int {
        return inputs.joinToString("").toCharArray()
            .groupBy { it }.values
            .filter { it.size == inputs.size }
            .count()
    }
}