package pl.jitsolutions.agile.presentation.authorization.registrationSuccessful

import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import pl.jitsolutions.agile.common.Error
import pl.jitsolutions.agile.domain.Failure
import pl.jitsolutions.agile.domain.Success
import pl.jitsolutions.agile.domain.User
import pl.jitsolutions.agile.domain.usecases.GetLoggedUserUseCase
import pl.jitsolutions.agile.presentation.common.CoroutineViewModel
import pl.jitsolutions.agile.presentation.navigation.Navigator
import pl.jitsolutions.agile.presentation.navigation.Navigator.Destination.ProjectList
import pl.jitsolutions.agile.presentation.navigation.Navigator.Destination.RegistrationSuccessful

class RegistrationSuccessfulViewModel(
    private val getLoggedUserUseCase: GetLoggedUserUseCase,
    private val navigator: Navigator,
    mainDispatcher: CoroutineDispatcher
) : CoroutineViewModel(mainDispatcher) {

    val user = MutableLiveData<User>()

    init {
        executeGetUserName()
    }

    private fun executeGetUserName() = launch {
        val params = GetLoggedUserUseCase.Params
        val userResponse = getLoggedUserUseCase.executeAsync(params).await()
        when (userResponse) {
            is Success -> user.value = userResponse.data
            is Failure -> when (userResponse.error) {
                Error.DoesNotExist -> {
                    // TODO: user session not found, move to login screen
                }
            }
        }
    }

    fun confirmSuccess() {
        navigator.navigate(from = RegistrationSuccessful, to = ProjectList)
    }
}