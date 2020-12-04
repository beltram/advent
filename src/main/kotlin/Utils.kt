private class Utils

fun input(name: String) = Utils::class.java.getResource("$name.txt").readText()