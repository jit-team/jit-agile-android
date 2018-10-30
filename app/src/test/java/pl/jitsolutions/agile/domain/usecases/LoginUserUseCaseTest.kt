package pl.jitsolutions.agile.domain.usecases

import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import kotlinx.coroutines.experimental.Dispatchers
import kotlinx.coroutines.experimental.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import pl.jitsolutions.agile.domain.Response
import pl.jitsolutions.agile.domain.User
import pl.jitsolutions.agile.domain.errorResponse
import pl.jitsolutions.agile.domain.response
import pl.jitsolutions.agile.repository.ProjectRepository
import pl.jitsolutions.agile.repository.UserRepository

class LoginUserUseCaseTest {

    @Test
    fun `login and get projects successful`() = runBlocking {
        val mockUserRepository = mock<UserRepository> {
            onBlocking { login("test@test.pl", "123") } doReturn response(User("abc", "email@email.com"))
        }
        val mockProjectRepository = mock<ProjectRepository> {
            onBlocking { getProjects("abc") } doReturn response(emptyList())
        }
        val params = LoginUserUseCase.Params("test@test.pl", "123")
        val useCase = LoginUserUseCase(mockUserRepository, mockProjectRepository, Dispatchers.Unconfined)

        val actualResponse = useCase.executeAsync(params).await()

        assertEquals(Response.Status.SUCCESS, actualResponse.status)
        assertEquals("abc, projects: []", actualResponse.data)
    }

    @Test
    fun `login server connection error`() = runBlocking {
        val mockUserRepository = mock<UserRepository> {
            onBlocking { login("test@test.pl", "123") } doReturn errorResponse(error = UserRepository.Error.ServerConnection)
        }
        val mockProjectRepository = mock<ProjectRepository> {}
        val params = LoginUserUseCase.Params("test@test.pl", "123")
        val useCase = LoginUserUseCase(mockUserRepository, mockProjectRepository, Dispatchers.Unconfined)

        val actualResponse = useCase.executeAsync(params).await()

        assertEquals(Response.Status.ERROR, actualResponse.status)
        assertEquals(LoginUserUseCase.Error.ServerConnection, actualResponse.error)
    }

    @Test
    fun `user not found error`() = runBlocking {
        val mockUserRepository = mock<UserRepository> {
            onBlocking { login("test@test.pl", "123") } doReturn errorResponse(error = UserRepository.Error.UserNotFound)
        }
        val mockProjectRepository = mock<ProjectRepository> {}
        val params = LoginUserUseCase.Params("test@test.pl", "123")
        val useCase = LoginUserUseCase(mockUserRepository, mockProjectRepository, Dispatchers.Unconfined)

        val actualResponse = useCase.executeAsync(params).await()

        assertEquals(Response.Status.ERROR, actualResponse.status)
        assertEquals(LoginUserUseCase.Error.UserEmailNotFound, actualResponse.error)
    }
}