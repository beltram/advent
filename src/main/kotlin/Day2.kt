fun day2(): Int {
    return input("day2")
        .split("\n")
        .filterNot { it.isBlank() }
        .map { it.trim().toPwd() }
        .filter { it.isValid() }
        .count()
}

private fun String.toPwd(): Pwd {
    val (policy, password) = split(':')
    val (length, letter) = policy.split(' ')
    val (atLeast, atMost) = length.split('-')
    return Pwd(
        atLeast.toInt(),
        atMost.toInt(),
        letter.toCharArray().first(),
        password.trim(),
    )
}

data class Pwd(val atLeast: Int, val atMost: Int, val letter: Char, val password: String) {
    fun isValid() = satisifesAtLeast() && satisifesAtMost()
    private fun satisifesAtLeast() = password.toCharArray().count { it == letter } >= atLeast
    private fun satisifesAtMost() = password.toCharArray().count { it == letter } <= atMost

    override fun toString() = password
}

