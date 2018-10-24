package pl.jitsolutions.agile.presentation.authorization.resetPassword

import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import kotlinx.coroutines.experimental.Dispatchers
import kotlinx.coroutines.experimental.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import pl.jitsolutions.agile.domain.UserResetPasswordUseCase
import pl.jitsolutions.agile.domain.errorResponse
import pl.jitsolutions.agile.repository.UserRepository

class ResetPasswordViewModelTest {

    @Test
    fun testBadFormattedEmailException() = runBlocking(Dispatchers.Default) {
        val userRepository = mock<UserRepository> {}
        val userResetPasswordUseCase = UserResetPasswordUseCase(userRepository, Dispatchers.Default)
        val params = UserResetPasswordUseCase.Params("badformatedemail.pl")
        val response = userResetPasswordUseCase.executeAsync(params).await()
        assertEquals(UserResetPasswordUseCase.Error.InvalidEmail, response.error)
    }

    @Test
    fun testEmptyEmailException() = runBlocking(Dispatchers.Default) {
        val userRepository = mock<UserRepository> {}
        val userResetPasswordUseCase = UserResetPasswordUseCase(userRepository, Dispatchers.Default)
        val params = UserResetPasswordUseCase.Params("")
        val response = userResetPasswordUseCase.executeAsync(params).await()
        assertEquals(UserResetPasswordUseCase.Error.EmptyEmail, response.error)
    }

    @Test
    fun testEmailNotFoundException() = runBlocking(Dispatchers.Default) {
        val userRepository = mock<UserRepository> {
            onBlocking { resetPassword("test@test.pl") } doReturn errorResponse(error = UserRepository.Error.UserNotFound)
        }
        val userResetPasswordUseCase = UserResetPasswordUseCase(userRepository, Dispatchers.Default)
        val params = UserResetPasswordUseCase.Params("test@test.pl")
        val response = userResetPasswordUseCase.executeAsync(params).await()
        assertEquals(UserResetPasswordUseCase.Error.UserEmailNotFound, response.error)
    }
}