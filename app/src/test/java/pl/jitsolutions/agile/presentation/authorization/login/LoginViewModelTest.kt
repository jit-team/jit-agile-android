package pl.jitsolutions.agile.presentation.authorization.login

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
import pl.jitsolutions.agile.domain.usecases.LoginUserUseCase
import pl.jitsolutions.agile.repository.ProjectRepository
import pl.jitsolutions.agile.repository.UserRepository

class LoginViewModelTest {

    @Test
    fun testLoginAndGetProjectSuccessful() = runBlocking<Unit>(Dispatchers.Default) {
        val mockUserRepository = mock<UserRepository> {
            onBlocking { login("test@test.pl", "123") } doReturn response(User("abc"))
        }

        val mockProjectRepository = mock<ProjectRepository> {
            onBlocking { getProjects("abc") } doReturn response("Test group")
        }

        val classUnderTest = LoginUserUseCase(mockUserRepository, mockProjectRepository, Dispatchers.Unconfined)
        val params = LoginUserUseCase.Params("test@test.pl", "123")
        val response = classUnderTest.executeAsync(params).await()

        assertEquals(Response.Status.SUCCESS, response.status)
        assertEquals("abc, projects: Test group", response.data)
    }

    @Test
    fun testLoginServerConnectionException() = runBlocking(Dispatchers.Default) {
        val mockUserRepository = mock<UserRepository> {
            onBlocking { login("test@test.pl", "123") } doReturn errorResponse(error = UserRepository.Error.ServerConnection)
        }

        val mockProjectRepository = mock<ProjectRepository> {}
        val classUnderTest = LoginUserUseCase(mockUserRepository, mockProjectRepository, Dispatchers.Unconfined)
        val params = LoginUserUseCase.Params("test@test.pl", "123")
        val response = classUnderTest.executeAsync(params).await()
        assertEquals(Response.Status.ERROR, response.status)
        assertEquals(LoginUserUseCase.Error.ServerConnection, response.error)
    }
}
