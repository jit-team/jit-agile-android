package pl.jitsolutions.agile.presentation.authorization.registrationSuccessful

import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.experimental.CoroutineDispatcher
import kotlinx.coroutines.experimental.launch
import pl.jitsolutions.agile.domain.UserRegistrationSuccessFulUseCase
import pl.jitsolutions.agile.presentation.CoroutineViewModel
import pl.jitsolutions.agile.presentation.Navigator

class RegistrationSuccessfulViewModel(private val userRegistrationSuccessFulUseCase: UserRegistrationSuccessFulUseCase,
                                      private val navigator: Navigator,
                                      mainDispatcher: CoroutineDispatcher)
    : CoroutineViewModel(mainDispatcher) {

    val userName = MutableLiveData<String>()

    init {
        getUserName()
    }

    private fun getUserName() = launch {
        val params = UserRegistrationSuccessFulUseCase.Params()
        val user = userRegistrationSuccessFulUseCase.executeAsync(params).await()
        userName.value = user.data
    }

    fun startMainScreen() {
        navigator.goToMain()
    }
}