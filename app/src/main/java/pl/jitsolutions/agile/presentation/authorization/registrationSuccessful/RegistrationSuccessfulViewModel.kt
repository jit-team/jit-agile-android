package pl.jitsolutions.agile.presentation.authorization.registrationSuccessful

import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.experimental.CoroutineDispatcher
import kotlinx.coroutines.experimental.launch
import pl.jitsolutions.agile.domain.usecases.GetLoggedUserUseCase
import pl.jitsolutions.agile.presentation.common.CoroutineViewModel
import pl.jitsolutions.agile.presentation.navigation.Navigator
import pl.jitsolutions.agile.presentation.navigation.Navigator.Destination.PROJECT_LIST
import pl.jitsolutions.agile.presentation.navigation.Navigator.Destination.REGISTRATION_SUCCESSFUL

class RegistrationSuccessfulViewModel(private val getLoggedUserUseCase: GetLoggedUserUseCase,
                                      private val navigator: Navigator,
                                      mainDispatcher: CoroutineDispatcher
) : CoroutineViewModel(mainDispatcher) {

    val userName = MutableLiveData<String?>()

    init {
        getUserName()
    }

    private fun getUserName() = launch {
        val params = GetLoggedUserUseCase.Params()
        val userResponse = getLoggedUserUseCase.executeAsync(params).await()
        userName.value = userResponse.data?.name
    }

    fun confirmSuccess() {
        navigator.navigate(from = REGISTRATION_SUCCESSFUL, to = PROJECT_LIST)
    }
}