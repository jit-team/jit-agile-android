package pl.jitsolutions.agile.repository

import pl.jitsolutions.agile.domain.Response

interface ProjectRepository {
    suspend fun getProjects(userId: String): Response<String>

    sealed class Error {
        object UserNotFound : Error()
        object ServerConnection : Error()
    }
}