package pl.jitsolutions.agile.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import pl.jitsolutions.agile.domain.GetCurrentUserUseCase
import pl.jitsolutions.agile.domain.LoginUserUseCase

class LoginViewModelFactory(private val getCurrentUserUseCase: GetCurrentUserUseCase,
                            private val loginUserUseCase: LoginUserUseCase) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return LoginViewModel(getCurrentUserUseCase, loginUserUseCase) as T
    }
}