package pl.jitsolutions.agile.repository

import kotlinx.coroutines.experimental.CoroutineDispatcher
import pl.jitsolutions.agile.domain.Project
import pl.jitsolutions.agile.domain.Response
import pl.jitsolutions.agile.domain.response

class MockProjectRepository(private val dispatcher: CoroutineDispatcher) : ProjectRepository {
    override suspend fun getProject(projectId: String): Response<Project> {
        return response(
            Project(
                name = "Example project"
            )
        )
    }

    override suspend fun getProjects(userId: String): Response<List<Project>> {
        return response(listOf())
    }
}