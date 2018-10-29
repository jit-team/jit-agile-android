package pl.jitsolutions.agile.domain.usecases

import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import kotlinx.coroutines.experimental.Dispatchers
import kotlinx.coroutines.experimental.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import pl.jitsolutions.agile.domain.User
import pl.jitsolutions.agile.domain.response
import pl.jitsolutions.agile.repository.UserRepository

class LogoutCurrentUserUseCaseTest {
    @Test
    fun `test successful logout`() = runBlocking {
        val mockUserRepository = mock<UserRepository> {
            onBlocking { logout() } doReturn response(User("Name", "email@email.com"))
        }
        val useCase = LogoutCurrentUserUseCase(mockUserRepository, Dispatchers.Unconfined)

        val params = LogoutCurrentUserUseCase.Params()
        val actualResponse = useCase.executeAsync(params).await()

        val expectedResponse = response(User(name = "Name", email = "email@email.com"))
        assertEquals(expectedResponse, actualResponse)
    }
}