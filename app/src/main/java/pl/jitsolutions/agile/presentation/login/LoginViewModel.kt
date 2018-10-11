package pl.jitsolutions.agile.presentation.login

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.experimental.Dispatchers
import kotlinx.coroutines.experimental.GlobalScope
import kotlinx.coroutines.experimental.channels.ReceiveChannel
import kotlinx.coroutines.experimental.channels.consumeEach
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.withContext
import pl.jitsolutions.agile.domain.LoginUserUseCase
import pl.jitsolutions.agile.domain.Response
import pl.jitsolutions.agile.utils.mutableLiveData

class LoginViewModel(private val loginUserUseCase: LoginUserUseCase) : ViewModel() {
    val password = mutableLiveData("")
    val email = mutableLiveData("")
    val loginState = mutableLiveData<LoginState>(LoginState.None)
    val userName = mutableLiveData("")
    private var loginChannel: ReceiveChannel<Response<String>>? = null


    fun login() {
        GlobalScope.launch {
            loginChannel = loginUserUseCase.execute(LoginUserUseCase.Params(email.value!!, password.value!!))
            loginChannel!!.consumeEach { response ->
                withContext(Dispatchers.Main) {
                    when (response.status) {
                        Response.Status.SUCCESS -> {
                            loginState.value = LoginState.Success
                            userName.value = response.data!!
                        }
                        Response.Status.ERROR -> {
                            loginState.value = LoginState.Error
                            userName.value = ""
                        }
                    }
                }
            }
        }
    }

    override fun onCleared() {
        loginChannel?.cancel()
    }
}

sealed class LoginState {
    object None : LoginState()
    object InProgress : LoginState()
    object Error : LoginState()
    object Success : LoginState()
}