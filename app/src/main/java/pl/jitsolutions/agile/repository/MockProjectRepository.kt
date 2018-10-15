package pl.jitsolutions.agile.repository

import kotlinx.coroutines.experimental.CoroutineDispatcher
import pl.jitsolutions.agile.domain.Response
import pl.jitsolutions.agile.domain.response

class MockProjectRepository(private val dispatcher: CoroutineDispatcher) : ProjectRepository {
    override suspend fun getProjects(userId: String): Response<String> {
        return response("test groups")
    }
}