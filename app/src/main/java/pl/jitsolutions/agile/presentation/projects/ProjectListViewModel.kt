package pl.jitsolutions.agile.presentation.projects

import kotlinx.coroutines.experimental.CoroutineDispatcher
import kotlinx.coroutines.experimental.launch
import pl.jitsolutions.agile.domain.Response
import pl.jitsolutions.agile.domain.User
import pl.jitsolutions.agile.domain.usecases.GetLoggedUserUseCase
import pl.jitsolutions.agile.presentation.common.CoroutineViewModel
import pl.jitsolutions.agile.presentation.navigation.Navigator
import pl.jitsolutions.agile.presentation.navigation.Navigator.Destination.LOGIN
import pl.jitsolutions.agile.presentation.navigation.Navigator.Destination.PROJECT_LIST
import pl.jitsolutions.agile.utils.mutableLiveData

class ProjectListViewModel(private val getLoggedUserUseCase: GetLoggedUserUseCase,
                           private val navigator: Navigator,
                           mainDispatcher: CoroutineDispatcher
) : CoroutineViewModel(mainDispatcher) {
    val userName = mutableLiveData("")

    init {
        executeGetLoggedUser()
    }

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
            userName.value = response.data.name
        } else {
            navigator.navigate(PROJECT_LIST, LOGIN)
        }
    }
}