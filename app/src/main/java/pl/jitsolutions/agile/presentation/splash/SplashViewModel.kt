package pl.jitsolutions.agile.presentation.splash

import kotlinx.coroutines.experimental.CoroutineDispatcher
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import pl.jitsolutions.agile.domain.GetLoggedUserUseCase
import pl.jitsolutions.agile.domain.Response
import pl.jitsolutions.agile.domain.User
import pl.jitsolutions.agile.presentation.common.CoroutineViewModel
import pl.jitsolutions.agile.presentation.navigation.Navigator
import pl.jitsolutions.agile.presentation.navigation.Navigator.Destination.*

class SplashViewModel(private val getLoggedUserUseCase: GetLoggedUserUseCase,
                      private val navigator: Navigator,
                      mainDispatcher: CoroutineDispatcher
) : CoroutineViewModel(mainDispatcher) {

    private fun executeGetLoggedUser() = launch {
        val params = GetLoggedUserUseCase.Params()
        val response = getLoggedUserUseCase.executeAsync(params).await()
        when (response.status) {
            Response.Status.SUCCESS -> handleGetLoggedUserSuccess(response)
            Response.Status.ERROR -> {
                /*TODO*/
            }
        }
    }

    private fun handleGetLoggedUserSuccess(response: Response<User?>) {
        if (response.data != null) {
            navigator.navigate(SPLASH, PROJECT_LIST)
        } else {
            navigator.navigate(SPLASH, LOGIN)
        }
    }

    fun splashFinished() = launch {
        delay(500)
        executeGetLoggedUser()
    }
}