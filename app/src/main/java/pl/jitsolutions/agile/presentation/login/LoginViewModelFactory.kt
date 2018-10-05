package pl.jitsolutions.agile.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import pl.jitsolutions.agile.domain.GetCurrentUserUseCase

class LoginViewModelFactory(private val getCurrentUserUseCase: GetCurrentUserUseCase) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return LoginViewModel(getCurrentUserUseCase) as T
    }
}