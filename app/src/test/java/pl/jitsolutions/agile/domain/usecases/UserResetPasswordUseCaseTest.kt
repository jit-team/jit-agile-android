package pl.jitsolutions.agile.domain.usecases

import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import kotlinx.coroutines.experimental.Dispatchers
import kotlinx.coroutines.experimental.runBlocking
import org.junit.Assert.*
import org.junit.Test
import pl.jitsolutions.agile.domain.errorResponse
import pl.jitsolutions.agile.repository.UserRepository

class UserResetPasswordUseCaseTest {

    @Test
    fun `bad formatted email`() = runBlocking {
        val userRepository = mock<UserRepository> {}
        val params = UserResetPasswordUseCase.Params("badformatedemail.pl")
        val useCase = UserResetPasswordUseCase(userRepository, Dispatchers.Unconfined)

        val actualResponse = useCase.executeAsync(params).await()

        assertEquals(UserResetPasswordUseCase.Error.InvalidEmail, actualResponse.error)
    }

    @Test
    fun `empty email`() = runBlocking {
        val userRepository = mock<UserRepository> {}
        val params = UserResetPasswordUseCase.Params("")
        val useCase = UserResetPasswordUseCase(userRepository, Dispatchers.Unconfined)

        val response = useCase.executeAsync(params).await()

        assertEquals(UserResetPasswordUseCase.Error.EmptyEmail, response.error)
    }

    @Test
    fun `email not found`() = runBlocking {
        val userRepository = mock<UserRepository> {
            onBlocking { resetPassword("test@test.pl") } doReturn errorResponse(error = UserRepository.Error.UserNotFound)
        }
        val params = UserResetPasswordUseCase.Params("test@test.pl")
        val useCase = UserResetPasswordUseCase(userRepository, Dispatchers.Unconfined)

        val response = useCase.executeAsync(params).await()

        assertEquals(UserResetPasswordUseCase.Error.UserEmailNotFound, response.error)
    }
}