package pl.jitsolutions.agile.presentation.login

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ViewModel
import pl.jitsolutions.agile.domain.GetCurrentUserUseCase

class LoginViewModel(private val getCurrentUserUseCase: GetCurrentUserUseCase) : ViewModel() {
    val userName: MutableLiveData<String> = MutableLiveData()

    init {
        userName.value = getCurrentUserUseCase.execute().name
    }
}