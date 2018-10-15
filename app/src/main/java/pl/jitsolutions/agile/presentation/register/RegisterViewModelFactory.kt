package pl.jitsolutions.agile.presentation.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.experimental.CoroutineDispatcher
import pl.jitsolutions.agile.domain.RegisterUserUseCase

@Suppress("UNCHECKED_CAST")
class RegisterViewModelFactory(private val mainDispatcher: CoroutineDispatcher,
                               private val registerUserUseCase: RegisterUserUseCase) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return RegisterViewModel(registerUserUseCase, mainDispatcher) as T
    }
}