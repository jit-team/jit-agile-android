package pl.jitsolutions.agile.presentation.login

import androidx.lifecycle.Observer
import kotlinx.coroutines.experimental.CoroutineDispatcher
import kotlinx.coroutines.experimental.launch
import pl.jitsolutions.agile.domain.LoginUserUseCase
import pl.jitsolutions.agile.domain.Response
import pl.jitsolutions.agile.presentation.CoroutineViewModel
import pl.jitsolutions.agile.presentation.Navigator
import pl.jitsolutions.agile.utils.mutableLiveData

class LoginViewModel(private val loginUserUseCase: LoginUserUseCase,
                     private val navigator: Navigator,
                     mainDispatcher: CoroutineDispatcher
) : CoroutineViewModel(mainDispatcher) {
    val password = mutableLiveData("")
    val email = mutableLiveData("")
    val loginState = mutableLiveData<LoginState>(LoginState.None)

    private val typedTextObserver = Observer<String> { loginState.value = LoginState.None }

    init {
        email.observeForever(typedTextObserver)
        password.observeForever(typedTextObserver)
    }

    fun login() = launch {
        loginState.value = LoginState.InProgress

        val params = LoginUserUseCase.Params(email.value!!, password.value!!)
        val response = loginUserUseCase.executeAsync(params).await()
        when (response.status) {
            Response.Status.SUCCESS -> {
                loginState.value = LoginState.Success
                navigator.goToMain()
            }
            Response.Status.ERROR -> {
                val type = when (response.error) {
                    is LoginUserUseCase.Error.UnknownError -> LoginErrorType.SERVER
                    is LoginUserUseCase.Error.ServerConnection -> LoginErrorType.SERVER
                    is LoginUserUseCase.Error.WrongPassword -> LoginErrorType.PASSWORD
                    is LoginUserUseCase.Error.UserEmailNotFound -> LoginErrorType.EMAIL
                    else -> LoginErrorType.SERVER
                }
                loginState.value = LoginState.Error(type)
            }
        }
    }

    fun register() {
        navigator.goToRegistration()
    }

    override fun onCleared() {
        email.removeObserver(typedTextObserver)
        password.removeObserver(typedTextObserver)
        super.onCleared()
    }

    sealed class LoginState {
        fun isErrorOfType(type: LoginErrorType): Boolean {
            return this is Error && this.type == type
        }


        object None : LoginState()
        object InProgress : LoginState()
        data class Error(val type: LoginErrorType) : LoginState()
        object Success : LoginState()
    }

    enum class LoginErrorType { EMAIL, PASSWORD, SERVER }
}