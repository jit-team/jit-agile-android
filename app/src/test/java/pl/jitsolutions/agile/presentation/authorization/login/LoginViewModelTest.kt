package pl.jitsolutions.agile.presentation.authorization.login

import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import kotlinx.coroutines.experimental.Dispatchers
import kotlinx.coroutines.experimental.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import pl.jitsolutions.agile.domain.Response
import pl.jitsolutions.agile.domain.User
import pl.jitsolutions.agile.domain.response
import pl.jitsolutions.agile.domain.usecases.LoginUserUseCase
import pl.jitsolutions.agile.repository.ProjectRepository
import pl.jitsolutions.agile.repository.UserRepository

class LoginViewModelTest {

    @Test
    fun doAction_doesSomething() = runBlocking<Unit>(Dispatchers.Default) {
        val mockUserRepository = mock<UserRepository> {
            onBlocking { login("abc", "123") } doReturn response(User("abc"))
        }

        val mockProjectRepository = mock<ProjectRepository> {
            onBlocking { getProjects("abc") } doReturn response("Test group")
        }

        val classUnderTest = LoginUserUseCase(mockUserRepository, mockProjectRepository, Dispatchers.Unconfined)
        val params = LoginUserUseCase.Params("abc", "123")
        val response = classUnderTest.executeAsync(params).await()

        assertEquals(Response.Status.SUCCESS, response.status)
        assertEquals("abc, projects: Test group", response.data)
    }
}
