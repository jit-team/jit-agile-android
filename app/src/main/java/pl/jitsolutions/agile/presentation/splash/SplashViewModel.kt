package pl.jitsolutions.agile.presentation.splash

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import pl.jitsolutions.agile.domain.Response
import pl.jitsolutions.agile.domain.Response.Status.FAILURE
import pl.jitsolutions.agile.domain.Response.Status.SUCCESS
import pl.jitsolutions.agile.domain.User
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
        when (response.status) {
            SUCCESS -> version.value = response.data
            FAILURE -> navigator.forceFinish()
        }
    }

    fun splashFinished() = launch {
        delay(500)
        executeGetLoggedUser()
    }

    private fun executeGetLoggedUser() = launch {
        val params = GetLoggedUserUseCase.Params
        val response = getLoggedUserUseCase.executeAsync(params).await()
        when (response.status) {
            SUCCESS -> handleGetLoggedUserSuccess(response)
            FAILURE -> navigator.forceFinish()
        }
    }

    private fun handleGetLoggedUserSuccess(response: Response<User?>) {
        val isUserLoggedIn = response.data != null
        if (isUserLoggedIn) {
            navigator.navigate(from = Splash, to = ProjectList)
        } else {
            navigator.navigate(from = Splash, to = Login)
        }
    }
}