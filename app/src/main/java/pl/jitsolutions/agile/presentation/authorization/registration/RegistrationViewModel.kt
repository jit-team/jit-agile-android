package pl.jitsolutions.agile.presentation.authorization.registration

import androidx.lifecycle.Observer
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import pl.jitsolutions.agile.common.Error
import pl.jitsolutions.agile.domain.Response
import pl.jitsolutions.agile.domain.usecases.UserRegistrationUseCase
import pl.jitsolutions.agile.presentation.common.CoroutineViewModel
import pl.jitsolutions.agile.presentation.navigation.Navigator
import pl.jitsolutions.agile.presentation.navigation.Navigator.Destination.Registration
import pl.jitsolutions.agile.presentation.navigation.Navigator.Destination.RegistrationSuccessful
import pl.jitsolutions.agile.utils.mutableLiveData

class RegistrationViewModel(
    private val userRegistrationUseCase: UserRegistrationUseCase,
    private val navigator: Navigator,
    mainDispatcher: CoroutineDispatcher
) : CoroutineViewModel(mainDispatcher) {

    val email = mutableLiveData("")
    val password = mutableLiveData("")
    val userName = mutableLiveData("")
    val state = mutableLiveData<State>(State.None)

    private val typedTextObserver = Observer<String> { state.value = State.None }

    init {
        email.observeForever(typedTextObserver)
        password.observeForever(typedTextObserver)
        userName.observeForever(typedTextObserver)
    }

    fun register() = launch {
        state.value = State.InProgress
        val params =
            UserRegistrationUseCase.Params(email.value!!, userName.value!!, password.value!!)
        val response = userRegistrationUseCase.executeAsync(params).await()
        when (response.status) {
            Response.Status.SUCCESS -> {
                state.value = State.Success
                navigator.navigate(from = Registration, to = RegistrationSuccessful)
            }
            Response.Status.FAILURE -> state.value = State.Fail(response.error!!)
        }
    }

    override fun onCleared() {
        userName.removeObserver(typedTextObserver)
        email.removeObserver(typedTextObserver)
        password.removeObserver(typedTextObserver)
        super.onCleared()
    }

    sealed class State {
        fun isErrorOfType(type: Error): Boolean {
            return this is RegistrationViewModel.State.Fail && this.type == type
        }

        object None : State()
        object InProgress : State()
        data class Fail(val type: Error) : State()
        object Success : State()
    }
}