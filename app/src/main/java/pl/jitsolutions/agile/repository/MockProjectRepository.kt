package pl.jitsolutions.agile.repository

import kotlinx.coroutines.experimental.CoroutineDispatcher
import pl.jitsolutions.agile.domain.Project
import pl.jitsolutions.agile.domain.Response
import pl.jitsolutions.agile.domain.User
import pl.jitsolutions.agile.domain.response

class MockProjectRepository(private val dispatcher: CoroutineDispatcher) : ProjectRepository {
    override suspend fun getProject(projectId: String): Response<Project> {
        return response(Project(name = "Example project", users = listOf(
                User(name = "Test user 1", email = "email1@email.com"),
                User(name = "Test user 2", email = "email1@email.com"),
                User(name = "Test user 3", email = "email1@email.com"),
                User(name = "Test user 4", email = "email1@email.com"),
                User(name = "Test user 5", email = "email1@email.com"),
                User(name = "Test user 6", email = "email1@email.com"),
                User(name = "Test user 7", email = "email1@email.com"),
                User(name = "Test user 8", email = "email1@email.com")
        )))
    }

    override suspend fun getProjects(userId: String): Response<String> {
        return response("test groups")
    }
}