import Group.Companion.toGroup

fun day6Part1(): Int {
    return input("day6")
        .split("\n\n")
        .map { it.toGroup().count() }
        .sum()
}

private class Group(val input: String) {
    companion object {
        fun String.toGroup() = Group(replace("\n", ""))
    }

    fun count() = input.toCharArray().distinct().count()
}