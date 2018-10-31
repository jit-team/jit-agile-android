package pl.jitsolutions.agile.domain.usecases

import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import kotlinx.coroutines.experimental.Dispatchers
import kotlinx.coroutines.experimental.runBlocking
import org.junit.Test
import pl.jitsolutions.agile.assertThat
import pl.jitsolutions.agile.domain.User
import pl.jitsolutions.agile.domain.response
import pl.jitsolutions.agile.hasUser
import pl.jitsolutions.agile.repository.UserRepository

class LogoutCurrentUserUseCaseTest {
    @Test
    fun `test successful logout`() = runBlocking {
        val mockUserRepository = mock<UserRepository> {
            onBlocking { logout() } doReturn response(
                User(
                    id = "",
                    name = "Name",
                    email = "email@email.com"
                )
            )
        }
        val useCase = LogoutCurrentUserUseCase(mockUserRepository, Dispatchers.Unconfined)

        val params = LogoutCurrentUserUseCase.Params()
        val response = useCase.executeAsync(params).await()

        assertThat(response) {
            isSuccessful()
            hasUser {
                withName("Name")
                withEmail("email@email.com")
            }
        }
    }
}