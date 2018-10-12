package pl.jitsolutions.agile.domain

data class Response<T>(
        val data: T?,
        val status: Status,
        val error: Any?
) {
    enum class Status { SUCCESS, ERROR, IN_PROGRESS }
}

inline fun <reified T> response(data: T): Response<T> {
    return Response(data = data, status = Response.Status.SUCCESS, error = null)
}

inline fun <reified T> inProgressResponse(data: T? = null): Response<T> {
    return Response<T>(data = data, status = Response.Status.IN_PROGRESS, error = null)
}

inline fun <reified T> errorResponse(data: T? = null, error: Any): Response<T> {
    return Response<T>(data = data, status = Response.Status.ERROR, error = error)
}