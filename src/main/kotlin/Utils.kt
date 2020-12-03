private class Utils

fun input(name: String): String {
    return Utils::class.java.getResource("$name.txt").readText()
}