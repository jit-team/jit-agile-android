package pl.jitsolutions.agile.repository

import pl.jitsolutions.agile.domain.Project
import pl.jitsolutions.agile.domain.ProjectWithDaily
import pl.jitsolutions.agile.domain.Response
import pl.jitsolutions.agile.domain.User

interface ProjectRepository {
    suspend fun getProjects(userId: String): Response<List<Project>>
    suspend fun getProject(projectId: String): Response<Pair<Project, List<User>>>
    suspend fun leaveProject(projectId: String): Response<Unit>
    suspend fun createNewProject(projectName: String, password: String): Response<Unit>
    suspend fun deleteProject(projectId: String): Response<Unit>
    suspend fun joinProject(projectName: String, password: String): Response<Unit>
    suspend fun getProjectsWithDailyState(userId: String): Response<List<ProjectWithDaily>>

    sealed class Error(message: String? = null) : Throwable(message) {
        object UserNotFound : Error()
        object ServerConnection : Error()
        class ProjectNotFound(val projectId: String) :
            Error("Project with id: $projectId not found!")
        object ProjectAlreadyExist : Error()
        object UnknownError : Error()
        object InvalidPassword : Error()
    }
}