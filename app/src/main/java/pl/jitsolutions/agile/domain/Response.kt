package pl.jitsolutions.agile.domain

import pl.jitsolutions.agile.JitError

data class Response<T>(
    val data: T?,
    val status: Status,
    val error: Throwable?,
    val newError: JitError?
) {
    enum class Status { SUCCESS, FAILURE }
}

inline fun <reified T> response(data: T): Response<T> {
    return Response(data = data, status = Response.Status.SUCCESS, error = null, newError = null)
}

inline fun <reified T> errorResponse(data: T? = null, error: Throwable): Response<T> {
    return Response(data = data, status = Response.Status.FAILURE, error = error, newError = null)
}

inline fun <reified T> newErrorResponse(data: T? = null, error: JitError): Response<T> {
    return Response(data = data, status = Response.Status.FAILURE, error = null, newError = error)
}