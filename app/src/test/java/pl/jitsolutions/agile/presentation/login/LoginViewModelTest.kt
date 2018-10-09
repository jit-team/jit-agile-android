package pl.jitsolutions.agile.presentation.login

import com.nhaarman.mockitokotlin2.mock
import org.junit.Assert.assertEquals
import org.junit.Test
import pl.jitsolutions.agile.domain.User
import pl.jitsolutions.agile.repository.UserRepository

class LoginViewModelTest {

    @Test
    fun doAction_doesSomething(){

        val mock = mock<UserRepository> {
            on { getCurrentUser() } doReturn User("User")
        }
        val classUnderTest = GetCurrentUserUseCase(mock)
        assertEquals(classUnderTest.execute().name, "User")
    }
}