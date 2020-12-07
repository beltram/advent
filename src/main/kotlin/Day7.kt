private fun rules() = input("day7").split("\n").map { Rule(it) }

fun day7() = rules().findHolders("shiny", "gold").distinctBy { it.pattern to it.color }.count()

private tailrec fun List<Rule>.findHolders(pattern: String, color: String): List<Bag> {
    val candidateBags = filter { it.contains(pattern, color) }.map { it.holder }
    return candidateBags + candidateBags.flatMap { findHolders(it.pattern!!, it.color!!) }
}

private class Rule(val input: String) {
    val parts get() = input.split("contain")
    val holder: Bag get() = Bag(parts.first())
    val bags get() = parts.last().split(',').map { Bag(it) }
    fun contains(pattern: String, color: String) = bags.any { it.pattern == pattern && it.color == color }
}

private class Bag(val input: String) {
    val cleaned get() = input.trim().substringBefore("bag").substringBefore("bags").trim()
    val quantity get() = cleaned.split(' ').firstOrNull()?.toIntOrNull() ?: 0
    val isEmpty get() = cleaned == "no other"
    val kinds get() = cleaned.takeUnless { isEmpty }?.split(' ')?.run { takeIf { quantity == 0 } ?: drop(1) }
    val pattern get() = kinds?.first()
    val color get() = kinds?.last()
}