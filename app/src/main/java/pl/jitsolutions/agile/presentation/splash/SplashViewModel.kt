package pl.jitsolutions.agile.presentation.splash

import kotlinx.coroutines.experimental.CoroutineDispatcher
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import pl.jitsolutions.agile.presentation.common.CoroutineViewModel
import pl.jitsolutions.agile.presentation.navigation.Navigator
import pl.jitsolutions.agile.presentation.navigation.Navigator.Destination.LOGIN
import pl.jitsolutions.agile.presentation.navigation.Navigator.Destination.SPLASH

class SplashViewModel(private val navigator: Navigator,
                      mainDispatcher: CoroutineDispatcher
) : CoroutineViewModel(mainDispatcher) {
    fun login() = launch {
        delay(2000)
        navigator.navigate(from = SPLASH, to = LOGIN)
    }
}