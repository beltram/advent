fun day2Part1(): Int {
    return input("day2-part1")
        .split("\n")
        .filterNot { it.isBlank() }
        .map { it.trim().toPwd() }
        .filter { it.isValid() }
        .count()
}

data class Pwd(val atLeast: Int, val atMost: Int, val letter: Char, val password: String) {
    fun isValid() = satisfiesAtLeast() && satisfiesAtMost()
    private fun satisfiesAtLeast() = password.toCharArray().count { it == letter } >= atLeast
    private fun satisfiesAtMost() = password.toCharArray().count { it == letter } <= atMost
}

private fun String.toPwd(): Pwd {
    val (policy, password) = split(':')
    val (length, letter) = policy.split(' ')
    val (atLeast, atMost) = length.split('-')
    return Pwd(atLeast.toInt(), atMost.toInt(), letter.toCharArray().first(), password.trim())
}