package pl.jitsolutions.agile.presentation.authorization.registration

import androidx.lifecycle.Observer
import kotlinx.coroutines.experimental.CoroutineDispatcher
import kotlinx.coroutines.experimental.launch
import pl.jitsolutions.agile.domain.Response
import pl.jitsolutions.agile.domain.usecases.UserRegistrationUseCase
import pl.jitsolutions.agile.presentation.common.CoroutineViewModel
import pl.jitsolutions.agile.presentation.navigation.Navigator
import pl.jitsolutions.agile.presentation.navigation.Navigator.Destination.REGISTRATION
import pl.jitsolutions.agile.presentation.navigation.Navigator.Destination.REGISTRATION_SUCCESSFUL
import pl.jitsolutions.agile.utils.mutableLiveData


class RegistrationViewModel(private val userRegistrationUseCase: UserRegistrationUseCase,
                            private val navigator: Navigator,
                            mainDispatcher: CoroutineDispatcher)
    : CoroutineViewModel(mainDispatcher) {

    val email = mutableLiveData("")
    val password = mutableLiveData("")
    val userName = mutableLiveData("")
    val registrationState = mutableLiveData<RegistrationState>(RegistrationState.None)

    private val typedTextObserver = Observer<String> { registrationState.value = RegistrationState.None }

    init {
        email.observeForever(typedTextObserver)
        password.observeForever(typedTextObserver)
        userName.observeForever(typedTextObserver)
    }

    fun register() = launch {
        registrationState.value = RegistrationState.InProgress
        val params = UserRegistrationUseCase.Params(email.value!!, userName.value!!, password.value!!)
        val response = userRegistrationUseCase.executeAsync(params).await()
        when (response.status) {
            Response.Status.SUCCESS -> {
                userName.value = response.data
                registrationState.value = RegistrationState.Success
                navigator.navigate(from = REGISTRATION, to = REGISTRATION_SUCCESSFUL)
            }
            Response.Status.ERROR -> {
                val type = when (response.error) {
                    is UserRegistrationUseCase.Error.WeakPassword -> RegisterTypeError.PASSWORD
                    is UserRegistrationUseCase.Error.EmptyUserName -> RegisterTypeError.USERNAME
                    is UserRegistrationUseCase.Error.EmptyPassword -> RegisterTypeError.PASSWORD
                    is UserRegistrationUseCase.Error.EmptyEmail -> RegisterTypeError.EMAIL
                    is UserRegistrationUseCase.Error.InvalidCredentials -> RegisterTypeError.EMAIL
                    else -> RegisterTypeError.SERVER
                }
                registrationState.value = RegistrationState.Error(type)
            }
        }
    }

    override fun onCleared() {
        userName.removeObserver(typedTextObserver)
        email.removeObserver(typedTextObserver)
        password.removeObserver(typedTextObserver)
        super.onCleared()
    }

    sealed class RegistrationState {
        fun isErrorOfType(type: RegisterTypeError): Boolean {
            return this is RegistrationState.Error && this.type == type
        }

        object None : RegistrationState()
        object InProgress : RegistrationState()
        data class Error(val type: RegisterTypeError) : RegistrationState()
        object Success : RegistrationState()

    }

    enum class RegisterTypeError { USERNAME, EMAIL, PASSWORD, SERVER }
}