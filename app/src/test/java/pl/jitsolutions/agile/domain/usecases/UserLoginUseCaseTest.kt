package pl.jitsolutions.agile.domain.usecases

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.junit.Test
import pl.jitsolutions.agile.assertThat
import pl.jitsolutions.agile.common.Error
import pl.jitsolutions.agile.domain.User
import pl.jitsolutions.agile.domain.response
import pl.jitsolutions.agile.repository.NotificationRepository
import pl.jitsolutions.agile.repository.UserRepository

class UserLoginUseCaseTest {

    @Test
    fun `login successful`() = runBlocking {
        val mockUserRepository = mock<UserRepository> {
            onBlocking {
                login("abc@email.pl", "abc")
            } doReturn
                response(Unit)

            onBlocking {
                getLoggedInUser()
            } doReturn
                response<User?>(User("", "", ""))
        }

        val mockNotificationsRepository = mock<NotificationRepository> {
            onBlocking {
                assignDeviceTokenToUser(any())
            } doReturn
                response(Unit)
        }

        val params = UserLoginUseCase.Params("abc@email.pl", "abc")
        val useCase =
            UserLoginUseCase(mockUserRepository, mockNotificationsRepository, Dispatchers.Default)

        val response = useCase.executeAsync(params).await()

        assertThat(response) {
            isSuccessful()
        }
    }

    @Test
    fun `empty login`() = runBlocking {
        val mockUserRepository = mock<UserRepository>()
        val mockNotificationsRepository = mock<NotificationRepository>()

        val params = UserLoginUseCase.Params("", "abc")
        val useCase =
            UserLoginUseCase(mockUserRepository, mockNotificationsRepository, Dispatchers.Default)

        val response = useCase.executeAsync(params).await()

        assertThat(response) {
            hasError(Error.EmptyEmail)
        }
    }

    @Test
    fun `empty password`() = runBlocking {
        val mockUserRepository = mock<UserRepository>()
        val mockNotificationsRepository = mock<NotificationRepository>()

        val params = UserLoginUseCase.Params("abc@test.pl", "")
        val useCase =
            UserLoginUseCase(mockUserRepository, mockNotificationsRepository, Dispatchers.Default)

        val response = useCase.executeAsync(params).await()

        assertThat(response) {
            hasError(Error.EmptyPassword)
        }
    }
}
