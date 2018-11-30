package pl.jitsolutions.agile.domain

import pl.jitsolutions.agile.common.Error

sealed class Response<T>
data class Success<T>(val data: T) : Response<T>()
data class Failure<T>(val error: Error) : Response<T>()