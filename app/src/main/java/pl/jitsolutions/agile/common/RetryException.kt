package pl.jitsolutions.agile.common

import kotlinx.coroutines.delay

class RetryException : Exception()

suspend fun <T> retry(times: Int = 3, function: suspend () -> T): T {
    repeat(times - 1) {
        try {
            return function()
        } catch (e: RetryException) {
            delay((2000 until 5000).random().toLong())
        }
    }
    return function()
}