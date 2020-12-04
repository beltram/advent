import Field.Companion.toField
import Passport.Companion.toPassport

fun day4Part1(): Int {
    return input("day4")
        .split("\n\n")
        .map { it.toPassport() }
        .filter { it.isValidPart1() }
        .count()
}

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

    fun isValidPart1(): Boolean {
        return Key.values()
            .filter { it.isRequired }
            .all { k -> k in fields.map { it.key } }
    }
}

data class Field(val key: Key, val value: String) {
    companion object {
        fun String.toField(): Field {
            val (key, value) = split(':')
            return Field(Key.valueOf(key.toUpperCase()), value)
        }
    }
}

enum class Key(val isRequired: Boolean) {
    BYR(true),
    IYR(true),
    EYR(true),
    HGT(true),
    HCL(true),
    ECL(true),
    PID(true),
    CID(false);
}