package pl.jitsolutions.agile.presentation.register

import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.experimental.CoroutineDispatcher
import kotlinx.coroutines.experimental.launch
import pl.jitsolutions.agile.domain.RegisterUserUseCase
import pl.jitsolutions.agile.domain.Response
import pl.jitsolutions.agile.presentation.CoroutineViewModel
import pl.jitsolutions.agile.utils.mutableLiveData


class RegisterViewModel(private val registerUserUseCase: RegisterUserUseCase, mainDispatcher: CoroutineDispatcher)
    : CoroutineViewModel(mainDispatcher) {

    val email = mutableLiveData("")
    val password = mutableLiveData("")
    val userName = mutableLiveData("")
    val registerState = mutableLiveData<RegisterState>(RegisterState.None)
    val userNameError: MutableLiveData<String> = mutableLiveData("")
    val emailError: MutableLiveData<String> = mutableLiveData("")
    val passwordError: MutableLiveData<String> = mutableLiveData("")

    fun register() = launch {
        userNameError.value = ""
        emailError.value = ""
        passwordError.value = ""
        registerState.value = RegisterState.InProgress
        val params = RegisterUserUseCase.Params(email.value!!, userName.value!!, password.value!!)
        val response = registerUserUseCase.executeAsync(params).await()
        when (response.status) {
            Response.Status.SUCCESS -> {
                userName.value = response.data
                registerState.value = RegisterState.Success
            }
            Response.Status.ERROR -> {
                registerState.value = RegisterState.Error
                when (response.error) {
                    //todo: strings ...
                    is RegisterUserUseCase.Error.WeakPassword -> passwordError.value = "weak password"
                    is RegisterUserUseCase.Error.EmptyUserName -> userNameError.value = "empty username"
                    is RegisterUserUseCase.Error.EmptyPassword -> passwordError.value = "empty password"
                    is RegisterUserUseCase.Error.EmptyEmail -> emailError.value = "empty email"
                    is RegisterUserUseCase.Error.InvalidCredentials -> emailError.value = "bad email"

                }
            }
        }
    }

    sealed class RegisterState {
        object None : RegisterState()
        object InProgress : RegisterState()
        object Error : RegisterState()
        object Success : RegisterState()
    }
}