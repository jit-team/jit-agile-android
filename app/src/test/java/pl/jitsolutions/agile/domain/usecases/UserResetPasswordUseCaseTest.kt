package pl.jitsolutions.agile.domain.usecases

import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import kotlinx.coroutines.experimental.Dispatchers
import kotlinx.coroutines.experimental.runBlocking
import org.junit.Test
import pl.jitsolutions.agile.assertThat
import pl.jitsolutions.agile.domain.errorResponse
import pl.jitsolutions.agile.repository.UserRepository

class UserResetPasswordUseCaseTest {

    @Test
    fun `bad formatted email`() = runBlocking {
        val userRepository = mock<UserRepository>()
        val params = UserResetPasswordUseCase.Params("badformatedemail.pl")
        val useCase = UserResetPasswordUseCase(userRepository, Dispatchers.Unconfined)

        val response = useCase.executeAsync(params).await()

        assertThat(response) {
            hasError(UserResetPasswordUseCase.Error.InvalidEmail)
        }
    }

    @Test
    fun `empty email`() = runBlocking {
        val userRepository = mock<UserRepository>()
        val params = UserResetPasswordUseCase.Params("")
        val useCase = UserResetPasswordUseCase(userRepository, Dispatchers.Unconfined)

        val response = useCase.executeAsync(params).await()

        assertThat(response) {
            hasError(UserResetPasswordUseCase.Error.EmptyEmail)
        }
    }

    @Test
    fun `email not found`() = runBlocking {
        val userRepository = mock<UserRepository> {
            onBlocking {
                resetPassword("test@test.pl")
            } doReturn
                errorResponse(error = UserRepository.Error.UserNotFound)
        }
        val params = UserResetPasswordUseCase.Params("test@test.pl")
        val useCase = UserResetPasswordUseCase(userRepository, Dispatchers.Unconfined)

        val response = useCase.executeAsync(params).await()

        assertThat(response) {
            hasError(UserResetPasswordUseCase.Error.UserEmailNotFound)
        }
    }
}