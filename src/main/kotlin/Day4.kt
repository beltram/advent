import Field.Companion.toField
import Passport.Companion.toPassport

fun day4Part1() = allPassports().filter { it.isValidPart1() }.count()

fun day4Part2() = allPassports().filter { it.isValidPart2() }.count()

private fun allPassports() = input("day4").split("\n\n").map { it.toPassport() }

data class Passport(val fields: List<Field>) {
    companion object {
        fun String.toPassport(): Passport {
            return split("\n")
                .joinToString(" ")
                .split(' ')
                .map { it.toField() }
                .distinctBy { it.key }
                .let { Passport(it) }
        }
    }

    private val keys = fields.map { it.key }
    private val requiredKeys = Key.values().filter { it.isRequired }

    fun isValidPart1() = requiredKeys.all { k -> k in keys }

    fun isValidPart2(): Boolean {
        return isValidPart1() && fields.all { it.isValid() }
    }
}

data class Field(val key: Key, val value: String) {
    companion object {
        fun String.toField(): Field {
            val (key, value) = split(':')
            return Field(Key.valueOf(key.toUpperCase()), value)
        }
    }

    fun isValid() = key.validator(value)
}

enum class Key(val isRequired: Boolean, val validator: (String) -> Boolean = { true }) {
    BYR(true, { s -> s.toIntOrNull()?.let { it in 1920..2002 } ?: false }),
    IYR(true, { s -> s.toIntOrNull()?.let { it in 2010..2020 } ?: false }),
    EYR(true, { s -> s.toIntOrNull()?.let { it in 2020..2030 } ?: false }),
    HGT(true, { Height(it).isValid() }),
    HCL(true, { hairColorRegex.matches(it) }),
    ECL(true, { it in eyeColors }),
    PID(true, { passportIdRegex.matches(it) }),
    CID(false);

    companion object {
        val hairColorRegex = Regex("#[0-9a-f]{6}")
        val eyeColors = setOf("amb", "blu", "brn", "gry", "grn", "hzl", "oth")
        val passportIdRegex = Regex("[0-9]{9}")
    }

    private class Height(val input: String) {
        val unit: HeightUnit? by lazy {
            when {
                input.endsWith(HeightUnit.CM.label) -> HeightUnit.CM
                input.endsWith(HeightUnit.IN.label) -> HeightUnit.IN
                else -> null
            }
        }

        val value: Int? by lazy {
            when (unit) {
                HeightUnit.CM -> input.substringBefore(HeightUnit.CM.label)
                HeightUnit.IN -> input.substringBefore(HeightUnit.IN.label)
                else -> null
            }?.toIntOrNull()
        }

        fun isValid(): Boolean {
            return when (unit) {
                HeightUnit.CM -> value?.let { it in 150..193 } ?: false
                HeightUnit.IN -> value?.let { it in 59..76 } ?: false
                else -> false
            }
        }

        enum class HeightUnit {
            CM, IN;

            val label get() = name.toLowerCase()
        }
    }
}