package pl.jitsolutions.agile.domain.usecases

import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import kotlinx.coroutines.experimental.Dispatchers
import kotlinx.coroutines.experimental.runBlocking
import org.junit.Test
import pl.jitsolutions.agile.assertThat
import pl.jitsolutions.agile.domain.errorResponse
import pl.jitsolutions.agile.repository.ProjectRepository

class ProjectCreationUseCaseTest {

    @Test
    fun `empty project name`() = runBlocking(Dispatchers.Default) {
        val projectRepository = mock<ProjectRepository>()
        val params = ProjectCreationUseCase.Params("", "")
        val useCase = ProjectCreationUseCase(projectRepository, Dispatchers.Default)

        val response = useCase.executeAsync(params).await()

        assertThat(response) {
            hasError(ProjectCreationUseCase.Error.EmptyProjectName)
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
        val params = ProjectCreationUseCase.Params("test", "password")
        val useCase = ProjectCreationUseCase(userRepository, Dispatchers.Default)

        val response = useCase.executeAsync(params).await()

        assertThat(response) {
            hasError(ProjectCreationUseCase.Error.ProjectAlreadyExist)
        }
    }
}