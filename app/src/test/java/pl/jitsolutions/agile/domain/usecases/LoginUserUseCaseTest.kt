package pl.jitsolutions.agile.domain.usecases

import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import kotlinx.coroutines.experimental.Dispatchers
import kotlinx.coroutines.experimental.runBlocking
import org.junit.Test
import pl.jitsolutions.agile.assertThat
import pl.jitsolutions.agile.domain.User
import pl.jitsolutions.agile.domain.errorResponse
import pl.jitsolutions.agile.domain.response
import pl.jitsolutions.agile.repository.ProjectRepository
import pl.jitsolutions.agile.repository.UserRepository

class LoginUserUseCaseTest {

    @Test
    fun `login and get projects successful`() = runBlocking {
        val mockUserRepository = mock<UserRepository> {
            onBlocking {
                login("test@test.pl", "123")
            } doReturn
                response(User("abc", "email@email.com"))
        }
        val mockProjectRepository = mock<ProjectRepository> {
            onBlocking {
                getProjects("abc")
            } doReturn
                response("Test group")
        }
        val params = LoginUserUseCase.Params("test@test.pl", "123")
        val useCase =
            LoginUserUseCase(mockUserRepository, mockProjectRepository, Dispatchers.Unconfined)

        val response = useCase.executeAsync(params).await()

        assertThat(response) {
            isSuccessful()
            hasString("abc, projects: Test group")
        }
    }

    @Test
    fun `login server connection error`() = runBlocking {
        val mockUserRepository = mock<UserRepository> {
            onBlocking {
                login("test@test.pl", "123")
            } doReturn
                errorResponse(error = UserRepository.Error.ServerConnection)
        }
        val mockProjectRepository = mock<ProjectRepository>()

        val params = LoginUserUseCase.Params("test@test.pl", "123")
        val useCase =
            LoginUserUseCase(mockUserRepository, mockProjectRepository, Dispatchers.Unconfined)

        val response = useCase.executeAsync(params).await()

        assertThat(response) {
            hasError(LoginUserUseCase.Error.ServerConnection)
        }
    }

    @Test
    fun `user not found error`() = runBlocking {
        val mockUserRepository = mock<UserRepository> {
            onBlocking {
                login("test@test.pl", "123")
            } doReturn
                errorResponse(error = UserRepository.Error.UserNotFound)
        }
        val mockProjectRepository = mock<ProjectRepository>()

        val params = LoginUserUseCase.Params("test@test.pl", "123")
        val useCase =
            LoginUserUseCase(mockUserRepository, mockProjectRepository, Dispatchers.Unconfined)

        val response = useCase.executeAsync(params).await()

        assertThat(response) {
            hasError(LoginUserUseCase.Error.UserEmailNotFound)
        }
    }
}