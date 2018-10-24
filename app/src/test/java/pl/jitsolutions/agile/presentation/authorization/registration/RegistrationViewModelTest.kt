package pl.jitsolutions.agile.presentation.authorization.registration

import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import kotlinx.coroutines.experimental.Dispatchers
import kotlinx.coroutines.experimental.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import pl.jitsolutions.agile.domain.Response
import pl.jitsolutions.agile.domain.User
import pl.jitsolutions.agile.domain.response
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
}