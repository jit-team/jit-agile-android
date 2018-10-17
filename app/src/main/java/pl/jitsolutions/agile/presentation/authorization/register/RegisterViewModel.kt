package pl.jitsolutions.agile.presentation.authorization.register

import kotlinx.coroutines.experimental.CoroutineDispatcher
import kotlinx.coroutines.experimental.launch
import pl.jitsolutions.agile.domain.RegisterUserUseCase
import pl.jitsolutions.agile.domain.Response
import pl.jitsolutions.agile.presentation.CoroutineViewModel
import pl.jitsolutions.agile.presentation.Navigator
import pl.jitsolutions.agile.utils.mutableLiveData


class RegisterViewModel(private val registerUserUseCase: RegisterUserUseCase,
                        private val navigator: Navigator,
                        mainDispatcher: CoroutineDispatcher)
    : CoroutineViewModel(mainDispatcher) {

    val email = mutableLiveData("")
    val password = mutableLiveData("")
    val userName = mutableLiveData("")
    val registerState = mutableLiveData<RegisterState>(RegisterState.None)

    fun register() = launch {
        registerState.value = RegisterState.InProgress
        val params = RegisterUserUseCase.Params(email.value!!, userName.value!!, password.value!!)
        val response = registerUserUseCase.executeAsync(params).await()
        when (response.status) {
            Response.Status.SUCCESS -> {
                userName.value = response.data
                registerState.value = RegisterState.Success
            }
            Response.Status.ERROR -> {
                val type = when (response.error) {
                    is RegisterUserUseCase.Error.WeakPassword -> RegisterTypeError.PASSWORD
                    is RegisterUserUseCase.Error.EmptyUserName -> RegisterTypeError.USERNAME
                    is RegisterUserUseCase.Error.EmptyPassword -> RegisterTypeError.PASSWORD
                    is RegisterUserUseCase.Error.EmptyEmail -> RegisterTypeError.EMAIL
                    is RegisterUserUseCase.Error.InvalidCredentials -> RegisterTypeError.EMAIL
                    else -> RegisterTypeError.SERVER
                }
                registerState.value = RegisterState.Error(type)
            }
        }
    }

    sealed class RegisterState {
        fun isErrorOfType(type: RegisterTypeError): Boolean {
            return this is RegisterState.Error && this.type == type
        }

        object None : RegisterState()
        object InProgress : RegisterState()
        data class Error(val type: RegisterTypeError) : RegisterState()
        object Success : RegisterState()

    }

    enum class RegisterTypeError { USERNAME, EMAIL, PASSWORD, SERVER }
}