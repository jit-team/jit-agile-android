package pl.jitsolutions.agile.presentation.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import pl.jitsolutions.agile.domain.LoginUserUseCase
import pl.jitsolutions.agile.domain.User
import pl.jitsolutions.agile.utils.mutableLiveData

class LoginViewModel(private val loginUserUseCase: LoginUserUseCase) : ViewModel() {
    val password = mutableLiveData("")
    val email = mutableLiveData("")
    val loginState = mutableLiveData<LoginState>(LoginState.None)

    private var loginLiveData: LiveData<User>? = null
    private var loginObserver: Observer<User>? = null

    fun login() {
        loginLiveData?.removeObserver { loginObserver }

        loginObserver = Observer { loginState.value = LoginState.Success }
        loginLiveData = loginUserUseCase.execute(LoginUserUseCase.Params(email.value!!, password.value!!))
        loginLiveData?.observeForever { loginObserver }
    }

    override fun onCleared() {
        loginLiveData?.removeObserver { loginObserver }
    }
}

sealed class LoginState {
    object None : LoginState()
    object InProgress : LoginState()
    object Error : LoginState()
    object Success : LoginState()
}