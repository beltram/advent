fun day2Part2(): Int {
    return input("day2")
        .split("\n")
        .filterNot { it.isBlank() }
        .map { it.trim().toPwd2() }
        .filter { it.isValid() }
        .count()
}

data class Pwd2(val posA: Int, val posB: Int, val letter: Char, val password: String) {
    fun isValid() = satisfies(posA) xor satisfies(posB)
    private fun satisfies(index: Int) = password.elementAtOrNull(index - 1)?.let { it == letter } ?: false
}

private fun String.toPwd2(): Pwd2 {
    val (policy, password) = split(':')
    val (length, letter) = policy.split(' ')
    val (posA, posB) = length.split('-')
    return Pwd2(posA.toInt(), posB.toInt(), letter.toCharArray().first(), password.trim())
}
