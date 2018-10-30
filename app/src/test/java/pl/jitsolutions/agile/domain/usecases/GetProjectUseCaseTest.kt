package pl.jitsolutions.agile.domain.usecases

import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import kotlinx.coroutines.experimental.Dispatchers
import kotlinx.coroutines.experimental.runBlocking
import org.junit.Test
import pl.jitsolutions.agile.assertThat
import pl.jitsolutions.agile.domain.Project
import pl.jitsolutions.agile.domain.errorResponse
import pl.jitsolutions.agile.domain.response
import pl.jitsolutions.agile.hasProject
import pl.jitsolutions.agile.repository.ProjectRepository

class GetProjectUseCaseTest {
    @Test
    fun `get project successful`() = runBlocking {
        val mockProjectRepository = mock<ProjectRepository> {
            onBlocking {
                getProject("projectId")
            } doReturn
                response(Project(name = "Project"))
        }

        val params = GetProjectUseCase.Params("projectId")
        val useCase = GetProjectUseCase(mockProjectRepository, Dispatchers.Unconfined)

        val response = useCase.executeAsync(params).await()

        assertThat(response) {
            isSuccessful()
            hasProject {
                withName("Project")
            }
        }
    }

    @Test
    fun `project not found`() = runBlocking {
        val mockProjectRepository = mock<ProjectRepository> {
            onBlocking {
                getProject("projectId")
            } doReturn
                errorResponse(error = ProjectRepository.Error.ProjectNotFound("projectId"))
        }

        val params = GetProjectUseCase.Params("projectId")
        val useCase = GetProjectUseCase(mockProjectRepository, Dispatchers.Unconfined)

        val response = useCase.executeAsync(params).await()

        assertThat(response) {
            hasError(GetProjectUseCase.Error.ProjectNotFound("projectId"))
        }
    }

    @Test
    fun `server connection error`() = runBlocking {
        val mockProjectRepository = mock<ProjectRepository> {
            onBlocking {
                getProject("projectId")
            } doReturn
                errorResponse(error = ProjectRepository.Error.ServerConnection)
        }

        val params = GetProjectUseCase.Params("projectId")
        val useCase = GetProjectUseCase(mockProjectRepository, Dispatchers.Unconfined)

        val response = useCase.executeAsync(params).await()

        assertThat(response) {
            hasError(GetProjectUseCase.Error.ServerConnection)
        }
    }

    @Test(expected = Throwable::class)
    fun `throw exception if unknown error`() = runBlocking {
        val mockProjectRepository = mock<ProjectRepository> {
            onBlocking {
                getProject("projectId")
            } doReturn
                errorResponse(error = RuntimeException())
        }

        val params = GetProjectUseCase.Params("projectId")
        val useCase = GetProjectUseCase(mockProjectRepository, Dispatchers.Unconfined)

        val response = useCase.executeAsync(params).await()
    }
}