package pl.jitsolutions.agile.presentation.authorization.registration

import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import kotlinx.coroutines.experimental.Dispatchers
import kotlinx.coroutines.experimental.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import pl.jitsolutions.agile.domain.*
import pl.jitsolutions.agile.domain.usecases.UserRegistrationUseCase
import pl.jitsolutions.agile.repository.UserRepository

class RegistrationViewModelTest {

    @Test
    fun testRegistrationSuccessful() = runBlocking<Unit>(Dispatchers.Default) {
        val mockUserRepository = mock<UserRepository> {
            onBlocking { register("tester", "tester@test.pl", "test123") } doReturn response(User("tester"))
        }
        val classUnderTest = UserRegistrationUseCase(mockUserRepository, Dispatchers.Unconfined)
        val params = UserRegistrationUseCase.Params("tester@test.pl", "tester", "test123")
        val response = classUnderTest.executeAsync(params).await()
        assertEquals(Response.Status.SUCCESS, response.status)
    }

    @Test
    fun testBadFormattedEmailException() = runBlocking(Dispatchers.Default) {
        val userRepository = mock<UserRepository> {}
        val userResetPasswordUseCase = UserRegistrationUseCase(userRepository, Dispatchers.Default)
        val params = UserRegistrationUseCase.Params("badformatedemail.pl", "test", "123456")
        val response = userResetPasswordUseCase.executeAsync(params).await()
        assertEquals(UserRegistrationUseCase.Error.InvalidEmail, response.error)
    }

    @Test
    fun testEmptyEmailException() = runBlocking(Dispatchers.Default) {
        val userRepository = mock<UserRepository> {}
        val userResetPasswordUseCase = UserRegistrationUseCase(userRepository, Dispatchers.Default)
        val params = UserRegistrationUseCase.Params("", "test", "123456")
        val response = userResetPasswordUseCase.executeAsync(params).await()
        assertEquals(UserRegistrationUseCase.Error.EmptyEmail, response.error)
    }

    @Test
    fun testEmailNotFoundException() = runBlocking(Dispatchers.Default) {
        val userRepository = mock<UserRepository> {
            onBlocking { register("test", "test@test.pl", "123456") } doReturn errorResponse(error = UserRepository.Error.UserAlreadyExist)
        }
        val userResetPasswordUseCase = UserRegistrationUseCase(userRepository, Dispatchers.Default)
        val params = UserRegistrationUseCase.Params("test@test.pl", "test", "123456")
        val response = userResetPasswordUseCase.executeAsync(params).await()
        assertEquals(UserRegistrationUseCase.Error.UserAlreadyExist, response.error)
    }
}