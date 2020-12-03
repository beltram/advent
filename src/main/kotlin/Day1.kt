fun day1(): Int {
    val inputs: List<Int> = input("day1")
        .split("\n")
        .mapNotNull { it.trim().toIntOrNull() }
        .distinct()
    return inputs
        .map { it to inputs.firstOrNull { other -> it + other == 2020 } }
        .first { (_, b) -> b != null }
        .let { (a, b) -> a * b!! }
}
