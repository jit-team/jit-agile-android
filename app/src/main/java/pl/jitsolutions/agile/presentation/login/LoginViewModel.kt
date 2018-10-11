package pl.jitsolutions.agile.presentation.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.experimental.CoroutineDispatcher
import kotlinx.coroutines.experimental.GlobalScope
import kotlinx.coroutines.experimental.channels.ReceiveChannel
import kotlinx.coroutines.experimental.launch
import pl.jitsolutions.agile.domain.LoginUserUseCase
import pl.jitsolutions.agile.domain.Response
import pl.jitsolutions.agile.utils.execute
import pl.jitsolutions.agile.utils.mutableLiveData

class LoginViewModel(private val mainDispatcher: CoroutineDispatcher,
                     private val loginUserUseCase: LoginUserUseCase
) : ViewModel() {
    val password = mutableLiveData("")
    val email = mutableLiveData("")
    val loginState = mutableLiveData<LoginState>(LoginState.None)
    val userName = mutableLiveData("")
    val register = MutableLiveData<Any>()

    private var loginChannel: ReceiveChannel<Response<String>>? = null
        //Add custom property delegate
        set(value) {
            field?.cancel()
            field = value
        }

    private val typedTextObserver = Observer<String> { loginState.value = LoginState.None }

    init {
        email.observeForever(typedTextObserver)
        password.observeForever(typedTextObserver)
    }


    fun login() = GlobalScope.launch(mainDispatcher) {
        val params = LoginUserUseCase.Params(email.value!!, password.value!!)
        loginChannel = loginUserUseCase.execute(params, mainDispatcher) { response ->
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
        register.value = null
    }

    override fun onCleared() {
        loginChannel?.cancel()
        email.removeObserver(typedTextObserver)
        password.removeObserver(typedTextObserver)
    }

    sealed class LoginState {
        object None : LoginState()
        object InProgress : LoginState()
        object Error : LoginState()
        object Success : LoginState()
    }
}