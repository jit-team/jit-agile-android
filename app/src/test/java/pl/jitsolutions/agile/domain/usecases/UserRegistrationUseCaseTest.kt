package pl.jitsolutions.agile.domain.usecases

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
import pl.jitsolutions.agile.repository.UserRepository

class UserRegistrationUseCaseTest {

    @Test
    fun `registration successful`() = runBlocking {
        val mockUserRepository = mock<UserRepository> {
            onBlocking { register("tester", "tester@test.pl", "test123") } doReturn response(User("tester", "email@email.com"))
        }
        val params = UserRegistrationUseCase.Params("tester@test.pl", "tester", "test123")
        val useCase = UserRegistrationUseCase(mockUserRepository, Dispatchers.Unconfined)

        val actualResponse = useCase.executeAsync(params).await()

        assertEquals(Response.Status.SUCCESS, actualResponse.status)
    }

    @Test
    fun `bad formatted email`() = runBlocking(Dispatchers.Default) {
        val userRepository = mock<UserRepository> {}
        val params = UserRegistrationUseCase.Params("badformatedemail.pl", "test", "123456")
        val useCase = UserRegistrationUseCase(userRepository, Dispatchers.Default)

        val actualResponse = useCase.executeAsync(params).await()

        assertEquals(UserRegistrationUseCase.Error.InvalidEmail, actualResponse.error)
    }

    @Test
    fun `empty email`() = runBlocking(Dispatchers.Default) {
        val userRepository = mock<UserRepository> {}
        val params = UserRegistrationUseCase.Params("", "test", "123456")
        val useCase = UserRegistrationUseCase(userRepository, Dispatchers.Default)

        val actualResponse = useCase.executeAsync(params).await()

        assertEquals(UserRegistrationUseCase.Error.EmptyEmail, actualResponse.error)
    }

    @Test
    fun `email not found`() = runBlocking(Dispatchers.Default) {
        val userRepository = mock<UserRepository> {
            onBlocking { register("test", "test@test.pl", "123456") } doReturn errorResponse(error = UserRepository.Error.UserAlreadyExist)
        }
        val params = UserRegistrationUseCase.Params("test@test.pl", "test", "123456")
        val useCase = UserRegistrationUseCase(userRepository, Dispatchers.Default)

        val actualResponse = useCase.executeAsync(params).await()

        assertEquals(UserRegistrationUseCase.Error.UserAlreadyExist, actualResponse.error)
    }
}