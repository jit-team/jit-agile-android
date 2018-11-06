package pl.jitsolutions.agile.repository

import pl.jitsolutions.agile.domain.Project
import pl.jitsolutions.agile.domain.Response
import pl.jitsolutions.agile.domain.User

interface ProjectRepository {
    suspend fun getProjects(userId: String): Response<List<Project>>
    suspend fun getProject(projectId: String): Response<Project>
    suspend fun getUsersAssignedToProject(projectId: String): Response<List<User>>
    suspend fun leaveProject(projectId: String): Response<Unit>
    suspend fun createNewProject(projectName: String, password: String): Response<String>
    suspend fun deleteProject(projectId: String): Response<Unit>

    sealed class Error(message: String? = null) : Throwable(message) {
        object UserNotFound : Error()
        object ServerConnection : Error()
        class ProjectNotFound(val projectId: String) :
            Error("Project with id: $projectId not found!")
        object ProjectAlreadyExist : Error()
    }
}