fun day13(): Long? {
    return input("day13")
        .split("\n")
        .run { Schedule(first().toLong(), get(1).split(',').map { Bus(it) }) }
        .result
}

private data class Schedule(val depart: Long, val busses: List<Bus>) {

    private val earliest
        get() = busses
            .map { it to it.next(depart) }
            .filterNot { (_, time) -> time == null }
            .minByOrNull { (_, time) -> time!! }

    val result get() = earliest?.let { (bus, time) -> time?.minus(depart)?.let { wait -> bus.value?.times(wait) } }
}

private data class Bus(val id: String) {
    val value get() = id.toIntOrNull()
    fun quotient(by: Long) = value?.let { by.div(it) }
    fun next(by: Long) = value?.let { v -> quotient(by)?.plus(1)?.times(v) }
}