package pl.jitsolutions.agile.presentation.authorization.registrationSuccessful

import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import kotlinx.coroutines.experimental.Dispatchers
import kotlinx.coroutines.experimental.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import pl.jitsolutions.agile.domain.Response
import pl.jitsolutions.agile.domain.User
import pl.jitsolutions.agile.domain.response
import pl.jitsolutions.agile.domain.usecases.GetLoggedUserUseCase
import pl.jitsolutions.agile.repository.UserRepository


class RegistrationSuccessfulViewModelTest {

    @Test
    fun testLoggedInUserSuccessful() = runBlocking(Dispatchers.Default) {

        val mockUserRepository = mock<UserRepository> {
            onBlocking { getLoggedInUser() } doReturn response<User?>(User("tester"))
        }

        val classUnderTest = GetLoggedUserUseCase(mockUserRepository, Dispatchers.Unconfined)
        val params = GetLoggedUserUseCase.Params()
        val response = classUnderTest.executeAsync(params).await()
        assertEquals(Response.Status.SUCCESS, response.status)
    }

}