package pl.jitsolutions.agile.presentation.login

import androidx.lifecycle.Observer
import kotlinx.coroutines.experimental.CoroutineDispatcher
import kotlinx.coroutines.experimental.channels.consumeEach
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
    val userName = mutableLiveData("")

    private val typedTextObserver = Observer<String> { loginState.value = LoginState.None }

    init {
        email.observeForever(typedTextObserver)
        password.observeForever(typedTextObserver)
    }

    fun login() = launch {
        val params = LoginUserUseCase.Params(email.value!!, password.value!!)
        loginUserUseCase.execute(params).consumeEach { response ->
            when (response.status) {
                Response.Status.SUCCESS -> {
                    loginState.value = LoginState.Success
                    userName.value = response.data!!
                }
                Response.Status.ERROR -> {
                    loginState.value = LoginState.Error
                    userName.value = ""
                }
                Response.Status.IN_PROGRESS -> {
                    loginState.value = LoginState.InProgress
                }
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
        object None : LoginState()
        object InProgress : LoginState()
        object Error : LoginState()
        object Success : LoginState()
    }
}