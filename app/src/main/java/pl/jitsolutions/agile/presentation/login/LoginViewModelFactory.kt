package pl.jitsolutions.agile.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import pl.jitsolutions.agile.domain.LoginUserUseCase

@Suppress("UNCHECKED_CAST")
class LoginViewModelFactory(private val loginUserUseCase: LoginUserUseCase) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return LoginViewModel(loginUserUseCase) as T
    }
}