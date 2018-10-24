package pl.jitsolutions.agile.domain

data class Response<T>(
        val data: T?,
        val status: Status,
        val error: Throwable?
) {
    enum class Status { SUCCESS, ERROR }
}

inline fun <reified T> response(data: T): Response<T> {
    return Response(data = data, status = Response.Status.SUCCESS, error = null)
}

inline fun <reified T> errorResponse(data: T? = null, error: Throwable): Response<T> {
    return Response<T>(data = data, status = Response.Status.ERROR, error = error)
}