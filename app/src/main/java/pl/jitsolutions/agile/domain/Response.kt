package pl.jitsolutions.agile.domain

import pl.jitsolutions.agile.Error

data class Response<T>(
    val data: T?,
    val status: Status,
    val error: Error?
) {
    enum class Status { SUCCESS, FAILURE }
}

inline fun <reified T> response(data: T): Response<T> {
    return Response(data = data, status = Response.Status.SUCCESS, error = null)
}

inline fun <reified T> errorResponse(data: T? = null, error: Error): Response<T> {
    return Response(data = data, status = Response.Status.FAILURE, error = error)
}