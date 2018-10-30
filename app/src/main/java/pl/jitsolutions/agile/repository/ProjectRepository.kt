package pl.jitsolutions.agile.repository

import pl.jitsolutions.agile.domain.Project
import pl.jitsolutions.agile.domain.Response

interface ProjectRepository {
    suspend fun getProjects(userId: String): Response<List<Project>>
    suspend fun getProject(projectId: String): Response<Project>

    sealed class Error(message: String? = null) : Throwable(message) {
        object UserNotFound : Error()
        object ServerConnection : Error()
        class ProjectNotFound(val projectId: String) : Error("Project with id: $projectId not found!")
    }
}