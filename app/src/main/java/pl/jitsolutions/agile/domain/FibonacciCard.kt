package pl.jitsolutions.agile.domain

fun getCards() = fibonacci().map { FibonacciCard(it.toString()) }

private fun fibonacci(): List<Int> =
    generateSequence(Pair(0, 1)) {
        Pair(
            it.second,
            it.first + it.second
        )
    }.map { it.first }.take(9).toList().distinct()

data class FibonacciCard(val value: String)