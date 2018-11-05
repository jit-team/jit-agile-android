package pl.jitsolutions.agile.domain.usecases

import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import kotlinx.coroutines.experimental.Dispatchers
import kotlinx.coroutines.experimental.runBlocking
import org.junit.Test
import pl.jitsolutions.agile.domain.errorResponse
import pl.jitsolutions.agile.repository.ProjectRepository

class CreateNewProjectUseCaseTest {

    @Test
    fun `empty project name`() = runBlocking(Dispatchers.Default) {
        val projectRepository = mock<ProjectRepository>()
        val params = CreateNewProjectUseCase.Params("", "")
        val useCase = CreateNewProjectUseCase(projectRepository, Dispatchers.Default)

        val response = useCase.executeAsync(params).await()

        pl.jitsolutions.agile.assertThat(response) {
            hasError(CreateNewProjectUseCase.Error.EmptyProjectName)
        }
    }

    @Test
    fun `project already exist`() = runBlocking(Dispatchers.Default) {
        val userRepository = mock<ProjectRepository> {
            onBlocking {
                createNewProject("test", "password")
            } doReturn
                errorResponse(error = ProjectRepository.Error.ProjectAlreadyExist)
        }
        val params = CreateNewProjectUseCase.Params("test", "password")
        val useCase = CreateNewProjectUseCase(userRepository, Dispatchers.Default)

        val response = useCase.executeAsync(params).await()

        pl.jitsolutions.agile.assertThat(response) {
            hasError(CreateNewProjectUseCase.Error.ProjectAlreadyExist)
        }
    }
}