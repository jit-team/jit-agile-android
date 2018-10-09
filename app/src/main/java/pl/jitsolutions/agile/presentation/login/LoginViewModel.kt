package pl.jitsolutions.agile.presentation.login

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import pl.jitsolutions.agile.domain.GetCurrentUserUseCase
import pl.jitsolutions.agile.domain.LoginUserUseCase
import pl.jitsolutions.agile.domain.User

class LoginViewModel(private val getCurrentUserUseCase: GetCurrentUserUseCase,
                     private val loginUserUseCase: LoginUserUseCase) : ViewModel() {
    val userName: MutableLiveData<String> = MutableLiveData()
    val password = ObservableField<String>()
    val email = ObservableField<String>()

    fun login(): MutableLiveData<User> {
        val email = email.get()
        val password = password.get()
        if (email != null && password != null) {
            return loginUserUseCase.execute(email, password)
        }
        return MutableLiveData()
    }
}