private fun rules() = input("day7").split("\n").map { Rule(it) }

fun day7Part1() = rules().findHolders("shiny", "gold").distinctBy { it.pattern to it.color }.count()

private tailrec fun List<Rule>.findHolders(pattern: String, color: String): List<Bag> {
    val candidateBags = filter { it.contains(pattern, color) }.map { it.holder }
    return candidateBags + candidateBags.flatMap { findHolders(it.pattern!!, it.color!!) }
}

fun day7Part2() = rules().findBags("shiny", "gold") - 1

private tailrec fun List<Rule>.findBags(pattern: String, color: String): Int {
    val candidateBags = filter { it.doesHolderMatches(pattern, color) }.map { it.bags }
    return candidateBags.size + candidateBags
        .map { it.filter { !it.isEmpty }.map { it.quantity * findBags(it.pattern!!, it.color!!) } }
        .sumBy { it.sum() }
}

private class Rule(val input: String) {
    val parts get() = input.split("contain")
    val holder: Bag get() = Bag(parts.first())
    val bags get() = parts.last().split(',').map { Bag(it) }
    fun contains(pattern: String, color: String) = bags.any { it.pattern == pattern && it.color == color }
    fun doesHolderMatches(pattern: String, color: String) = holder.pattern == pattern && holder.color == color
}

class Bag(val input: String) {
    private val cleaned get() = input.trim().substringBefore("bag").substringBefore("bags").trim()
    val quantity get() = cleaned.split(' ').firstOrNull()?.toIntOrNull() ?: 0
    val isEmpty get() = cleaned == "no other"
    private val kinds get() = cleaned.takeUnless { isEmpty }?.split(' ')?.run { takeIf { quantity == 0 } ?: drop(1) }
    val pattern get() = kinds?.first()
    val color get() = kinds?.last()
}