package pl.jitsolutions.agile.repository

import pl.jitsolutions.agile.domain.Project
import pl.jitsolutions.agile.domain.ProjectWithDaily
import pl.jitsolutions.agile.domain.ProjectWithUsers
import pl.jitsolutions.agile.domain.Response

interface ProjectRepository {
    suspend fun getProjects(userId: String): Response<List<Project>>
    suspend fun getProject(projectId: String): Response<ProjectWithUsers>
    suspend fun getProjectsWithDailyState(userId: String): Response<List<ProjectWithDaily>>

    suspend fun joinProject(projectName: String, password: String): Response<String>
    suspend fun leaveProject(projectId: String): Response<Unit>
    suspend fun deleteProject(projectId: String): Response<Unit>

    suspend fun createNewProject(projectName: String, password: String): Response<String>
}