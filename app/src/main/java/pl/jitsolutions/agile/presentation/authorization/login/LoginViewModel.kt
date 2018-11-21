package pl.jitsolutions.agile.presentation.authorization.login

import androidx.lifecycle.Observer
import kotlinx.coroutines.experimental.CoroutineDispatcher
import kotlinx.coroutines.experimental.launch
import pl.jitsolutions.agile.Error
import pl.jitsolutions.agile.domain.Response
import pl.jitsolutions.agile.domain.usecases.UserLoginUseCase
import pl.jitsolutions.agile.presentation.common.CoroutineViewModel
import pl.jitsolutions.agile.presentation.navigation.Navigator
import pl.jitsolutions.agile.presentation.navigation.Navigator.Destination.Login
import pl.jitsolutions.agile.presentation.navigation.Navigator.Destination.ProjectList
import pl.jitsolutions.agile.presentation.navigation.Navigator.Destination.Registration
import pl.jitsolutions.agile.presentation.navigation.Navigator.Destination.ResetPassword
import pl.jitsolutions.agile.utils.mutableLiveData

class LoginViewModel(
    private val userLoginUseCase: UserLoginUseCase,
    private val navigator: Navigator,
    mainDispatcher: CoroutineDispatcher
) : CoroutineViewModel(mainDispatcher) {
    val password = mutableLiveData("")
    val email = mutableLiveData("")
    val state = mutableLiveData<State>(State.None)

    private val typedTextObserver = Observer<String> { state.value = State.None }

    init {
        email.observeForever(typedTextObserver)
        password.observeForever(typedTextObserver)
    }

    fun login() = launch {
        state.value = State.InProgress
        val params = UserLoginUseCase.Params(email.value!!, password.value!!)
        val response = userLoginUseCase.executeAsync(params).await()
        when (response.status) {
            Response.Status.SUCCESS -> {
                state.value = State.Success
                navigator.navigate(from = Login, to = ProjectList)
            }
            Response.Status.FAILURE -> {
                state.value = State.Fail(response.error!!)
            }
        }
    }

    fun register() {
        navigator.navigate(from = Login, to = Registration)
    }

    fun resetPassword() {
        navigator.navigate(from = Login, to = ResetPassword)
    }

    override fun onCleared() {
        email.removeObserver(typedTextObserver)
        password.removeObserver(typedTextObserver)
        super.onCleared()
    }

    sealed class State {
        fun isErrorOfType(type: Error): Boolean {
            return this is Fail && this.type == type
        }

        object None : State()
        object InProgress : State()
        data class Fail(val type: Error) : State()
        object Success : State()
    }
}