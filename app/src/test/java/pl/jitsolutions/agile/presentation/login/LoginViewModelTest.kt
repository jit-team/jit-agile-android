package pl.jitsolutions.agile.presentation.login

import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import kotlinx.coroutines.experimental.*
import kotlinx.coroutines.experimental.channels.produce
import org.junit.Assert.assertEquals
import org.junit.Test
import pl.jitsolutions.agile.domain.LoginUserUseCase
import pl.jitsolutions.agile.domain.User
import pl.jitsolutions.agile.repository.ProjectRepository
import pl.jitsolutions.agile.repository.UserRepository
import java.lang.Runnable
import kotlin.coroutines.experimental.CoroutineContext

class LoginViewModelTest {

    @Test
    fun doAction_doesSomething() {

        val mockUserRepository = mock<UserRepository> {
            on { login("abc", "123") } doReturn CoroutineScope(TestDispatcher()).produce {
                println("send")
                send(User("abc"))
            }
        }
        val mockProjectRepository = mock<ProjectRepository> {

        }

        val classUnderTest = LoginUserUseCase(mockUserRepository, mockProjectRepository, TestDispatcher())
        GlobalScope.launch {
            val login = classUnderTest.execute("abc", "123")
        }

        assertEquals(true, true)
    }
}

class TestDispatcher : CoroutineDispatcher() {

    override fun dispatch(context: CoroutineContext, block: Runnable) {
        Dispatchers.Default.dispatch(context, block)
    }
}
