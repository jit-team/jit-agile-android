package pl.jitsolutions.agile.presentation.authorization.login

import androidx.lifecycle.Observer
import kotlinx.coroutines.experimental.CoroutineDispatcher
import kotlinx.coroutines.experimental.launch
import pl.jitsolutions.agile.domain.Response
import pl.jitsolutions.agile.domain.usecases.LoginUserUseCase
import pl.jitsolutions.agile.presentation.common.CoroutineViewModel
import pl.jitsolutions.agile.presentation.navigation.Navigator
import pl.jitsolutions.agile.presentation.navigation.Navigator.Destination.Login
import pl.jitsolutions.agile.presentation.navigation.Navigator.Destination.ProjectList
import pl.jitsolutions.agile.presentation.navigation.Navigator.Destination.Registration
import pl.jitsolutions.agile.presentation.navigation.Navigator.Destination.ResetPassword
import pl.jitsolutions.agile.utils.mutableLiveData

class LoginViewModel(
    private val loginUserUseCase: LoginUserUseCase,
    private val navigator: Navigator,
    mainDispatcher: CoroutineDispatcher
) : CoroutineViewModel(mainDispatcher) {
    val password = mutableLiveData("")
    val email = mutableLiveData("")
    val state = mutableLiveData<LoginState>(LoginState.None)

    private val typedTextObserver = Observer<String> { state.value = LoginState.None }

    init {
        email.observeForever(typedTextObserver)
        password.observeForever(typedTextObserver)
    }

    fun login() = launch {
        state.value = LoginState.InProgress
        val params = LoginUserUseCase.Params(email.value!!, password.value!!)
        val response = loginUserUseCase.executeAsync(params).await()
        when (response.status) {
            Response.Status.SUCCESS -> {
                state.value = LoginState.Success
                navigator.navigate(from = Login, to = ProjectList)
            }
            Response.Status.ERROR -> {
                val type = when (response.error) {
                    is LoginUserUseCase.Error.UnknownError -> LoginErrorType.SERVER
                    is LoginUserUseCase.Error.ServerConnection -> LoginErrorType.SERVER
                    is LoginUserUseCase.Error.WrongPassword -> LoginErrorType.PASSWORD
                    is LoginUserUseCase.Error.InvalidEmail -> LoginErrorType.EMAIL
                    is LoginUserUseCase.Error.UserEmailNotFound -> LoginErrorType.EMAIL_NOT_FOUND
                    is LoginUserUseCase.Error.EmptyEmail -> LoginErrorType.EMAIL
                    is LoginUserUseCase.Error.EmptyPassword -> LoginErrorType.PASSWORD
                    else -> LoginErrorType.SERVER
                }
                state.value = LoginState.Fail(type)
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

    sealed class LoginState {
        fun isErrorOfType(type: LoginErrorType): Boolean {
            return this is Fail && this.type == type
        }

        object None : LoginState()
        object InProgress : LoginState()
        data class Fail(val type: LoginErrorType) : LoginState()
        object Success : LoginState()
    }

    enum class LoginErrorType { EMAIL, EMAIL_NOT_FOUND, PASSWORD, SERVER, UNKNOWN }
}