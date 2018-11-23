package pl.jitsolutions.agile.presentation.authorization.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test
import pl.jitsolutions.agile.awaitResponseMock
import pl.jitsolutions.agile.domain.response
import pl.jitsolutions.agile.domain.usecases.UserLoginUseCase
import pl.jitsolutions.agile.presentation.navigation.Navigator

class LoginViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Test
    fun `login successful`() = runBlocking {

        val awaitResponseMock = awaitResponseMock(response(Unit))

        val userLoginUseCase = mock<UserLoginUseCase> {
            on {
                executeAsync(UserLoginUseCase.Params("", ""))
            } doReturn
                awaitResponseMock
        }

        val navigator = mock<Navigator>()

        val viewModel = LoginViewModel(userLoginUseCase, navigator, Dispatchers.Unconfined)

        val observerState = mock<Observer<LoginViewModel.State>>()
        viewModel.state.observeForever(observerState)

        verify(observerState).onChanged(LoginViewModel.State.None)

        viewModel.login()

        verify(observerState).onChanged(LoginViewModel.State.InProgress)

        verify(navigator).navigate(
            from = Navigator.Destination.Login,
            to = Navigator.Destination.ProjectList
        )

        verify(observerState).onChanged(LoginViewModel.State.Success)
    }
}