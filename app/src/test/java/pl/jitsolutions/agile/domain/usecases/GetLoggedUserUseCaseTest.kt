package pl.jitsolutions.agile.domain.usecases

import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import kotlinx.coroutines.experimental.Dispatchers
import kotlinx.coroutines.experimental.runBlocking
import org.junit.Test
import pl.jitsolutions.agile.assertThat
import pl.jitsolutions.agile.domain.User
import pl.jitsolutions.agile.domain.response
import pl.jitsolutions.agile.repository.UserRepository

class GetLoggedUserUseCaseTest {
    @Test
    fun `logged in user successful`() = runBlocking(Dispatchers.Default) {
        val mockUserRepository = mock<UserRepository> {
            onBlocking {
                getLoggedInUser()
            } doReturn
                response<User?>(User("tester", "email@email.com"))
        }
        val useCase = GetLoggedUserUseCase(mockUserRepository, Dispatchers.Unconfined)

        val response = useCase.executeAsync(GetLoggedUserUseCase.Params()).await()

        assertThat(response) { isSuccessful() }
    }
}