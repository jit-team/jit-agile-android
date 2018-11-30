package pl.jitsolutions.agile.presentation.splash

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import pl.jitsolutions.agile.common.Error
import pl.jitsolutions.agile.domain.Failure
import pl.jitsolutions.agile.domain.Success
import pl.jitsolutions.agile.domain.usecases.GetApplicationVersionUseCase
import pl.jitsolutions.agile.domain.usecases.GetLoggedUserUseCase
import pl.jitsolutions.agile.presentation.common.CoroutineViewModel
import pl.jitsolutions.agile.presentation.navigation.Navigator
import pl.jitsolutions.agile.presentation.navigation.Navigator.Destination.Login
import pl.jitsolutions.agile.presentation.navigation.Navigator.Destination.ProjectList
import pl.jitsolutions.agile.presentation.navigation.Navigator.Destination.Splash
import pl.jitsolutions.agile.utils.mutableLiveData

class SplashViewModel(
    private val getLoggedUserUseCase: GetLoggedUserUseCase,
    private val getApplicationVersionUseCase: GetApplicationVersionUseCase,
    private val navigator: Navigator,
    mainDispatcher: CoroutineDispatcher
) : CoroutineViewModel(mainDispatcher) {

    val version = mutableLiveData("")

    init {
        executeGetApplicationVersion()
    }

    private fun executeGetApplicationVersion() = launch {
        val params = GetApplicationVersionUseCase.Params
        val response = getApplicationVersionUseCase.executeAsync(params).await()
        when (response) {
            is Success -> version.value = response.data
            is Failure -> navigator.forceFinish()
        }
    }

    fun splashFinished() = launch {
        delay(500)
        executeGetLoggedUser()
    }

    private fun executeGetLoggedUser() = launch {
        val params = GetLoggedUserUseCase.Params
        val response = getLoggedUserUseCase.executeAsync(params).await()
        when (response) {
            is Success -> navigator.navigate(from = Splash, to = ProjectList)
            is Failure -> if(response.error == Error.DoesNotExist) {
                navigator.navigate(from = Splash, to = Login)
            } else {
                navigator.forceFinish()
            }
        }
    }
}